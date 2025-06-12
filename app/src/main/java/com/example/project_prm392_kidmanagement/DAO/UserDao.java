package com.example.project_prm392_kidmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm392_kidmanagement.DB.UserDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.User;
import com.example.project_prm392_kidmanagement.Mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final UserDatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new UserDatabaseHelper(context);
    }

    public long insert(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        return db.insert(UserDatabaseHelper.TABLE_USER, null, values);
    }

    public boolean update(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", user.getPassword());
        int result = db.update(UserDatabaseHelper.TABLE_USER, values, "id=?", new String[]{String.valueOf(user.getId())});
        return result > 0;
    }

    public boolean delete(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(UserDatabaseHelper.TABLE_USER, "id=?", new String[]{String.valueOf(userId)});
        return result > 0;
    }

    public User getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserDatabaseHelper.TABLE_USER, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = UserMapper.fromCursor(cursor);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserDatabaseHelper.TABLE_USER, null, null, null, null, null, "username ASC");
        while (cursor.moveToNext()) {
            User user = UserMapper.fromCursor(cursor);
            list.add(user);
        }
        cursor.close();
        return list;
    }

    public boolean validate(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}