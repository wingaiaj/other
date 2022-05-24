package com.zx.jdbcutils;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @ClassName JDBCUtils
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/23 9:25
 * @Version 1.0
 */
public class JDBCUtils {
    public static Connection getConnection() throws Exception {
        //读取配置文件中的信息
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");
        //加载驱动
        Class.forName(driverClass);
        //获取连接
        java.sql.Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public  static void closeResource(Connection connection, Statement statement) {

        try {
            if (statement != null)
                statement.close();
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
  public  static void closeResource(Connection connection, Statement statement, ResultSet resultSet) {

        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

      try {
          if(resultSet != null)
          resultSet.close();
      } catch (SQLException e) {
          e.printStackTrace();
      }


  }


}
