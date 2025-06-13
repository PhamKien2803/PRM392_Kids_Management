package com.example.project_prm392_kidmanagement.DB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchedulesToClassDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Kid_database";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_SCHEDULES_TO_CLASS = "schedulesToClass";

    public static final String COLUMN_SCHEDULES_TO_CLASS_ID = "scheduleClassID";
    public static final String COLUMN_SCHEDULE_ID = "scheduleId";
    public static final String COLUMN_CLASS_ID = "classId";

    private static final String CREATE_STUDENT_TO_CLASS_TABLE =
            "CREATE TABLE " + TABLE_SCHEDULES_TO_CLASS + " (" +
                    COLUMN_SCHEDULES_TO_CLASS_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_SCHEDULE_ID + " TEXT , " +
                    COLUMN_CLASS_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_SCHEDULE_ID + ") REFERENCES schedules(scheduleId), " +
                    "FOREIGN KEY (" + COLUMN_CLASS_ID + ") REFERENCES classes(classId)" +
                    ");";

    public SchedulesToClassDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_SCHEDULES_TO_CLASS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES_TO_CLASS);
        onCreate(db);
    }
}
