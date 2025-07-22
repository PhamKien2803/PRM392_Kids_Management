package com.example.se1829_prm392_kindergartenms.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;
import com.example.se1829_prm392_kindergartenms.Entity.Feedback;
import com.example.se1829_prm392_kindergartenms.Mapper.FeedbackMapper;

import java.util.ArrayList;
import java.util.List;

public class FeedbackDao {
    private final SqlDatabaseHelper dbHelper;

    public FeedbackDao(Context context) {
        dbHelper = new SqlDatabaseHelper(context);
    }

    // Thêm feedback mới
    public boolean insertFeedback(Feedback feedback) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("feedbackId", feedback.getFeedbackId());
        values.put("teacherId", feedback.getTeacherId());
        values.put("studentId", feedback.getStudentId());
        values.put("classId", feedback.getClassId());
        values.put("content", feedback.getContent());
        values.put("date", feedback.getDate());

        long result = db.insert("feedback", null, values);
        db.close();
        return result != -1;
    }

    // Lấy tất cả feedback của 1 học sinh
    public List<Feedback> getFeedbackByStudent(String studentId) {
        List<Feedback> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM feedback WHERE studentId = ?", new String[]{studentId});
        if (cursor.moveToFirst()) {
            do {
                Feedback feedback = FeedbackMapper.fromCursor(cursor); // sử dụng Mapper
                list.add(feedback);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Lấy feedback theo class + date (cho giáo viên nhập feedback hôm nay)
    public List<Feedback> getFeedbackByClassAndDate(String classId, String date) {
        List<Feedback> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM feedback WHERE classId = ? AND date = ?",
                new String[]{classId, date}
        );
        if (cursor.moveToFirst()) {
            do {
                Feedback feedback = FeedbackMapper.fromCursor(cursor); // sử dụng Mapper
                list.add(feedback);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Lấy feedback cho một học sinh cụ thể vào một ngày cụ thể (mới thêm)
    public Feedback getFeedbackByStudentIdAndDate(String studentId, String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        Feedback feedback = null;
        try {
            cursor = db.rawQuery("SELECT * FROM feedback WHERE studentId = ? AND date = ?", new String[]{studentId, date});
            if (cursor.moveToFirst()) {
                feedback = FeedbackMapper.fromCursor(cursor); // Sử dụng Mapper để tạo đối tượng Feedback
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close(); // Đảm bảo đóng kết nối database
        }
        return feedback;
    }

    // Cập nhật nội dung feedback (mới thêm)
    public boolean updateFeedback(String feedbackId, String newContent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", newContent); // Chỉ cập nhật nội dung

        int rowsAffected = 0;
        try {
            rowsAffected = db.update("feedback", values, "feedbackId = ?", new String[]{feedbackId});
        } finally {
            db.close(); // Đảm bảo đóng kết nối database
        }
        return rowsAffected > 0;
    }
}