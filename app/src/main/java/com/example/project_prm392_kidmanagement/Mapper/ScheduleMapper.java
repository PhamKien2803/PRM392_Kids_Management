package com.example.project_prm392_kidmanagement.Mapper;

import android.database.Cursor;
import com.example.project_prm392_kidmanagement.Entity.Schedule;

public class ScheduleMapper {
    public static Schedule fromCursor(Cursor cursor) {
        return new Schedule(
                cursor.getString(cursor.getColumnIndexOrThrow("scheduleId")),
                cursor.getString(cursor.getColumnIndexOrThrow("activityName")),
                cursor.getString(cursor.getColumnIndexOrThrow("timeStart")),
                cursor.getString(cursor.getColumnIndexOrThrow("timeEnd")),
                cursor.getString(cursor.getColumnIndexOrThrow("timeDate"))
        );
    }
}
