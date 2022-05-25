package com.zx.connection;

/**
 * @ClassName examStudent
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/25 9:21
 * @Version 1.0
 */
public class examStudent {
    int FlowID;
    int Type;
    String IDCard;
    String ExamCard;
    String StudentName;
    String Location;
    int Grade;

    public examStudent() {
    }

    public examStudent(int flowID, int type, String IDCard, String examCard, String studentName, String location, int grade) {
        FlowID = flowID;
        Type = type;
        this.IDCard = IDCard;
        ExamCard = examCard;
        StudentName = studentName;
        Location = location;
        Grade = grade;
    }

    public int getFlowID() {
        return FlowID;
    }

    public void setFlowID(int flowID) {
        FlowID = flowID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return ExamCard;
    }

    public void setExamCard(String examCard) {
        ExamCard = examCard;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int grade) {
        Grade = grade;
    }

    @Override
    public String toString() {
        return "=========查询结果=========\n" +
                "流水号:" + FlowID +"\n"+
                "四/六级:" + Type +"\n"+
                " 身份证号='" + IDCard + "\n"+
                " 准考证号='" + ExamCard + "\n"+
                " 学生姓名='" + StudentName + "\n" +
                " 区域='" + Location + "\n" +
                " 成绩=" + Grade +
                '}';
    }
}
