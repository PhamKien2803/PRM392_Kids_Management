package com.example.project_prm392_kidmanagement.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.project_prm392_kidmanagement.DAO.ParentDao;
import com.example.project_prm392_kidmanagement.DAO.TeacherDao;
import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Account;
import com.example.project_prm392_kidmanagement.Entity.Parent;
import com.example.project_prm392_kidmanagement.Entity.Teacher;

public class AccountMapper {

    public static Account fromCursor (Cursor cursor, Context context) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_ACCOUNT_ID));
        String username = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_USERNAME));
        String password = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_PASSWORD));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_EMAIL));
//        boolean role = cursor.getInt(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_ROLE)) == 1;
        int role =  cursor.getInt(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_ROLE));

        String teacherIdStr = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_TEACHER_ID));
        String parentIdStr = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_PARENT_ID));

        TeacherDao teacherDao = new TeacherDao(context);
        ParentDao parentDao = new ParentDao(context);

        Teacher teacher = teacherIdStr != null ? teacherDao.getById(teacherIdStr) : null;
        Parent parent = parentIdStr != null ? parentDao.getById(parentIdStr) : null;

        return new Account(id, username, password, email, role, teacher, parent);
    }
}
