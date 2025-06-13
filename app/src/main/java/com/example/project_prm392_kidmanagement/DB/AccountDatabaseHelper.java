package com.example.project_prm392_kidmanagement.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Kid_database";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_ACCOUNT = "accounts";
    public static final String COLUMN_ACCOUNT_ID = "accountId";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_TEACHER_ID = "teacherId";
    public static final String COLUMN_PARENT_ID = "parentId";

    private static final String CREATE_ACCOUNT_TABLE =
            "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                    COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_ROLE + " INTEGER, " +
                    COLUMN_TEACHER_ID + " TEXT, " +
                    COLUMN_PARENT_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_TEACHER_ID + ") REFERENCES teachers(teacherId), " +
                    "FOREIGN KEY (" + COLUMN_PARENT_ID + ") REFERENCES parents(parentId)" +
                    ");";

    public AccountDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }
}
