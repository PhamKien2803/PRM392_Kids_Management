package com.example.project_prm392_kidmanagement.Entity;

public class Teacher {
    private String teacherId;
    private String fullName;
    private String address;
    private String phone;
    private String dob;

    public Teacher() {
    }

    public Teacher(String teacherId, String fullName, String address, String phone, String dob) {
        this.teacherId = teacherId;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}
