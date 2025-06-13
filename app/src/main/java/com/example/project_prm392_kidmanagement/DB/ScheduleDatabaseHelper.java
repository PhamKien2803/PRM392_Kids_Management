package com.example.project_prm392_kidmanagement.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Kid_database";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_SCHEDULE = "schedules";
    public static final String COLUMN_SCHEDULE_ID = "scheduleId";
    public static final String COLUMN_ACTIVITY_NAME = "activityName";
    public static final String COLUMN_TIME_START = "timeStart";
    public static final String COLUMN_TIME_END = "timeEnd";
    public static final String COLUMN_TIME_DATE = "timeDate";

    private static final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE " + TABLE_SCHEDULE + " (" +
                    COLUMN_SCHEDULE_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_ACTIVITY_NAME + " TEXT, " +
                    COLUMN_TIME_START + " TEXT, " +
                    COLUMN_TIME_END + " TEXT, " +
                    COLUMN_TIME_DATE + " TEXT" +
                    ");";

    public ScheduleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        onCreate(db);
    }
}
