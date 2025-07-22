package com.example.se1829_prm392_kindergartenms.Mapper;


import android.database.Cursor;

import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

public class TeacherMapper {
    public static Teacher fromCursor(Cursor cursor) {
        return new Teacher(
                cursor.getString(cursor.getColumnIndexOrThrow("teacherId")),
                cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                cursor.getString(cursor.getColumnIndexOrThrow("address")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                cursor.getString(cursor.getColumnIndexOrThrow("dob"))
        );
    }
}
