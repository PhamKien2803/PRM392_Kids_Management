package com.example.project_prm392_kidmanagement.Entity;

public class StudentToClass {
    private String studentClassID;
    private Student studentId;
    private Class classId;

    public StudentToClass() {
    }

    public StudentToClass(String studentClassID, Student studentId, Class classId) {
        this.studentClassID = studentClassID;
        this.studentId = studentId;
        this.classId = classId;
    }

    public String getStudentClassID() {
        return studentClassID;
    }

    public void setStudentClassID(String studentClassID) {
        this.studentClassID = studentClassID;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Class getClassId() {
        return classId;
    }

    public void setClassId(Class classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return "StudentToClass{" +
                "studentClassID=" + studentClassID +
                ", studentId=" + studentId +
                ", classId=" + classId +
                '}';
    }
}
