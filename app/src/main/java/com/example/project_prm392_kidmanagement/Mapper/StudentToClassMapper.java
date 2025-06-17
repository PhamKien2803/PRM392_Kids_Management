package com.example.project_prm392_kidmanagement.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.project_prm392_kidmanagement.DAO.ClassDao;
import com.example.project_prm392_kidmanagement.DAO.ParentDao;
import com.example.project_prm392_kidmanagement.DAO.StudentDao;
import com.example.project_prm392_kidmanagement.DAO.TeacherDao;
import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Account;
import com.example.project_prm392_kidmanagement.Entity.Class;
import com.example.project_prm392_kidmanagement.Entity.Parent;
import com.example.project_prm392_kidmanagement.Entity.Student;
import com.example.project_prm392_kidmanagement.Entity.StudentToClass;
import com.example.project_prm392_kidmanagement.Entity.Teacher;

public class StudentToClassMapper {
    public static StudentToClass fromCursor (Cursor cursor, Context context) {
        String id = cursor.getString(cursor.getColumnIndexOrThrow("studentClassID"));
        String studentIdStr = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_STUDENT_ID));
        String classIdStr = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_CLASS_ID));

        StudentDao studentDao = new StudentDao(context);
        ClassDao classDao = new ClassDao(context);

        Student student = studentIdStr != null ? studentDao.getById(studentIdStr) : null;
        Class classID = classIdStr != null ? classDao.getById(classIdStr) : null;
        return new StudentToClass(id, student, classID);

    }
}
