package com.zx.connection;

import org.junit.Test;

import javax.sound.sampled.Line;
import java.io.InputStream;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @ClassName Connection
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/22 14:44
 * @Version 1.0
 */
public class Connection {
    //方式一
    @Test
    public void ConnectionTest() throws Exception {
        Driver driver = new com.mysql.cj.jdbc.Driver();
        String url = "jdbc:mysql://localhost:3306/testjava?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone = GMT";
        //将用户名和密码封装在Properties里
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "1009555200");
        java.sql.Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式二
    @Test
    public void ConnectionTest2() throws Exception {
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        String url = "jdbc:mysql://localhost:3306/testjava?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone = GMT";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "1009555200");
        java.sql.Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式三
    @Test
    public void ConnectionTest3() throws Exception {
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjava?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone = GMT", "root", "1009555200");
        System.out.println(connection);
    }

    //方式四
    @Test
    public void ConnectionTest4() throws Exception {
        //注册驱动
        //静态代码块 里注册了驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        //获取连接
        java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjava?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone = GMT", "root", "1009555200");
        System.out.println(connection);
    }

    //方式五(final) 将基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    @Test
    public void ConnectionTest5() throws Exception {

        //读取配置文件中的信息
        InputStream resourceAsStream = Connection.class.getClassLoader().getResourceAsStream("jdbc.properties");
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

        System.out.println(connection);
    }
}
