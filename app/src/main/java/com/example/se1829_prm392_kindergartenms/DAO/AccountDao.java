package com.example.se1829_prm392_kindergartenms.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;
import com.example.se1829_prm392_kindergartenms.Entity.Account;
import com.example.se1829_prm392_kindergartenms.Mapper.AccountMapper;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    private final SqlDatabaseHelper dbHelper;

    public AccountDao(Context context) {
        dbHelper = new SqlDatabaseHelper(context);
    }

    public long insert(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", account.getUsername());
        values.put("password", account.getPassword());
        values.put("email", account.getEmail());
//        values.put("role", account.isRole() ? 1 : 0);
        values.put("role", account.isRole());
        values.put("teacherId", account.getTeacherId() != null ? account.getTeacherId().getTeacherId() : null);
        values.put("parentId", account.getParentId() != null ? account.getParentId().getParentId() : null);

        return db.insert(SqlDatabaseHelper.TABLE_ACCOUNT, null, values);
    }

    public boolean update(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", account.getPassword());
        values.put("email", account.getEmail());

        int rowsAffected = db.update(
                SqlDatabaseHelper.TABLE_ACCOUNT,
                values,
                "accountId = ?",
                new String[]{String.valueOf(account.getAccountId())}
        );

        return rowsAffected > 0;
    }

    public boolean delete(int accountId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(
                SqlDatabaseHelper.TABLE_ACCOUNT,
                "accountId = ?",
                new String[]{String.valueOf(accountId)}
        );
        return rowsDeleted > 0;
    }

    public Account getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_ACCOUNT,
                null,
                "accountId = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        Account account = null;
        if (cursor.moveToFirst()) {
            account = AccountMapper.fromCursor(cursor, dbHelper.getContext());
        }

        cursor.close();
        return account;
    }

    public Account getByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_ACCOUNT,
                null,
                "username = ?",
                new String[]{username},
                null,
                null,
                null
        );

        Account account = null;
        if (cursor.moveToFirst()) {
            account = AccountMapper.fromCursor(cursor, dbHelper.getContext());
        }

        cursor.close();
        return account;
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(SqlDatabaseHelper.TABLE_ACCOUNT, null, null);
    }


    public List<Account> getAll() {
        List<Account> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_ACCOUNT,
                null,
                null,
                null,
                null,
                null,
                "username ASC"
        );

        while (cursor.moveToNext()) {
            Account account = AccountMapper.fromCursor(cursor, dbHelper.getContext());
            list.add(account);
        }

        cursor.close();
        return list;
    }
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("password", newPassword);

            int rows = db.update(
                    SqlDatabaseHelper.TABLE_ACCOUNT,
                    values,
                    "email = ?",
                    new String[]{email}
            );
            return rows > 0;
        } catch (Exception e) {
            Log.e("AccountDao", "Error updating password", e);
            return false;
        } finally {
            db.close();
        }
    }
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_ACCOUNT,
                new String[]{"email"},
                "email = ?",
                new String[]{email},
                null, null, null
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    public boolean validate(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SqlDatabaseHelper.TABLE_ACCOUNT,
                null,
                "username = ? AND password = ?",
                new String[]{username, password},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public String getParentIdByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT parentId FROM accounts WHERE username = ?",
                new String[]{username}
        );

        String parentId = null;
        if (cursor.moveToFirst()) {
            parentId = cursor.getString(0);
        }
        cursor.close();
        return parentId;
    }
}
