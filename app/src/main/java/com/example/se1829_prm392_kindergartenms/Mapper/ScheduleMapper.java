package com.example.se1829_prm392_kindergartenms.Mapper;

import android.database.Cursor;

import com.example.se1829_prm392_kindergartenms.Entity.Schedule;

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
