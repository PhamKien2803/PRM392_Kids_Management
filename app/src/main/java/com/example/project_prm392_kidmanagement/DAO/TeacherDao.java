package com.example.project_prm392_kidmanagement.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm392_kidmanagement.DB.TeacherDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Teacher;

public class TeacherDao {
    private final SQLiteDatabase db;

    public TeacherDao(Context context) {
        TeacherDatabaseHelper helper = new TeacherDatabaseHelper(context);
        db = helper.getReadableDatabase();
    }

    public Teacher getById(String id) {
        Cursor cursor = db.query("teachers", null, "teacherId = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            Teacher teacher = new Teacher(
                    cursor.getString(cursor.getColumnIndexOrThrow("teacherId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("address")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dob"))
            );
            cursor.close();
            return teacher;
        }
        cursor.close();
        return null;
    }
}

