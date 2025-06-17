package com.example.project_prm392_kidmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Teacher;
import com.example.project_prm392_kidmanagement.Mapper.TeacherMapper;

import java.util.ArrayList;
import java.util.List;

public class TeacherDao {
    private final SqlDatabaseHelper dbHelper;

    public TeacherDao(Context context) {
        dbHelper = new SqlDatabaseHelper(context);
    }

    public long insert(Teacher teacher) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("teacherId", teacher.getTeacherId());
        values.put("fullName", teacher.getFullName());
        values.put("address", teacher.getAddress());
        values.put("phone", teacher.getPhone());
        values.put("dob", teacher.getDob());

        return db.insert(SqlDatabaseHelper.TABLE_TEACHER, null, values);
    }

    public boolean update(Teacher teacher) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullName", teacher.getFullName());
        values.put("address", teacher.getAddress());
        values.put("phone", teacher.getPhone());
        values.put("dob", teacher.getDob());

        int rowsAffected = db.update(
                SqlDatabaseHelper.TABLE_TEACHER,
                values,
                "teacherId = ?",
                new String[]{teacher.getTeacherId()}
        );

        return rowsAffected > 0;
    }

    public boolean delete(String teacherId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(
                SqlDatabaseHelper.TABLE_TEACHER,
                "teacherId = ?",
                new String[]{teacherId}
        );
        return rowsDeleted > 0;
    }

    public Teacher getById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_TEACHER,
                null,
                "teacherId = ?",
                new String[]{id},
                null,
                null,
                null
        );

        Teacher teacher = null;
        if (cursor.moveToFirst()) {
            teacher = TeacherMapper.fromCursor(cursor);
        }

        cursor.close();
        return teacher;
    }

    public List<Teacher> getAll() {
        List<Teacher> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_TEACHER,
                null,
                null,
                null,
                null,
                null,
                "fullName ASC"
        );

        while (cursor.moveToNext()) {
            list.add(TeacherMapper.fromCursor(cursor));
        }

        cursor.close();
        return list;
    }
}
