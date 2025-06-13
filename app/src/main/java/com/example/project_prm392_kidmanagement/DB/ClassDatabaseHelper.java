package com.example.project_prm392_kidmanagement.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Kid_database";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CLASS = "classes";
    public static final String COLUMN_CLASS_ID = "classId";
    public static final String COLUMN_CLASS_NAME = "className";
    public static final String COLUMN_SCHOOL_YEAR = "schoolYear";
    public static final String COLUMN_TEACHER_ID = "teacherId";
    public static final String COLUMN_SCHEDULE_ID = "scheduleId";

    private static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + TABLE_CLASS + " (" +
                    COLUMN_CLASS_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_CLASS_NAME + " TEXT, " +
                    COLUMN_SCHOOL_YEAR + " TEXT, " +
                    COLUMN_TEACHER_ID + " TEXT, " +
                    COLUMN_SCHEDULE_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_TEACHER_ID + ") REFERENCES teachers(teacherId), " +
                    "FOREIGN KEY (" + COLUMN_SCHEDULE_ID + ") REFERENCES schedules(scheduleId)" +
                    ");";

    public ClassDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        onCreate(db);
    }
}
