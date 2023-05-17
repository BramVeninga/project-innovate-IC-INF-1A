package com.example.miraclepack;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "MiraclePack.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_ITEM = "Item";
    private static final String TABLE_CONFIGURATION = "Configuration";
    private static final String TABLE_COMPARTMENT = "Compartment";
    private static final String TABLE_CONFIGURATION_ITEM = "ConfigurationItem";

    // Columns
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_WEEKDAY = "weekDay";
    private static final String COLUMN_CONFIGURATION_NAME = "configurationName";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ITEM_NAME = "itemName";
    private static final String COLUMN_COMPARTMENT_ID = "compartmentID";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createItemTableQuery = "CREATE TABLE " + TABLE_ITEM + " (" +
                COLUMN_NAME + " TEXT PRIMARY KEY)";
        db.execSQL(createItemTableQuery);

        String createConfigurationTableQuery = "CREATE TABLE " + TABLE_CONFIGURATION + " (" +
                COLUMN_WEEKDAY + " TEXT, " +
                COLUMN_CONFIGURATION_NAME + " TEXT PRIMARY KEY)";
        db.execSQL(createConfigurationTableQuery);

        String createCompartmentTableQuery = "CREATE TABLE " + TABLE_COMPARTMENT + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createCompartmentTableQuery);

        String createConfigurationItemTableQuery = "CREATE TABLE " + TABLE_CONFIGURATION_ITEM + " (" +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_COMPARTMENT_ID + " INTEGER, " +
                COLUMN_CONFIGURATION_NAME + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_ITEM_NAME + ", " + COLUMN_COMPARTMENT_ID + ", " + COLUMN_CONFIGURATION_NAME + "), " +
                "FOREIGN KEY (" + COLUMN_ITEM_NAME + ") REFERENCES " + TABLE_ITEM + "(" + COLUMN_NAME + "), " +
                "FOREIGN KEY (" + COLUMN_COMPARTMENT_ID + ") REFERENCES " + TABLE_COMPARTMENT + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY (" + COLUMN_CONFIGURATION_NAME + ") REFERENCES " + TABLE_CONFIGURATION + "(" + COLUMN_CONFIGURATION_NAME + "))";
        db.execSQL(createConfigurationItemTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPARTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURATION_ITEM);
        onCreate(db);
    }

    void addBagContent(String itemName, String weekDay, int compartmentID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ITEM_NAME, itemName);
        cv.put(COLUMN_WEEKDAY, weekDay);
        cv.put(COLUMN_COMPARTMENT_ID, compartmentID);

        long result = db.insert(TABLE_CONFIGURATION_ITEM, null, cv);

        if(result == -1) {
            Toast.makeText(context, "Fout bij het toevoegen", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Succesvol toegevoegd!", Toast.LENGTH_SHORT).show();
        }
    }
}