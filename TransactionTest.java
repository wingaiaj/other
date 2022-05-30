package com.zx.transaction;

import com.zx.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @ClassName TransactionTest
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/30 15:13
 * @Version 1.0
 */
/*
 * 1.什么叫数据库事务？
 * 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
 * 		> 一组逻辑操作单元：一个或多个DML操作。
 *
 * 2.事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存
 * 下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *
 * 3.数据一旦提交，就不可回滚
 *
 * 4.哪些操作会导致数据的自动提交？
 * 		>DDL操作一旦执行，都会自动提交。
 * 			>set autocommit = false 对DDL操作失效
 * 		>DML默认情况下，一旦执行，就会自动提交。
 * 			>我们可以通过set autocommit = false的方式取消DML操作的自动提交。
 * 		>默认在关闭连接时，会自动的提交数据
 */
public class TransactionTest {
    /*
        针对于数据表user_table set balance = balance - 100 where user = 'AA';
        针对于数据表user_table set balance = balance + 100 where user = 'BB';
     */
    //
    @Test
    public void testUpdate() {
        String sql = "update user_table set balance = balance - 100 where user = ?";
        update(sql, "AA");

        //模拟网络异常
        //System.out.println(100/0);

        String sql1 = "update user_table set balance = balance + 100 where user = ?";
        update(sql1, "BB");
    }
    @Test
    public void testUpdateWithTx(){
        Connection connection = null;
        try {
            //获取连接
            connection = JDBCUtils.getConnection();

            System.out.println(connection.getAutoCommit());
            //设置自动提交为false
            connection.setAutoCommit(false);
            String sql = "update user_table set balance = balance - 100 where user = ?";
            update(connection,sql, "AA");

            //模拟网络异常
            System.out.println(100/0);

            String sql1 = "update user_table set balance = balance + 100 where user = ?";
            update(connection,sql1, "BB");

            System.out.println("转帐成功！");

            //提交数据
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
            //回滚操作
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
        JDBCUtils.closeResource(connection,null);
        }


    }

    //通用增删改操作
    public int update(String sql, Object... args) {
        //获取连接
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCUtils.getConnection();
            //预声明sql
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //返回执行行数
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement);
        }
        return 0;
    }
    //通用增删改操作 version2.0
//******************未考虑数据库事务情况下的转账操作**************************
    public int update(Connection connection,String sql, Object... args) {
        //获取连接
        PreparedStatement preparedStatement = null;
        try {
            //预声明sql
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //返回执行行数
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(null, preparedStatement);
        }
        return 0;
    }

}
