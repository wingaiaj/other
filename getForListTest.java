package com.zx.connection;

import com.zx.jdbcutils.JDBCUtils;
import com.zx.preparedstatement.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName getForListTest
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/24 9:28
 * @Version 1.0
 */
public class getForListTest {
//java泛型关于方法返回值前面的是什么？
// 前面的T的声明，跟类后面的 <T> 没有关系。
//方法前面的<T>是给这个方法级别指定泛型。
    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<T> list = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[0]);
            }
            //返回结果集
            resultSet = preparedStatement.executeQuery();
            //结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();
            //创建集合
            list = new ArrayList<>();
            while (resultSet.next()) {
                //创建对象
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object value = resultSet.getObject(i + 1);
                    String columnName = metaData.getColumnName(i + 1);
                    Field declaredField = clazz.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(t, value);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }
        return null;
    }

    @Test
    public void testGetForList() {

        String sql = "select id,name,email,birth from customers where id < ?";
        List<Customer> forList = getForList(Customer.class, sql, 12);
        forList.forEach(System.out::println);
    }
}
