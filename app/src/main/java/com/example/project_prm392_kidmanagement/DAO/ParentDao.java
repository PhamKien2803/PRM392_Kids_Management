package com.example.project_prm392_kidmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Parent;
import com.example.project_prm392_kidmanagement.Mapper.ParentMapper;

import java.util.ArrayList;
import java.util.List;

public class ParentDao {
    private final SqlDatabaseHelper dbHelper;

    public ParentDao(Context context) {
        dbHelper = new SqlDatabaseHelper(context);
    }

    public long insert(Parent parent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("parentId", parent.getParentId());
        values.put("fullName", parent.getFullName());
        values.put("address", parent.getAddress());
        values.put("phone", parent.getPhone());
        values.put("dob", parent.getDob());

        return db.insert(SqlDatabaseHelper.TABLE_PARENT, null, values);
    }

    public boolean update(Parent parent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullName", parent.getFullName());
        values.put("address", parent.getAddress());
        values.put("phone", parent.getPhone());
        values.put("dob", parent.getDob());

        int rows = db.update(
                SqlDatabaseHelper.TABLE_PARENT,
                values,
                "parentId = ?",
                new String[]{parent.getParentId()}
        );

        return rows > 0;
    }

    public boolean delete(String parentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(
                SqlDatabaseHelper.TABLE_PARENT,
                "parentId = ?",
                new String[]{parentId}
        );
        return rows > 0;
    }

    public Parent getById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_PARENT,
                null,
                "parentId = ?",
                new String[]{id},
                null,
                null,
                null
        );

        Parent parent = null;
        if (cursor.moveToFirst()) {
            parent = ParentMapper.fromCursor(cursor);
        }
        cursor.close();
        return parent;
    }

    public List<Parent> getAll() {
        List<Parent> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_PARENT,
                null,
                null,
                null,
                null,
                null,
                "fullName ASC"
        );

        while (cursor.moveToNext()) {
            Parent parent = ParentMapper.fromCursor(cursor);
            list.add(parent);
        }
        cursor.close();
        return list;
    }
}
