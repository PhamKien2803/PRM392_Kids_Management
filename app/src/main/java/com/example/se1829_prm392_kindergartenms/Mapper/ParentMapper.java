package com.example.se1829_prm392_kindergartenms.Mapper;

import android.database.Cursor;

import com.example.se1829_prm392_kindergartenms.Entity.Parent;

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

