package com.zx.exer;

import com.zx.connection.PreparedStatementQuery;
import com.zx.connection.examStudent;

import com.zx.jdbcutils.JDBCUtils;
import com.zx.preparedstatement.PreparedStatementT;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Scanner;

/**
 * @ClassName TestWork
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/25 8:37
 * @Version 1.0
 */
public class TestWork {
    //增删改
    public void workTest(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }

//    @Test
//    public void runTest() {
//        String sql = "INSERT INTO customers(`name`,email,birth) VALUES(?,?,?)";
//        this.workTest(sql, "zx2", "zx@1234", new Date(561564L));
//    }

    Scanner scanner = new Scanner(System.in);

    public void getStudentGrade() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        System.out.println("请选择要输入的类型:");
        System.out.println("a:准考证号");
        System.out.println("b:身份证号");
        String next = scanner.next();
        if ("a".equals(next)) {
            System.out.println("请输入准考证号");
            String examId = scanner.next();
            //sql查询语句中 不能*
            String sql = "SELECT FlowID,Type,IDCard,ExamCard,StudentName,Location,Grade FROM examstudent WHERE ExamCard = ?";
            try {
                examStudent instance = new PreparedStatementQuery().getInstance(examStudent.class, sql, examId);
                System.out.println(instance);

            } catch (Exception e) {
                System.out.println("查无此人,请重新进入程序");
            }
        } else if ("b".equals(next)) {
            System.out.println("请输入身份证号");
            String Id = scanner.next();
            String sql = "SELECT FlowID,Type,IDCard,ExamCard,StudentName,Location,Grade FROM examstudent WHERE IDCard = ?";
            try {
                examStudent instance = new PreparedStatementQuery().getInstance(examStudent.class, sql, Id);
                System.out.println(instance);
            } catch (Exception e) {
                System.out.println("查无此人,请重新进入程序");
            }
        } else {
            System.out.println("您输入有误!请重新进入程序。");
        }

    }

    public void deleteStudent() {

        System.out.println("请输入学生学号:");
        int stID = scanner.nextInt();
        String sql ="DELETE FROM examstudent WHERE FlowID = ?";
        try {
            new PreparedStatementT().update(sql,stID);
        } catch (Exception e) {
            System.out.println("查无此人");
        }
        System.out.println("删除成功");

    }

    public void add() {

        System.out.println("考试类型4/6:");
        int type = scanner.nextInt();
        System.out.println("身份证号：");
        String IdCard = scanner.next();
        System.out.println("考号：");
        String ExamCard = scanner.next();
        System.out.println("学生姓名：");
        String StudentName = scanner.next();
        System.out.println("地址");
        String Location  =scanner.next();
        System.out.println("考试成绩");
        int Grade  = scanner.nextInt();
    String sql = "insert into examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?)";

      new PreparedStatementT().update(sql,type,IdCard,ExamCard,StudentName,Location,Grade);
    }


    public static void main(String[] args) {
        TestWork testWork = new TestWork();

        System.out.println("查询1or删除2");
        int i = testWork.scanner.nextInt();
        if (i == 1) {
            try {
                testWork.getStudentGrade();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (i == 2) {
            testWork.deleteStudent();
        }
//            testWork.add();
    }
}
