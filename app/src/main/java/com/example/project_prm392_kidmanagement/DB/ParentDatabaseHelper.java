package com.example.project_prm392_kidmanagement.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ParentDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Kid_database";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_PARENT = "parents";
    public static final String COLUMN_PARENT_ID = "parentId";
    public static final String COLUMN_FULL_NAME = "fullName";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_DOB = "dob";

    private static final String CREATE_PARENT_TABLE =
            "CREATE TABLE " + TABLE_PARENT + " (" +
                    COLUMN_PARENT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_FULL_NAME + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_DOB + " TEXT" +
                    ");";

    public ParentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PARENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENT);
        onCreate(db);
    }
}
