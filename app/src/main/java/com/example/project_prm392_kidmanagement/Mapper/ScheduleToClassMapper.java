package com.example.project_prm392_kidmanagement.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.project_prm392_kidmanagement.DAO.ClassDao;
import com.example.project_prm392_kidmanagement.DAO.ScheduleDao;
import com.example.project_prm392_kidmanagement.DAO.StudentDao;
import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Class;
import com.example.project_prm392_kidmanagement.Entity.Schedule;
import com.example.project_prm392_kidmanagement.Entity.SchedulesToClass;
import com.example.project_prm392_kidmanagement.Entity.Student;
import com.example.project_prm392_kidmanagement.Entity.StudentToClass;

public class ScheduleToClassMapper {
    public static SchedulesToClass fromCursor(Cursor cursor, Context context) {
        String id = cursor.getString(cursor.getColumnIndexOrThrow("scheduleClassID"));
        String schedulesIdStr = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_SCHEDULE_ID));
        String classIdStr = cursor.getString(cursor.getColumnIndexOrThrow(SqlDatabaseHelper.COLUMN_CLASS_ID));

        ScheduleDao scheduleDao = new ScheduleDao(context);
        ClassDao classDao = new ClassDao(context);


        Schedule student = schedulesIdStr != null ? scheduleDao.getById(schedulesIdStr) : null;
        Class classID = classIdStr != null ? classDao.getById(classIdStr) : null;
        return new SchedulesToClass(id, student, classID);
    }
}
