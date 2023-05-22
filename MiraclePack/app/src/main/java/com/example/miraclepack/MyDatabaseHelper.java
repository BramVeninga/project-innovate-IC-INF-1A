package com.example.miraclepack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "MiraclePack.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_ITEM = "item";
    private static final String TABLE_CONFIG = "config";
    private static final String TABLE_COMPARTMENT = "compartment";
    private static final String TABLE_CONFIG_ITEM = "configItem";

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_WEEKDAY = "weekDay";
    private static final String COLUMN_COMPARTMENT_NUMBER = "compartmentNumber";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ITEM_ID = "itemID";
    private static final String COLUMN_CONFIG_ID = "configID";
    private static final String COLUMN_COMPARTMENT_ID = "compartmentID";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createItemTableQuery = "CREATE TABLE " + TABLE_ITEM + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " VARCHAR(255))";
        db.execSQL(createItemTableQuery);

        String createConfigTableQuery = "CREATE TABLE " + TABLE_CONFIG + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " VARCHAR(255), " +
                COLUMN_WEEKDAY + " VARCHAR(255))";
        db.execSQL(createConfigTableQuery);

        String createCompartmentTableQuery = "CREATE TABLE " + TABLE_COMPARTMENT + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COMPARTMENT_NUMBER + " INT, " +
                COLUMN_DESCRIPTION + " VARCHAR(255))";
        db.execSQL(createCompartmentTableQuery);

        String createConfigItemTableQuery = "CREATE TABLE " + TABLE_CONFIG_ITEM + " (" +
                COLUMN_ITEM_ID + " INT, " +
                COLUMN_CONFIG_ID + " INT, " +
                COLUMN_COMPARTMENT_ID + " INT, " +
                "FOREIGN KEY (" + COLUMN_ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY (" + COLUMN_CONFIG_ID + ") REFERENCES " + TABLE_CONFIG + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY (" + COLUMN_COMPARTMENT_ID + ") REFERENCES " + TABLE_COMPARTMENT + "(" + COLUMN_ID + "))";
        db.execSQL(createConfigItemTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPARTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        onCreate(db);
    }

    void addBagContent(int itemID, int configID, int compartmentID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ITEM_ID, itemID);
        cv.put(COLUMN_CONFIG_ID, configID);
        cv.put(COLUMN_COMPARTMENT_ID, compartmentID);

        long result = db.insert(TABLE_CONFIG_ITEM, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Fout bij het toevoegen", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Succesvol toegevoegd!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM "; // Query to select all data
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
