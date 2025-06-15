package com.example.project_prm392_kidmanagement.Mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.project_prm392_kidmanagement.DAO.TeacherDao;
import com.example.project_prm392_kidmanagement.DAO.ScheduleDao;
import com.example.project_prm392_kidmanagement.Entity.Class;
import com.example.project_prm392_kidmanagement.Entity.Teacher;
import com.example.project_prm392_kidmanagement.Entity.Schedule;

public class ClassMapper {

    public static Class fromCursor(Cursor cursor, Context context) {
        String classId = cursor.getString(cursor.getColumnIndexOrThrow("classId"));
        String className = cursor.getString(cursor.getColumnIndexOrThrow("className"));
        String schoolYear = cursor.getString(cursor.getColumnIndexOrThrow("schoolYear"));
        String teacherIdStr = cursor.getString(cursor.getColumnIndexOrThrow("teacherId"));
        String scheduleIdStr = cursor.getString(cursor.getColumnIndexOrThrow("scheduleId"));

        TeacherDao teacherDao = new TeacherDao(context);
        Teacher teacher = teacherIdStr != null ? teacherDao.getById(teacherIdStr) : null;

        ScheduleDao scheduleDao = new ScheduleDao(context);
        Schedule schedule = scheduleIdStr != null ? scheduleDao.getById(scheduleIdStr) : null;

        return new Class(classId, className, schoolYear, teacher, schedule);
    }
}
