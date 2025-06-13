package com.example.project_prm392_kidmanagement.DAO;

import com.example.project_prm392_kidmanagement.DB.ParentDatabaseHelper;
import com.example.project_prm392_kidmanagement.Entity.Parent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class ParentDao {
    private final SQLiteDatabase db;

    public ParentDao(Context context) {
        ParentDatabaseHelper helper = new ParentDatabaseHelper(context);
        db = helper.getReadableDatabase();
    }

    public Parent getById(String id) {
        Cursor cursor = db.query("parents", null, "parentId = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            Parent parent = new Parent(
                    cursor.getString(cursor.getColumnIndexOrThrow("parentId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("address")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dob"))
            );
            cursor.close();
            return parent;
        }
        cursor.close();
        return null;
    }
}

