package com.example.project_prm392_kidmanagement.Entity;

public class Parent {
    private String parentId;
    private String fullName;
    private String address;
    private String phone;
    private String dob;

    public Parent() {
    }

    public Parent(String parentId, String fullName, String address, String phone, String dob) {
        this.parentId = parentId;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
        return "Parent{" +
                "parentId='" + parentId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}
