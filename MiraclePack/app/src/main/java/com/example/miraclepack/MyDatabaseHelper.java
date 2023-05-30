package com.example.miraclepack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MiraclePack.db";
    private static final int DATABASE_VERSION =3;

    // Tables
    private static final String TABLE_CONFIG = "CONFIGURATION";
    private static final String TABLE_COMPARTMENT = "COMPARTMENT";
    private static final String TABLE_CONFIG_ITEM = "CONFIGURATIONITEM";

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_WEEKDAY = "weekDay";
    private static final String COLUMN_MAIN = "MAIN";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ITEM_NAME = "itemID";
    private static final String COLUMN_COMPARTMENT_ID = "compartmentID";
    public static final String TABLE_GEOFENCE = "GEOFENCE";
    public static final String COLUMN_GEOFENCE_LATITUDE = "LATITUDE";
    public static final String COLUMN_GEOFENCE_LONGITUDE = "LONGITUDE";
    public static final String COLUMN_GEOFENCE_RADIUS = "RADIUS";
    public static final String COLUMN_GEOFENCE_NAME = "NAME";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addDataToDB(db, new String[]{
                "CREATE TABLE " + TABLE_CONFIG + " (" +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_WEEKDAY + " TEXT, " +
                        COLUMN_MAIN + " INTEGER, PRIMARY KEY (" + COLUMN_NAME + " ))",
                "CREATE TABLE " + TABLE_COMPARTMENT + " (" +
                        COLUMN_COMPARTMENT_ID + " INTEGER, " +
                        COLUMN_DESCRIPTION + " TEXT, PRIMARY KEY (" + COLUMN_COMPARTMENT_ID + " ))",
                "CREATE TABLE " + TABLE_CONFIG_ITEM + " (" +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_COMPARTMENT_ID + " INTEGER, " +
                        COLUMN_ITEM_NAME + " TEXT, " +
                        "PRIMARY KEY (" + COLUMN_NAME + ", " + COLUMN_COMPARTMENT_ID + "), " +
                        "FOREIGN KEY (" + COLUMN_NAME + ") REFERENCES " + TABLE_CONFIG + "(" + COLUMN_NAME + "), " +
                        "FOREIGN KEY (" + COLUMN_COMPARTMENT_ID + ") REFERENCES " + TABLE_COMPARTMENT + "(" + COLUMN_ID + "))",
                "CREATE TABLE " + TABLE_GEOFENCE + " (" + COLUMN_GEOFENCE_NAME + " TEXT, " +
                        COLUMN_GEOFENCE_LATITUDE + " REAL, " +
                        COLUMN_GEOFENCE_LONGITUDE + " REAL, " +
                        COLUMN_GEOFENCE_RADIUS + " INTEGER, PRIMARY KEY (" + COLUMN_GEOFENCE_NAME + "))"
        });

        addDataToDB(db, new String[]{
                "INSERT INTO " + TABLE_CONFIG + "(" + COLUMN_NAME + ", " + COLUMN_WEEKDAY + ")\n" +
                "VALUES\n" +
                "\t('testMaandag', 'Monday'),\n" +
                "\t('testDinsdag', 'Tuesday'),\n" +
                "\t('testDonderdag', 'Thursday'),\n" +
                "\t('testVrijdag', 'Friday')",
                "INSERT INTO " + TABLE_COMPARTMENT + " (" + COLUMN_COMPARTMENT_ID + ", " + COLUMN_DESCRIPTION + ")\n" +
                "VALUES\n" +
                "\t(0, 'Laptop compartment'),\n" +
                "\t(1, 'First main compartment'),\n" +
                "\t(2, 'Second main compartment'),\n" +
                "\t(3, 'Small compartment')",
                "INSERT INTO " + TABLE_CONFIG_ITEM + " (" + COLUMN_NAME + ", " + COLUMN_COMPARTMENT_ID + ")\n" +
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
                "\t('testVrijdag', 3)"
        });
    }

    private static void addDataToDB(SQLiteDatabase db, String[] queries) {
        for (String query: queries) {
            addData(db, query);
        }
    }

    private static void addData(SQLiteDatabase db, String Query) {
        if (Query != null) {
            db.execSQL(Query);
        }
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
        database.update(TABLE_CONFIG_ITEM, cv, COLUMN_NAME + "=? AND " + COLUMN_COMPARTMENT_ID + "=?", new String[]{item.getConfigurationName(), compartmentId.getString(0)});
        compartmentId.close();
        database.close();
    }

    public Cursor getConfigItems(Configuration configuration) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor query = database.query(TABLE_CONFIG_ITEM, new String[]{COLUMN_NAME, COLUMN_COMPARTMENT_ID, COLUMN_ITEM_NAME}, COLUMN_NAME + "=?", new String[]{configuration.getName()}, null, null, null);
        return query;
    }

    public ArrayList<ConfigurationItem> fillConfigItems (Cursor query) {
        ArrayList<ConfigurationItem> configItems = new ArrayList<>();
        if (query.moveToFirst()) {
            do {
                String configName = query.getString(0);
                int compId = query.getInt(1);
                String itemName = query.getString(2);
                ConfigurationItem configItem = new ConfigurationItem();
                if (itemName != null && !itemName.isEmpty()) {
                    configItem.setName(itemName);
                } else {
                    configItem.setName("Leeg");
                }
                configItem.getCompartment().setCompartmentId(compId);
                configItem.setConfigurationName(configName);
                configItems.add(configItem);
            } while (query.moveToNext());
        }
        query.close();
        return configItems;
    }

    public Cursor getCompartmentId(ConfigurationItem item) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor query = database.query(TABLE_COMPARTMENT, new String[]{COLUMN_COMPARTMENT_ID}, COLUMN_DESCRIPTION + "=?", new String[]{item.getCompartment().getDescription()}, null, null, null);
        return  query;
    }

    public Cursor getCompartment(String compId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor query = database.query(TABLE_COMPARTMENT, new String[]{COLUMN_COMPARTMENT_ID, COLUMN_DESCRIPTION}, COLUMN_COMPARTMENT_ID + "=?", new String[]{compId}, null, null, null);
        return query;
    }

    public void filloutCompInConfigItem(Cursor query, ConfigurationItem configItem) {
        if (query.moveToFirst()){
            do {
                int compId = query.getInt(0);
                String compDescription = query.getString(1);
                configItem.getCompartment().setCompartmentId(compId);
                configItem.getCompartment().setDescription(compDescription);
            } while (query.moveToNext());
        }
        query.close();
    }

    public Cursor getCompartments() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor query = database.query(TABLE_COMPARTMENT, new String[]{COLUMN_COMPARTMENT_ID, COLUMN_DESCRIPTION}, null, null, null, null, null);
        return query;
    }

    public List<Compartment> fillCompartments(Cursor query) {
        List<Compartment> compartments = new ArrayList<>();
        if (query.moveToFirst()) {
            do {
                Integer id = query.getInt(0);
                String description = query.getString(1);
                Compartment compartment = new Compartment(id, description);
                compartments.add(compartment);
            } while (query.moveToNext());
        }
        query.close();
        return compartments;
    }

    public Cursor getConfiguration() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor query = database.query(TABLE_CONFIG, new String[]{COLUMN_NAME, COLUMN_WEEKDAY, COLUMN_MAIN}, null, null, null, null, null);
        return query;
    }

    public List<Configuration> fillConfigurations(Cursor query) {
        List<Configuration> configurations = new ArrayList<>();
        if (query.moveToFirst()) {
            do {
                String name = query.getString(0);
                String weekday = query.getString(1);
                Integer tempMain = query.getInt(2);
                boolean main = tempMain == 0 ? false : true;
                Configuration configuration = new Configuration(name, weekday, main);
                configurations.add(configuration);
            } while (query.moveToNext());
        }
        query.close();
        return configurations;
    }
}
