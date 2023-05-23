package com.example.miraclepack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE users ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "email TEXT,"
            + "wachtwoord TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades if needed
    }

    public void insertUserCredentials(String email, String wachtwoord) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("wachtwoord", wachtwoord);
        db.insert("users", null, values);
        db.close();
    }

    public boolean checkCredentials(String email, String wachtwoord) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "email = ? AND wachtwoord = ?";
        String[] selectionArgs = {email, wachtwoord};
        Cursor cursor = db.query("users", null, selection, selectionArgs, null, null, null);
        boolean hasCredentials = cursor.moveToFirst();
        cursor.close();
        db.close();
        return hasCredentials;
    }
}

