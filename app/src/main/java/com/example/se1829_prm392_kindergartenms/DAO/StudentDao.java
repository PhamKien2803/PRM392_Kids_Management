package com.example.se1829_prm392_kindergartenms.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;
import com.example.se1829_prm392_kindergartenms.Entity.Student;
import com.example.se1829_prm392_kindergartenms.Mapper.StudentMapper;

import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    private final SqlDatabaseHelper dbHelper;
    private final Context context;

    public StudentDao(Context context) {
        this.context = context;
        this.dbHelper = new SqlDatabaseHelper(context);
    }

    // Insert new student
    public long insert(Student student) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = buildStudentContentValues(student, true);
        return db.insert(SqlDatabaseHelper.TABLE_STUDENT, null, values);
    }

    // Update existing student
    public boolean update(Student student) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = buildStudentContentValues(student, false);

        int rowsAffected = db.update(
                SqlDatabaseHelper.TABLE_STUDENT,
                values,
                "studentId = ?",
                new String[]{student.getStudentId()}
        );
        return rowsAffected > 0;
    }

    // Delete student by ID
    public boolean delete(String studentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(
                SqlDatabaseHelper.TABLE_STUDENT,
                "studentId = ?",
                new String[]{studentId}
        );
        return rowsDeleted > 0;
    }

    // Get single student by ID
    public Student getById(String id) {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     SqlDatabaseHelper.TABLE_STUDENT,
                     null,
                     "studentId = ?",
                     new String[]{id},
                     null,
                     null,
                     null
             )) {

            if (cursor.moveToFirst()) {
                return StudentMapper.fromCursor(cursor, context);
            }
            return null;
        }
    }

    // Get all students
    public List<Student> getAll() {
        return getStudentsByQuery(null, null, "fullName ASC");
    }

    // Get students by parent ID
    public List<Student> getStudentsByParentId(String parentId) {
        return getStudentsByQuery("parentId = ?", new String[]{parentId}, "fullName ASC");
    }

    // Helper method: get list of students with conditions
    private List<Student> getStudentsByQuery(String selection, String[] selectionArgs, String orderBy) {
        List<Student> students = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     SqlDatabaseHelper.TABLE_STUDENT,
                     null,
                     selection,
                     selectionArgs,
                     null,
                     null,
                     orderBy
             )) {

            while (cursor.moveToNext()) {
                students.add(StudentMapper.fromCursor(cursor, context));
            }
        }

        return students;
    }

    // Helper method: build ContentValues from Student object
    private ContentValues buildStudentContentValues(Student student, boolean includeId) {
        ContentValues values = new ContentValues();
        if (includeId) {
            values.put("studentId", student.getStudentId());
        }
        values.put("fullName", student.getFullName());
        values.put("address", student.getAddress());
        values.put("dob", student.getDob());

        if (student.getParentId() != null) {
            values.put("parentId", student.getParentId().getParentId());
        } else {
            values.putNull("parentId");
        }

        if (student.getClassId() != null) {
            values.put("classId", student.getClassId().getClassId());
        } else {
            values.putNull("classId");
        }

        return values;
    }
}
