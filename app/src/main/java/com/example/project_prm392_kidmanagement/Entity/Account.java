package com.example.project_prm392_kidmanagement.Entity;

public class Account {
    private int accountId;
    private String username;
    private String password;
    private String email;
    private int role;
    private Teacher teacherId;
    private Parent parentId;

    public Account() {
    }

    public Account(int accountId, String username, String password, String email, int role, Teacher teacherId, Parent parentId) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.teacherId = teacherId;
        this.parentId = parentId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int isRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Teacher getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Teacher teacherId) {
        this.teacherId = teacherId;
    }

    public Parent getParentId() {
        return parentId;
    }

    public void setParentId(Parent parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", teacherId=" + teacherId +
                ", parentId=" + parentId +
                '}';
    }
}
