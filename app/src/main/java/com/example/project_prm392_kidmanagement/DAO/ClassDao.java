package com.example.project_prm392_kidmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Class;
import com.example.project_prm392_kidmanagement.Mapper.ClassMapper;

import java.util.ArrayList;
import java.util.List;

public class ClassDao {
    private final SqlDatabaseHelper dbHelper;
    private final Context context;

    public ClassDao(Context context) {
        this.context = context;
        this.dbHelper = new SqlDatabaseHelper(context);
    }

    public long insert(Class classroom) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("classId", classroom.getClassId());
        values.put("className", classroom.getClassName());
        values.put("schoolYear", classroom.getSchoolYear());
        values.put("teacherId", classroom.getTeacherId() != null ? classroom.getTeacherId().getTeacherId() : null);
        values.put("scheduleId", classroom.getScheduleId() != null ? classroom.getScheduleId().getScheduleId() : null);

        return db.insert(SqlDatabaseHelper.TABLE_CLASS, null, values);
    }

    public boolean update(Class classroom) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("className", classroom.getClassName());
        values.put("schoolYear", classroom.getSchoolYear());
        values.put("teacherId", classroom.getTeacherId() != null ? classroom.getTeacherId().getTeacherId() : null);
        values.put("scheduleId", classroom.getScheduleId() != null ? classroom.getScheduleId().getScheduleId() : null);

        int rowsAffected = db.update(
                SqlDatabaseHelper.TABLE_CLASS,
                values,
                "classId = ?",
                new String[]{classroom.getClassId()}
        );
        return rowsAffected > 0;
    }

    public boolean delete(String classId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(
                SqlDatabaseHelper.TABLE_CLASS,
                "classId = ?",
                new String[]{classId}
        );
        return rows > 0;
    }

    public Class getById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_CLASS,
                null,
                "classId = ?",
                new String[]{id},
                null,
                null,
                null
        );

        Class classroom = null;
        if (cursor.moveToFirst()) {
            classroom = ClassMapper.fromCursor(cursor, context);
        }

        cursor.close();
        return classroom;
    }

    public List<Class> getAll() {
        List<Class> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_CLASS,
                null,
                null,
                null,
                null,
                null,
                "className ASC"
        );

        while (cursor.moveToNext()) {
            Class classroom = ClassMapper.fromCursor(cursor, context);
            list.add(classroom);
        }

        cursor.close();
        return list;
    }
}
