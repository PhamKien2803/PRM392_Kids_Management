package com.example.project_prm392_kidmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm392_kidmanagement.DB.SchedulesToClassDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.SchedulesToClass;
import com.example.project_prm392_kidmanagement.Mapper.ScheduleToClassMapper;

import java.util.ArrayList;
import java.util.List;

public class ScheduleToClassDao {
    private final SchedulesToClassDatabaseHelper dbHelper;

    public ScheduleToClassDao(Context context) {
        dbHelper = new SchedulesToClassDatabaseHelper(context);
    }

    public long insert(SchedulesToClass obj) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scheduleClassID", obj.getScheduleClassID());
        values.put("scheduleId", obj.getScheduleId());
        values.put("classId", obj.getClassId());

        return db.insert(SchedulesToClassDatabaseHelper.TABLE_SCHEDULES_TO_CLASS, null, values);
    }

    public boolean update(SchedulesToClass obj) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scheduleId", obj.getScheduleId());
        values.put("classId", obj.getClassId());

        int rows = db.update(
                SchedulesToClassDatabaseHelper.TABLE_SCHEDULES_TO_CLASS,
                values,
                "scheduleClassID = ?",
                new String[]{String.valueOf(obj.getScheduleClassID())}
        );
        return rows > 0;
    }

    public boolean delete(int scheduleClassID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(
                SchedulesToClassDatabaseHelper.TABLE_SCHEDULES_TO_CLASS,
                "scheduleClassID = ?",
                new String[]{String.valueOf(scheduleClassID)}
        );
        return rows > 0;
    }

    public SchedulesToClass getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SchedulesToClassDatabaseHelper.TABLE_SCHEDULES_TO_CLASS,
                null,
                "scheduleClassID = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        SchedulesToClass result = null;
        if (cursor.moveToFirst()) {
            result = ScheduleToClassMapper.fromCursor(cursor);
        }
        cursor.close();
        return result;
    }

    public List<SchedulesToClass> getAll() {
        List<SchedulesToClass> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SchedulesToClassDatabaseHelper.TABLE_SCHEDULES_TO_CLASS,
                null,
                null,
                null,
                null,
                null,
                "scheduleId ASC"
        );

        while (cursor.moveToNext()) {
            SchedulesToClass obj = ScheduleToClassMapper.fromCursor(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }
}
