package com.example.project_prm392_kidmanagement.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.project_prm392_kidmanagement.DAO.ClassDao;
import com.example.project_prm392_kidmanagement.DAO.ParentDao;
import com.example.project_prm392_kidmanagement.Entity.Class;
import com.example.project_prm392_kidmanagement.Entity.Parent;
import com.example.project_prm392_kidmanagement.Entity.Student;

public class StudentMapper {
    public static Student fromCursor(Cursor cursor, Context context) {
        String studentId = cursor.getString(cursor.getColumnIndexOrThrow("studentId"));
        String fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
        String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
        String dob = cursor.getString(cursor.getColumnIndexOrThrow("dob"));

        String parentIdStr = cursor.getString(cursor.getColumnIndexOrThrow("parentId"));
        String classIdStr = cursor.getString(cursor.getColumnIndexOrThrow("classId"));

        Parent parent = null;
        Class classroom = null;

        if (parentIdStr != null) {
            ParentDao parentDao = new ParentDao(context);
            parent = parentDao.getById(parentIdStr);
        }

        if (classIdStr != null) {
            ClassDao classDao = new ClassDao(context);
            classroom = classDao.getById(classIdStr);
        }

        return new Student(classroom, dob, address, fullName, parent, studentId);
    }
}
