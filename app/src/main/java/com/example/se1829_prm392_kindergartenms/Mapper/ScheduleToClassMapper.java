package com.example.se1829_prm392_kindergartenms.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.se1829_prm392_kindergartenms.Entity.Class;
import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.ScheduleDao;
import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;
import com.example.se1829_prm392_kindergartenms.Entity.Schedule;
import com.example.se1829_prm392_kindergartenms.Entity.SchedulesToClass;

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
