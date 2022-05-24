package com.zx.connection;

import com.zx.jdbcutils.JDBCUtils;
import com.zx.preparedstatement.Customer;
import com.zx.preparedstatement.Order;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @ClassName PreparedStatementQuery
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/24 8:58
 * @Version 1.0
 */
public class PreparedStatementQuery {
    //通用方法
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //获取连接
            connection = JDBCUtils.getConnection();
            //预编译sql语句
            preparedStatement = connection.prepareStatement(sql);
            //设置占位符
            for (int i = 0; i < args.length; i++) {

                preparedStatement.setObject(i + 1, args[0]);
            }
            //执行sql获取结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {//判断结果集下一位是否有元素
                //获取当前形参对象
                T t = clazz.newInstance();
                //取出每行的数据
                for (int i = 0; i < columnCount; i++) {
                    //获取列的参数
                    Object value = resultSet.getObject(i + 1);
                    //获取列别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //反射给对象赋值
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    //设置位可编辑
                    declaredField.setAccessible(true);
                    //将当前对象属性 赋值
                    declaredField.set(t, value);
                }
                //返回当前对象
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }
        return null;
    }

    @Test
    public void testPreparedStatementQuery() {

        String sql = "select id,name,email,birth from customers where id = ?";
        Customer instance = getInstance(Customer.class, sql, 12);
        System.out.println(instance);
    }
}
