package com.example.se1829_prm392_kindergartenms.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.se1829_prm392_kindergartenms.DAO.ScheduleDao;
import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Schedule;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;
import com.example.se1829_prm392_kindergartenms.Entity.Class;

public class ClassMapper {

    /**
     * Convert a database Cursor into a Class entity.
     *
     * @param cursor  the Cursor containing class data
     * @param context Android context (used to access DAOs)
     * @return a populated Class object
     */
    public static Class fromCursor(Cursor cursor, Context context) {
        // Extract values from cursor
        String classId = cursor.getString(cursor.getColumnIndexOrThrow("classId"));
        String className = cursor.getString(cursor.getColumnIndexOrThrow("className"));
        String schoolYear = cursor.getString(cursor.getColumnIndexOrThrow("schoolYear"));
        String teacherIdStr = cursor.getString(cursor.getColumnIndexOrThrow("teacherId"));
        String scheduleIdStr = cursor.getString(cursor.getColumnIndexOrThrow("scheduleId"));

        // Create DAO instances
        TeacherDao teacherDao = new TeacherDao(context);
        ScheduleDao scheduleDao = new ScheduleDao(context);

        // Get associated Teacher and Schedule
        Teacher teacher = null;
        if (teacherIdStr != null) {
            teacher = teacherDao.getById(teacherIdStr);
        }

        Schedule schedule = null;
        if (scheduleIdStr != null) {
            schedule = scheduleDao.getById(scheduleIdStr);
        }

        // System.out.println("Mapped class: " + className + " (" + classId + ")");

        // Return the new Class object
        return new Class(classId, className, schoolYear, teacher, schedule);
    }

}
