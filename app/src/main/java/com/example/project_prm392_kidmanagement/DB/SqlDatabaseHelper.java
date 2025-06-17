package com.example.project_prm392_kidmanagement.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Kid_database";
    public static final int DATABASE_VERSION = 2;

    private final Context context;

    public SqlDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    // ------------------ TEACHERS ------------------
    public static final String TABLE_TEACHER = "teachers";
    public static final String COLUMN_TEACHER_ID = "teacherId";
    public static final String COLUMN_TEACHER_NAME = "fullName";
    public static final String COLUMN_TEACHER_ADDRESS = "address";
    public static final String COLUMN_TEACHER_PHONE = "phone";
    public static final String COLUMN_TEACHER_DOB = "dob";

    private static final String CREATE_TEACHER_TABLE =
            "CREATE TABLE " + TABLE_TEACHER + " (" +
                    COLUMN_TEACHER_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_TEACHER_NAME + " TEXT, " +
                    COLUMN_TEACHER_ADDRESS + " TEXT, " +
                    COLUMN_TEACHER_PHONE + " TEXT, " +
                    COLUMN_TEACHER_DOB + " TEXT);";

    // ------------------ PARENTS ------------------
    public static final String TABLE_PARENT = "parents";
    public static final String COLUMN_PARENT_ID = "parentId";
    public static final String COLUMN_PARENT_NAME = "fullName";
    public static final String COLUMN_PARENT_ADDRESS = "address";
    public static final String COLUMN_PARENT_PHONE = "phone";
    public static final String COLUMN_PARENT_DOB = "dob";

    private static final String CREATE_PARENT_TABLE =
            "CREATE TABLE " + TABLE_PARENT + " (" +
                    COLUMN_PARENT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_PARENT_NAME + " TEXT, " +
                    COLUMN_PARENT_ADDRESS + " TEXT, " +
                    COLUMN_PARENT_PHONE + " TEXT, " +
                    COLUMN_PARENT_DOB + " TEXT);";

    // ------------------ SCHEDULES ------------------
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
                    COLUMN_TIME_DATE + " TEXT);";

    // ------------------ CLASSES ------------------
    public static final String TABLE_CLASS = "classes";
    public static final String COLUMN_CLASS_ID = "classId";
    public static final String COLUMN_CLASS_NAME = "className";
    public static final String COLUMN_SCHOOL_YEAR = "schoolYear";

    private static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + TABLE_CLASS + " (" +
                    COLUMN_CLASS_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_CLASS_NAME + " TEXT, " +
                    COLUMN_SCHOOL_YEAR + " TEXT, " +
                    COLUMN_TEACHER_ID + " TEXT, " +
                    COLUMN_SCHEDULE_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_TEACHER_ID + ") REFERENCES " + TABLE_TEACHER + "(" + COLUMN_TEACHER_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_SCHEDULE_ID + ") REFERENCES " + TABLE_SCHEDULE + "(" + COLUMN_SCHEDULE_ID + "));";

    // ------------------ STUDENTS ------------------
    public static final String TABLE_STUDENT = "students";
    public static final String COLUMN_STUDENT_ID = "studentId";
    public static final String COLUMN_STUDENT_NAME = "fullName";
    public static final String COLUMN_STUDENT_ADDRESS = "address";
    public static final String COLUMN_STUDENT_DOB = "dob";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + TABLE_STUDENT + " (" +
                    COLUMN_STUDENT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_PARENT_ID + " TEXT, " +
                    COLUMN_STUDENT_NAME + " TEXT, " +
                    COLUMN_STUDENT_ADDRESS + " TEXT, " +
                    COLUMN_STUDENT_DOB + " TEXT, " +
                    COLUMN_CLASS_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_PARENT_ID + ") REFERENCES " + TABLE_PARENT + "(" + COLUMN_PARENT_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_CLASS_ID + ") REFERENCES " + TABLE_CLASS + "(" + COLUMN_CLASS_ID + "));";

    // ------------------ ACCOUNTS ------------------
    public static final String TABLE_ACCOUNT = "accounts";
    public static final String COLUMN_ACCOUNT_ID = "accountId";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ROLE = "role";

    private static final String CREATE_ACCOUNT_TABLE =
            "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                    COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_ROLE + " INTEGER, " +
                    COLUMN_TEACHER_ID + " TEXT, " +
                    COLUMN_PARENT_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_TEACHER_ID + ") REFERENCES " + TABLE_TEACHER + "(" + COLUMN_TEACHER_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_PARENT_ID + ") REFERENCES " + TABLE_PARENT + "(" + COLUMN_PARENT_ID + "));";

    // ------------------ STUDENT_TO_CLASS ------------------
    public static final String TABLE_STUDENT_TO_CLASS = "studentToClass";
    public static final String COLUMN_STUDENT_TO_CLASS_ID = "studentClassID";

    private static final String CREATE_STUDENT_TO_CLASS_TABLE =
            "CREATE TABLE " + TABLE_STUDENT_TO_CLASS + " (" +
                    COLUMN_STUDENT_TO_CLASS_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_STUDENT_ID + " TEXT, " +
                    COLUMN_CLASS_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_STUDENT + "(" + COLUMN_STUDENT_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_CLASS_ID + ") REFERENCES " + TABLE_CLASS + "(" + COLUMN_CLASS_ID + "));";


    // ------------------ SCHEDULES_TO_CLASS ------------------
    public static final String TABLE_SCHEDULES_TO_CLASS = "schedulesToClass";
    public static final String COLUMN_SCHEDULES_TO_CLASS_ID = "scheduleClassID";

    private static final String CREATE_SCHEDULES_TO_CLASS_TABLE =
            "CREATE TABLE " + TABLE_SCHEDULES_TO_CLASS + " (" +
                    COLUMN_SCHEDULES_TO_CLASS_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_SCHEDULE_ID + " TEXT, " +
                    COLUMN_CLASS_ID + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_SCHEDULE_ID + ") REFERENCES " + TABLE_SCHEDULE + "(" + COLUMN_SCHEDULE_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_CLASS_ID + ") REFERENCES " + TABLE_CLASS + "(" + COLUMN_CLASS_ID + ")" +
                    ");";


    // ------------------ CREATE & UPGRADE ------------------
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEACHER_TABLE);
        db.execSQL(CREATE_PARENT_TABLE);
        db.execSQL(CREATE_SCHEDULE_TABLE);
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_STUDENT_TO_CLASS_TABLE);
        db.execSQL(CREATE_SCHEDULES_TO_CLASS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES_TO_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_TO_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        onCreate(db);
    }
}
