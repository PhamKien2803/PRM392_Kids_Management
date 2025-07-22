package com.example.se1829_prm392_kindergartenms.Entity;

public class Feedback {
    private String feedbackId;
    private String teacherId;
    private String studentId;
    private String classId;
    private String content;
    private String date;

    public Feedback() {}

    public Feedback(String feedbackId, String teacherId, String studentId, String classId, String content, String date) {
        this.feedbackId = feedbackId;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.classId = classId;
        this.content = content;
        this.date = date;
    }

    public String getFeedbackId() { return feedbackId; }
    public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
