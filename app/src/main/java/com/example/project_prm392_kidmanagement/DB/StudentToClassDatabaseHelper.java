package com.example.project_prm392_kidmanagement.DB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentToClassDatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Kid_database";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENT_TO_CLASS = "studentToClass";

    public static final String COLUMN_STUDENT_TO_CLASS_ID = "studentClassID";
    public static final String COLUMN_STUDENT_ID = "studentId";
    public static final String COLUMN_CLASS_ID = "classId";

    private static final String CREATE_STUDENT_TO_CLASS_TABLE =
            "CREATE TABLE " + TABLE_STUDENT_TO_CLASS + " (" +
                    COLUMN_STUDENT_TO_CLASS_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_STUDENT_ID + " TEXT , " +
                    COLUMN_CLASS_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_STUDENT_ID + ") REFERENCES students(studentId), " +
                    "FOREIGN KEY (" + COLUMN_CLASS_ID + ") REFERENCES classes(classId)" +
                    ");";

    public StudentToClassDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TO_CLASS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_TO_CLASS);
        onCreate(db);
    }
}
