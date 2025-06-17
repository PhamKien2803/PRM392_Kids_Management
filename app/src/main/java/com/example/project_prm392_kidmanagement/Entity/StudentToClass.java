package com.example.project_prm392_kidmanagement.Entity;

public class StudentToClass {
    private int studentClassID;
    private int studentId;
    private int classId;

    public StudentToClass() {
    }

    public StudentToClass(int studentClassID, int studentId, int classId) {
        this.studentClassID = studentClassID;
        this.studentId = studentId;
        this.classId = classId;
    }

    public int getStudentClassID() {
        return studentClassID;
    }

    public void setStudentClassID(int studentClassID) {
        this.studentClassID = studentClassID;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
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
