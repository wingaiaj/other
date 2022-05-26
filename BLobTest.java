package com.zx.exer;

import com.zx.jdbcutils.JDBCUtils;
import com.zx.preparedstatement.Customer;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * @ClassName BLobTest
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/26 8:31
 * @Version 1.0
 */
public class BLobTest {
    //向customers表中插入blob字段
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        FileInputStream fileInputStream = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "insert into customers (name,email,birth,photo) values (?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, "Apex");
            preparedStatement.setObject(2, "Apex@126.com");
            preparedStatement.setObject(3, "1900-01-01");
            fileInputStream = new FileInputStream(new File("C:\\Users\\xpower\\Desktop\\jdbcTest\\Apex Legends 2022_1_10 0_34_02.png"));

            preparedStatement.setBlob(4, fileInputStream);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }

    @Test
    public void testSelect() {
        InputStream binaryStream = null;
        FileOutputStream fileOutputStream = null;
        //获取数据库连接
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            //预编译sql
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            preparedStatement.setObject(1, 25);
            //执行sql返回结果集
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {//下一个有数据
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Date birth = resultSet.getDate("birth");
                String email = resultSet.getString("email");
                Customer customer = new Customer(id, name, email, birth);
                Blob photo = resultSet.getBlob("photo");
                //获取photo流
                binaryStream = photo.getBinaryStream();
                fileOutputStream = new FileOutputStream(new File("C:\\Users\\xpower\\Desktop\\jdbcTest\\7FFF5CE496213BB6BAC6948002D62234620.jpg"));
                int len = 0;
                byte[] by = new byte[1024];
                while ((len = binaryStream.read(by)) != -1)
                    fileOutputStream.write(by, 0, len);

                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (binaryStream != null)
                    binaryStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);

        }
    }

    @Test
    public void testInsertMore() {
















//        //获取连接
//        Connection connection = null;
//        Statement statement = null;
//        try {
//            connection = JDBCUtils.getConnection();
//            //预编译sql语句
//            statement = connection.createStatement();
//            for (int i = 0; i < 20000; i++) {
//                String sql = "insert into goods (name) value('name_" + i + "') ";
//                statement.execute(sql);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            //关闭
//            JDBCUtils.closeResource(connection, statement);
//        }
    }
}
