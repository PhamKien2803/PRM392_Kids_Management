package com.example.se1829_prm392_kindergartenms.Mapper;

import android.database.Cursor;

import com.example.se1829_prm392_kindergartenms.Entity.Feedback;

public class FeedbackMapper {

    public static Feedback fromCursor(Cursor cursor) {
        Feedback feedback = new Feedback();

        int indexFeedbackId = cursor.getColumnIndex("feedbackId");
        int indexTeacherId = cursor.getColumnIndex("teacherId");
        int indexStudentId = cursor.getColumnIndex("studentId");
        int indexClassId = cursor.getColumnIndex("classId");
        int indexContent = cursor.getColumnIndex("content");
        int indexDate = cursor.getColumnIndex("date");

        if (indexFeedbackId != -1) {
            feedback.setFeedbackId(cursor.getString(indexFeedbackId));
        }
        if (indexTeacherId != -1) {
            feedback.setTeacherId(cursor.getString(indexTeacherId));
        }
        if (indexStudentId != -1) {
            feedback.setStudentId(cursor.getString(indexStudentId));
        }
        if (indexClassId != -1) {
            feedback.setClassId(cursor.getString(indexClassId));
        }
        if (indexContent != -1) {
            feedback.setContent(cursor.getString(indexContent));
        }
        if (indexDate != -1) {
            feedback.setDate(cursor.getString(indexDate));
        }

        return feedback;
    }
}
