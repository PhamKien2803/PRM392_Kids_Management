package com.example.project_prm392_kidmanagement.Mapper;
import android.database.Cursor;
import com.example.project_prm392_kidmanagement.Entity.User;

public class UserMapper {
    public static User fromCursor (Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        return new User(id, username, password);
    }
}
