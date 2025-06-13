package com.example.project_prm392_kidmanagement.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Kid_database";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENT = "students";
    public static final String COLUMN_STUDENT_ID = "studentId";
    public static final String COLUMN_PARENT_ID = "parentId";
    public static final String COLUMN_FULL_NAME = "fullName";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_CLASS_ID = "classId";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + TABLE_STUDENT + " (" +
                    COLUMN_STUDENT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_PARENT_ID + " TEXT, " +
                    COLUMN_FULL_NAME + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_DOB + " TEXT, " +
                    COLUMN_CLASS_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_PARENT_ID + ") REFERENCES parents(parentId), " +
                    "FOREIGN KEY (" + COLUMN_CLASS_ID + ") REFERENCES classes(classId)" +
                    ");";

    public StudentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }
}
