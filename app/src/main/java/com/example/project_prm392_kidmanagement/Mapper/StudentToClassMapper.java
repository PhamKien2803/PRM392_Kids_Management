package com.example.project_prm392_kidmanagement.Mapper;

import android.database.Cursor;

import com.example.project_prm392_kidmanagement.Entity.StudentToClass;

public class StudentToClassMapper {
    public static StudentToClass fromCursor(Cursor cursor) {
        return new StudentToClass(
                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("studentClassID"))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("studentId"))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("classId")))
        );
    }
}
