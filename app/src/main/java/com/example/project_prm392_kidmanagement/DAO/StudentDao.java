package com.example.project_prm392_kidmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Student;
import com.example.project_prm392_kidmanagement.Mapper.StudentMapper;

import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    private final SqlDatabaseHelper dbHelper;
    private final Context context;

    public StudentDao(Context context) {
        this.context = context;
        this.dbHelper = new SqlDatabaseHelper(context);
    }

    public long insert(Student student) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("studentId", student.getStudentId());
        values.put("fullName", student.getFullName());
        values.put("address", student.getAddress());
        values.put("dob", student.getDob());
        values.put("parentId", student.getParentId() != null ? student.getParentId().getParentId() : null);
        values.put("classId", student.getClassId() != null ? student.getClassId().getClassId() : null);

        return db.insert(SqlDatabaseHelper.TABLE_STUDENT, null, values);
    }

    public boolean update(Student student) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullName", student.getFullName());
        values.put("address", student.getAddress());
        values.put("dob", student.getDob());
        values.put("parentId", student.getParentId() != null ? student.getParentId().getParentId() : null);
        values.put("classId", student.getClassId() != null ? student.getClassId().getClassId() : null);

        int rowsAffected = db.update(
                SqlDatabaseHelper.TABLE_STUDENT,
                values,
                "studentId = ?",
                new String[]{student.getStudentId()}
        );
        return rowsAffected > 0;
    }

    public boolean delete(String studentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(
                SqlDatabaseHelper.TABLE_STUDENT,
                "studentId = ?",
                new String[]{studentId}
        );
        return rows > 0;
    }

    public Student getById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_STUDENT,
                null,
                "studentId = ?",
                new String[]{id},
                null,
                null,
                null
        );

        Student student = null;
        if (cursor.moveToFirst()) {
            student = StudentMapper.fromCursor(cursor, context);
        }

        cursor.close();
        return student;
    }

    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_STUDENT,
                null,
                null,
                null,
                null,
                null,
                "fullName ASC"
        );

        while (cursor.moveToNext()) {
            Student student = StudentMapper.fromCursor(cursor, context);
            list.add(student);
        }

        cursor.close();
        return list;
    }

    public List<Student> getStudentsByParentId(String parentId) {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_STUDENT,
                null,
                "parentId = ?",
                new String[]{parentId},
                null,
                null,
                "fullName ASC"
        );

        while (cursor.moveToNext()) {
            Student student = StudentMapper.fromCursor(cursor, context);
            students.add(student);
        }

        cursor.close();
        return students;
    }

}
