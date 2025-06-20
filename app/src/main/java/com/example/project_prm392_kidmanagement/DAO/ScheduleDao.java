package com.example.project_prm392_kidmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Schedule;
import com.example.project_prm392_kidmanagement.Mapper.ScheduleMapper;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDao {
    private final SqlDatabaseHelper dbHelper;

    public ScheduleDao(Context context) {
        dbHelper = new SqlDatabaseHelper(context);
    }

    public long insert(Schedule schedule) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scheduleId", schedule.getScheduleId());
        values.put("activityName", schedule.getActivityName());
        values.put("timeStart", schedule.getTimeStart());
        values.put("timeEnd", schedule.getTimeEnd());
        values.put("timeDate", schedule.getTimeDate());

        return db.insert(SqlDatabaseHelper.TABLE_SCHEDULE, null, values);
    }

    public boolean update(Schedule schedule) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("activityName", schedule.getActivityName());
        values.put("timeStart", schedule.getTimeStart());
        values.put("timeEnd", schedule.getTimeEnd());
        values.put("timeDate", schedule.getTimeDate());

        int rowsAffected = db.update(
                SqlDatabaseHelper.TABLE_SCHEDULE,
                values,
                "scheduleId = ?",
                new String[]{schedule.getScheduleId()}
        );

        return rowsAffected > 0;
    }

    public boolean delete(String scheduleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(
                SqlDatabaseHelper.TABLE_SCHEDULE,
                "scheduleId = ?",
                new String[]{scheduleId}
        );
        return rows > 0;
    }

    public Schedule getById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_SCHEDULE,
                null,
                "scheduleId = ?",
                new String[]{id},
                null,
                null,
                null
        );

        Schedule schedule = null;
        if (cursor.moveToFirst()) {
            schedule = ScheduleMapper.fromCursor(cursor);
        }

        cursor.close();
        return schedule;
    }

    public List<Schedule> getAll() {
        List<Schedule> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_SCHEDULE,
                null,
                null,
                null,
                null,
                null,
                "timeDate ASC, timeStart ASC"
        );

        while (cursor.moveToNext()) {
            Schedule schedule = ScheduleMapper.fromCursor(cursor);
            list.add(schedule);
        }

        cursor.close();
        return list;
    }

    public List<Schedule> getSchedulesByClassId(String classId) {
        List<Schedule> schedules = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.* FROM schedules s " +
                "JOIN schedulesToClass stc ON s.scheduleId = stc.scheduleId " +
                "WHERE stc.classId = ?";

        Cursor cursor = db.rawQuery(query, new String[]{classId});

        while (cursor.moveToNext()) {
            Schedule schedule = ScheduleMapper.fromCursor(cursor);
            schedules.add(schedule);
        }

        cursor.close();
        return schedules;
    }



}
