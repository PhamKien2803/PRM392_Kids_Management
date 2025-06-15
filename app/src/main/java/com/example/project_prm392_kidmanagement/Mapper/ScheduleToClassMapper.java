package com.example.project_prm392_kidmanagement.Mapper;

import android.database.Cursor;
import com.example.project_prm392_kidmanagement.Entity.SchedulesToClass;

public class ScheduleToClassMapper {
    public static SchedulesToClass fromCursor(Cursor cursor) {
        return new SchedulesToClass(
                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("scheduleClassID"))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("scheduleId"))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("classId")))
        );
    }
}
