package com.zx.preparedstatement;

import com.zx.jdbcutils.JDBCUtils;
import org.junit.Test;
import org.junit.experimental.theories.FromDataPoints;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.cert.CertPathValidatorSpi;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @ClassName PreparedStatement
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/23 8:19
 * @Version 1.0
 */
/*
使用PreparedStatement替换statement实现数据表的增删改查
 */
public class PreparedStatement {
    @Test
    public void selectTableTest() {
        Connection connection = null;
        java.sql.PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] data = null;
        try {
            //获取连接
            connection = JDBCUtils.getConnection();
            //sql预编译
            String sql = "select id,name,email,birth from customers where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            preparedStatement.setObject(1, 1);
            //获取返回值
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);
                //data = new Object[]{id, name, email, birth};
                //将数据封装成对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer.toString());
            }
//            for (int i = 0; i < data.length; i++) {
//                System.out.println(data[i]);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }
    }

    @Test
    public void test() {

        Customer customer = this.selectTableTest1("select id,name,email,birth from customers where id = ?", 2);
        System.out.println(customer);

    }

    public Customer selectTableTest1(String sql, Object... args) {
        Connection connection = null;
        java.sql.PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
//        Object[] data = null;
        try {
            //获取连接
            connection = JDBCUtils.getConnection();
            //sql预编译
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            //获取返回值
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            if (resultSet.next()) {
                Customer customer = new Customer();
                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    Object revalue = resultSet.getObject(i + 1);
                    String columnName = metaData.getColumnName(i + 1);
                    //反射
                    Field declaredField = Customer.class.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(customer, revalue);
                }

                return customer;
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
    public void testCommonUpdate() {
//           String sql = "update customers set name = ? where id = ?";
//            update(sql,"哪吒",19);
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql, "DD", 2);
    }

    //通用的增删改操作
    public void update(String sql, Object... args) {
        Connection connection = null;
        java.sql.PreparedStatement preparedStatement = null;
        try {
            //获取连接
            connection = JDBCUtils.getConnection();
            //预编译sql
            preparedStatement = connection.prepareStatement(sql);
            //设置
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行sql
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }

    //修改表中数据
    @Test
    public void upDataTest() {
        Connection connection = null;
        java.sql.PreparedStatement preparedStatement = null;
        try {
            //获取数据库连接
            connection = JDBCUtils.getConnection();
            //预编译sql语句，返回prepareStatement
            String sql = "update customers set name = ? where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            preparedStatement.setObject(1, "莫扎特");
            preparedStatement.setObject(2, 18);
            //执行
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }

    //向表中插入一条数据
    @Test
    public void testInsert() {
        //获取配置信息
        Properties properties = new Properties();
        java.sql.PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            InputStream reStream = PreparedStatement.class.getClassLoader().getResourceAsStream("jdbc.properties");
            properties.load(reStream);
            //读取配置
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");
            //加载驱动
            Class.forName(driverClass);
            //获取连接
            connection = DriverManager.getConnection(url, user, password);

            //System.out.println(connection);
            //sql语句
            String sql = "insert into customers(name,email,birth)values(?,?,?)";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "哪吒");
            preparedStatement.setString(2, "nezha@gmail.com");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parse = simpleDateFormat.parse("1000-01-01");
            preparedStatement.setDate(3, new Date(parse.getTime()));
            //执行sql
            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @Test
    public void testOrder() {
//        //获取连接
//        Connection connection = null;
//        java.sql.PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        try {
//            connection = JDBCUtils.getConnection();
//            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setObject(1, 1);
//            resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                int order_id = resultSet.getInt("order_id");
//                String order_name = resultSet.getString("order_name");
//                Date order_date = resultSet.getDate("order_date");
//
//                Order order = new Order(order_name, order_id, order_date);
//                System.out.println(order);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
//        }
        String sql= "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = OrderMethod(sql,1);
        System.out.println(order);
    }

    //针对于order的通用
    public Order OrderMethod(String sql, Object... args) {
        Connection connection = null;
        java.sql.PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;//获取结果集
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            //填充可变个数的占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行sql
            resultSet = preparedStatement.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {//判断是否有数据
                //创建order对象
                Order od = new Order();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);
//                    //获取列的列名
//                    String columnName = metaData.getColumnName(i + 1);
                   //获取列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    //通过反射给对象属性赋值
                    Field declaredField = Order.class.getDeclaredField(columnLabel);
                    //设置为可编辑
                    declaredField.setAccessible(true);
                    declaredField.set(od, columnValue);
                }
                return od;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }
        return null;
    }
}
