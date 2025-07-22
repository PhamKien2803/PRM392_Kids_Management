package com.example.se1829_prm392_kindergartenms.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.StudentDao;
import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;
import com.example.se1829_prm392_kindergartenms.Entity.Class;
import com.example.se1829_prm392_kindergartenms.Entity.Student;
import com.example.se1829_prm392_kindergartenms.Entity.StudentToClass;

public class StudentToClassMapper {

    public static StudentToClass fromCursor(Cursor cursor, Context context) {
        String studentClassId = getColumnString(cursor, "studentClassID");
        String studentId = getColumnString(cursor, SqlDatabaseHelper.COLUMN_STUDENT_ID);
        String classId = getColumnString(cursor, SqlDatabaseHelper.COLUMN_CLASS_ID);

        Student student = fetchStudent(context, studentId);
        Class classroom = fetchClass(context, classId);

        return new StudentToClass(studentClassId, student, classroom);
    }

    private static String getColumnString(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        return index != -1 ? cursor.getString(index) : null;
    }

    private static Student fetchStudent(Context context, String studentId) {
        if (studentId == null) return null;
        return new StudentDao(context).getById(studentId);
    }

    private static Class fetchClass(Context context, String classId) {
        if (classId == null) return null;
        return new ClassDao(context).getById(classId);
    }
}
