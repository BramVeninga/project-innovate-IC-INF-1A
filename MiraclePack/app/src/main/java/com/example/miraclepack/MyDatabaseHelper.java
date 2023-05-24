package com.example.miraclepack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MiraclePack.db";
    private static final int DATABASE_VERSION =2;

    // Tables
//    private static final String TABLE_ITEM = "item";
    private static final String TABLE_CONFIG = "CONFIGURATION";
    private static final String TABLE_COMPARTMENT = "COMPARTMENT";
    private static final String TABLE_CONFIG_ITEM = "CONFIGURATIONITEM";

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_WEEKDAY = "weekDay";
    private static final String COLUMN_COMPARTMENT_NUMBER = "compartmentNumber";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ITEM_NAME = "itemID";
    private static final String COLUMN_CONFIG_ID = "configID";
    private static final String COLUMN_COMPARTMENT_ID = "compartmentID";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createConfigTableQuery = "CREATE TABLE " + TABLE_CONFIG + " (" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_WEEKDAY + " TEXT, PRIMARY KEY (" + COLUMN_NAME + " ))";
        db.execSQL(createConfigTableQuery);

        String createCompartmentTableQuery = "CREATE TABLE " + TABLE_COMPARTMENT + " (" +
                COLUMN_COMPARTMENT_ID + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, PRIMARY KEY (" + COLUMN_COMPARTMENT_ID + " ))";
        db.execSQL(createCompartmentTableQuery);

        String createConfigItemTableQuery = "CREATE TABLE " + TABLE_CONFIG_ITEM + " (" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_COMPARTMENT_ID + " INTEGER, " +
                COLUMN_ITEM_NAME + "TEXT, " +
                "PRIMARY KEY (" + COLUMN_NAME + ", " + COLUMN_COMPARTMENT_ID + "), " +
                "FOREIGN KEY (" + COLUMN_NAME + ") REFERENCES " + TABLE_CONFIG + "(" + COLUMN_NAME + "), " +
                "FOREIGN KEY (" + COLUMN_COMPARTMENT_ID + ") REFERENCES " + TABLE_COMPARTMENT + "(" + COLUMN_ID + "))";
        db.execSQL(createConfigItemTableQuery);

        String testDataConfiguraitonTable = "INSERT INTO " + TABLE_CONFIG + "(" + COLUMN_NAME + ", " + COLUMN_WEEKDAY + ")\n" +
                "VALUES\n" +
                "\t('testMaandag', 'Monday'),\n" +
                "\t('testDinsdag', 'Tuesday'),\n" +
                "\t('testDonderdag', 'Thursday'),\n" +
                "\t('testVrijdag', 'Friday')";
        db.execSQL(testDataConfiguraitonTable);

        String testDataCompartmentTable = "INSERT INTO " + TABLE_COMPARTMENT + " (" + COLUMN_COMPARTMENT_ID + ", " + COLUMN_DESCRIPTION + ")\n" +
                "VALUES\n" +
                "\t(0, 'Laptop compartment'),\n" +
                "\t(1, 'First main compartment'),\n" +
                "\t(2, 'Second main compartment'),\n" +
                "\t(3, 'Small compartment')";
        db.execSQL(testDataCompartmentTable);

        String testDataConfigurationItemTable = "INSERT INTO " + TABLE_CONFIG_ITEM + " (" + COLUMN_NAME + ", " + COLUMN_COMPARTMENT_ID + ")\n" +
                "VALUES\n" +
                "\t('testMaandag', 0),\n" +
                "\t('testMaandag', 1),\n" +
                "\t('testMaandag', 2),\n" +
                "\t('testMaandag', 3),\n" +
                "\t('testDinsdag', 0),\n" +
                "\t('testDinsdag', 1),\n" +
                "\t('testDinsdag', 2),\n" +
                "\t('testDinsdag', 3),\n" +
                "\t('testDonderdag', 0),\n" +
                "\t('testDonderdag', 1),\n" +
                "\t('testDonderdag', 2),\n" +
                "\t('testDonderdag', 3),\n" +
                "\t('testVrijdag', 0),\n" +
                "\t('testVrijdag', 1),\n" +
                "\t('testVrijdag', 2),\n" +
                "\t('testVrijdag', 3)";
        db.execSQL(testDataConfigurationItemTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPARTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        onCreate(db);
    }

    public void updateConfigItem(ConfigurationItem item){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor compartmentId = this.getCompartmentId(item);
        compartmentId.moveToFirst();

        cv.put(COLUMN_ITEM_NAME, item.getName());
        database.update(TABLE_CONFIG_ITEM, cv, COLUMN_NAME + "=? AND", new String[]{item.getConfigurationName(), compartmentId.getString(0)});
    }

    public Cursor getCompartmentId(ConfigurationItem item) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor query = database.query(TABLE_COMPARTMENT, new String[]{COLUMN_COMPARTMENT_ID}, COLUMN_DESCRIPTION + "=?", new String[]{item.getCompartmentName()}, null, null, null);
        return  query;
    }
}
