package com.example.se1829_prm392_kindergartenms.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.ParentDao;
import com.example.se1829_prm392_kindergartenms.Entity.Class;
import com.example.se1829_prm392_kindergartenms.Entity.Parent;
import com.example.se1829_prm392_kindergartenms.Entity.Student;

public class StudentMapper {

    public static Student fromCursor(Cursor cursor, Context context) {
        String studentId = getString(cursor, "studentId");
        String fullName = getString(cursor, "fullName");
        String address = getString(cursor, "address");
        String dob = getString(cursor, "dob");
        String parentId = getString(cursor, "parentId");
        String classId = getString(cursor, "classId");

        Parent parent = fetchParent(context, parentId);
        Class classroom = fetchClass(context, classId);

        return new Student(classroom, dob, address, fullName, parent, studentId);
    }

    private static String getString(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        return index != -1 ? cursor.getString(index) : null;
    }

    private static Parent fetchParent(Context context, String parentId) {
        if (parentId == null) return null;
        ParentDao parentDao = new ParentDao(context);
        return parentDao.getById(parentId);
    }

    private static Class fetchClass(Context context, String classId) {
        if (classId == null) return null;
        ClassDao classDao = new ClassDao(context);
        return classDao.getById(classId);
    }
}
