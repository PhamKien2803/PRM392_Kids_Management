package com.example.project_prm392_kidmanagement.Mapper;

import android.database.Cursor;

import com.example.project_prm392_kidmanagement.Entity.Parent;

public class ParentMapper {
    public static Parent fromCursor(Cursor cursor) {
        return new Parent(
                cursor.getString(cursor.getColumnIndexOrThrow("parentId")),
                cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                cursor.getString(cursor.getColumnIndexOrThrow("address")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                cursor.getString(cursor.getColumnIndexOrThrow("dob"))
        );
    }
}
