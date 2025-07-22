package com.example.se1829_prm392_kindergartenms.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;
import com.example.se1829_prm392_kindergartenms.Mapper.TeacherMapper;

import java.util.ArrayList;
import java.util.List;

public class TeacherDao {
    private final SqlDatabaseHelper dbHelper;

    public TeacherDao(Context context) {
        dbHelper = new SqlDatabaseHelper(context);
    }

    public long insert(Teacher teacher) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("teacherId", teacher.getTeacherId());
        values.put("fullName", teacher.getFullName());
        values.put("address", teacher.getAddress());
        values.put("phone", teacher.getPhone());
        values.put("dob", teacher.getDob());

        return db.insert(SqlDatabaseHelper.TABLE_TEACHER, null, values);
    }

    public boolean update(Teacher teacher) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullName", teacher.getFullName());
        values.put("address", teacher.getAddress());
        values.put("phone", teacher.getPhone());
        values.put("dob", teacher.getDob());

        int rowsAffected = db.update(
                SqlDatabaseHelper.TABLE_TEACHER,
                values,
                "teacherId = ?",
                new String[]{teacher.getTeacherId()}
        );

        return rowsAffected > 0;
    }
    public List<String> getTeacherSchedulesInClass(String teacherId, String classId, String targetDate) {
        List<String> scheduleList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.activityName, s.timeStart, s.timeEnd, s.timeDate " +
                "FROM schedules s " +
                "JOIN schedulesToClass stc ON s.scheduleId = stc.scheduleId " +
                "JOIN classes c ON stc.classId = c.classId " +
                "WHERE c.teacherId = ? AND c.classId = ? AND s.timeDate = ? " +
                "ORDER BY s.timeStart ASC";

        Cursor cursor = db.rawQuery(query, new String[]{teacherId, classId, targetDate});

        while (cursor.moveToNext()) {
            String activityName = cursor.getString(0);
            String timeStart = cursor.getString(1);
            String timeEnd = cursor.getString(2);
            String timeDate = cursor.getString(3);

            String[] parts = activityName.split(" - ", 2);
            String subject = parts.length > 0 ? parts[0].trim() : "Chưa rõ";
            String lesson = parts.length > 1 ? parts[1].trim() : "Chưa rõ";

            String line = classId + "|" + subject + "|" + lesson + "|" + timeStart + "|" + timeEnd;
            scheduleList.add(line);
        }

        cursor.close();
        return scheduleList;
    }

    public boolean delete(String teacherId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(
                SqlDatabaseHelper.TABLE_TEACHER,
                "teacherId = ?",
                new String[]{teacherId}
        );
        return rowsDeleted > 0;
    }

    public List<Teacher> getAll() {
        List<Teacher> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_TEACHER,
                null,
                null,
                null,
                null,
                null,
                "fullName ASC"
        );

        while (cursor.moveToNext()) {
            list.add(TeacherMapper.fromCursor(cursor));
        }

        cursor.close();
        return list;
    }

    public List<String> getTeacherSchedulesInClass(String teacherId, String classId) {
        List<String> scheduleList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.activityName, s.timeStart, s.timeEnd, s.timeDate " +
                "FROM schedules s " +
                "JOIN schedulesToClass stc ON s.scheduleId = stc.scheduleId " +
                "JOIN classes c ON stc.classId = c.classId " +
                "WHERE c.teacherId = ? AND c.classId = ? " +
                "ORDER BY s.timeDate ASC, s.timeStart ASC";

        Cursor cursor = db.rawQuery(query, new String[]{teacherId, classId});
        int tiet = 1;

        while (cursor.moveToNext()) {
            String activityName = cursor.getString(0);
            String timeStart = cursor.getString(1);
            String timeEnd = cursor.getString(2);
            String timeDate = cursor.getString(3);

            String[] parts = activityName.split(" - ", 2);
            String subject = parts.length > 0 ? parts[0].trim() : "Chưa rõ";
            String lesson = parts.length > 1 ? parts[1].trim() : "Chưa rõ";

            String line = "📅 " + timeDate + "\n"
                    + "⏰ " + timeStart + " - " + timeEnd + "\n"
                    + "Môn: " + subject + " – Bài: " + lesson;

            scheduleList.add(line);
            tiet++;
        }

        cursor.close();

        if (scheduleList.isEmpty()) {
            scheduleList.add("Không có tiết học nào cho giáo viên này trong lớp " + classId);
        }

        return scheduleList;
    }

    public Teacher getById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_TEACHER,
                null,
                "teacherId = ?",
                new String[]{id},
                null,
                null,
                null
        );

        Teacher teacher = null;
        if (cursor.moveToFirst()) {
            teacher = TeacherMapper.fromCursor(cursor);
        }

        cursor.close();
        return teacher;
    }

    public String generateAutoTeacherId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();;
        String query = "SELECT teacherId FROM teachers ORDER BY teacherId DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String lastId = cursor.getString(0);
            try {
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("GV%03d", num);
            } catch (NumberFormatException e) {
                return "GV001";
            }
        }
        cursor.close();
        db.close();
        return "GV001"; // Nếu không có giáo viên nào
    }



}
