package com.example.miraclepack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//This class deals with creating and exchange data with the database that holds all the information about the configurations, compartments and items.
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_GEOFENCE = "geofence";
    public static final String COLUMN_GEOFENCE_LATITUDE = "LATITUDE";
    public static final String COLUMN_GEOFENCE_LONGITUDE = "LONGITUDE";
    public static final String COLUMN_GEOFENCE_RADIUS = "RADIUS";
    public static final String COLUMN_GEOFENCE_NAME = "NAME";
    private static final String DATABASE_NAME = "MiraclePack.db";
    private static final int DATABASE_VERSION = 3;
    // Tables
    private static final String TABLE_ITEM = "item";
    private static final String TABLE_CONFIG = "config";
    private static final String TABLE_COMPARTMENT = "compartment";
    private static final String TABLE_CONFIG_ITEM = "configItem";
    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_WEEKDAY = "weekDay";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ITEM_NAME = "itemID";
    private static final String COLUMN_COMPARTMENT_ID = "compartmentID";
    private static MyDatabaseHelper instance;


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Takes the queries from the String array and adds executes the queries.
    private static void addDataToDB(SQLiteDatabase db, String[] queries) {
        for (String query : queries) {
            addData(db, query);
        }
    }

//    String createConfigItemTableQuery = "CREATE TABLE " + TABLE_CONFIG_ITEM + " (" +
//            COLUMN_ITEM_ID + " INT, " +
//            COLUMN_CONFIG_ID + " INT, " +
//            COLUMN_COMPARTMENT_ID + " INT, " +
//            "FOREIGN KEY (" + COLUMN_ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + COLUMN_ID + "), " +
//            "FOREIGN KEY (" + COLUMN_CONFIG_ID + ") REFERENCES " + TABLE_CONFIG + "(" + COLUMN_ID + "), " +
//            "FOREIGN KEY (" + COLUMN_COMPARTMENT_ID + ") REFERENCES " + TABLE_COMPARTMENT + "(" + COLUMN_ID + "))";
//        db.execSQL(createConfigItemTableQuery);
//
//    String createLoginTableQuery = "CREATE TABLE " + TABLE_LOGIN + " (" +
//            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            COLUMN_EMAIL + " VARCHAR(255), " +
//            COLUMN_PASSWORD + " VARCHAR(255))";
//        db.execSQL(createLoginTableQuery);

    //Checks if the query is not null, and then executes the query
    private static void addData(SQLiteDatabase db, String Query) {
        if (Query != null) {
            db.execSQL(Query);
        }
    }

    // Ensuring that only one instance of the class is created and providing a global access point to retrieve that instance
    public static synchronized MyDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MyDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    //If the database does not exists, the following queries are executed.
    //First of all a set of queries that will create all the tables
    //Then a set of queries that will fill the database with data
    @Override
    public void onCreate(SQLiteDatabase db) {
        addDataToDB(db, new String[]{
                "CREATE TABLE " + TABLE_CONFIG + " (" +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_WEEKDAY + " TEXT,  PRIMARY KEY (" + COLUMN_NAME + " ))",
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
                        "\t('presetZondag', 'Sunday'),\n" +
                        "\t('presetMaandag', 'Monday'),\n" +
                        "\t('presetDinsdag', 'Tuesday'),\n" +
                        "\t('presetWoensdag', 'Wednesday'),\n" +
                        "\t('presetDonderdag', 'Thursday'),\n" +
                        "\t('presetVrijdag', 'Friday'),\n" +
                        "\t('presetZaterdag', 'Saturday')",
                "INSERT INTO " + TABLE_COMPARTMENT + " (" + COLUMN_COMPARTMENT_ID + ", " + COLUMN_DESCRIPTION + ")\n" +
                        "VALUES\n" +
                        "\t(0, 'Laptop compartment'),\n" +
                        "\t(1, 'First main compartment'),\n" +
                        "\t(2, 'Second main compartment'),\n" +
                        "\t(3, 'Small compartment')",
                "INSERT INTO " + TABLE_CONFIG_ITEM + " (" + COLUMN_NAME + ", " + COLUMN_COMPARTMENT_ID + ")\n" +
                        "VALUES\n" +
                        "\t('presetZondag', 0),\n" +
                        "\t('presetZondag', 1),\n" +
                        "\t('presetZondag', 2),\n" +
                        "\t('presetZondag', 3),\n" +
                        "\t('presetMaandag', 0),\n" +
                        "\t('presetMaandag', 1),\n" +
                        "\t('presetMaandag', 2),\n" +
                        "\t('presetMaandag', 3),\n" +
                        "\t('presetDinsdag', 0),\n" +
                        "\t('presetDinsdag', 1),\n" +
                        "\t('presetDinsdag', 2),\n" +
                        "\t('presetDinsdag', 3),\n" +
                        "\t('presetWoensdag', 0),\n" +
                        "\t('presetWoensdag', 1),\n" +
                        "\t('presetWoensdag', 2),\n" +
                        "\t('presetWoensdag', 3),\n" +
                        "\t('presetDonderdag', 0),\n" +
                        "\t('presetDonderdag', 1),\n" +
                        "\t('presetDonderdag', 2),\n" +
                        "\t('presetDonderdag', 3),\n" +
                        "\t('presetVrijdag', 0),\n" +
                        "\t('presetVrijdag', 1),\n" +
                        "\t('presetVrijdag', 2),\n" +
                        "\t('presetVrijdag', 3),\n" +
                        "\t('presetZaterdag', 0),\n" +
                        "\t('presetZaterdag', 1),\n" +
                        "\t('presetZaterdag', 2),\n" +
                        "\t('presetZaterdag', 3)"

        });
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPARTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        onCreate(db);
    }

//    Cursor readAllData() {
//        String query = "SELECT * FROM"; // Query to select all data
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = null;
//        if(db != null) {
//            cursor = db.rawQuery(query, null);
//        }
//        return cursor;
//    }

    //Adds an item too the ConfigurationItem table, according to the ConfigurationItem object
    public void updateConfigItem(ConfigurationItem item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor compartmentId = this.getCompartmentId(item);
        compartmentId.moveToFirst();

        cv.put(COLUMN_ITEM_NAME, item.getName());
        database.update(TABLE_CONFIG_ITEM, cv, COLUMN_NAME + "=? AND " + COLUMN_COMPARTMENT_ID + "=?", new String[]{item.getConfigurationName(), compartmentId.getString(0)});
        compartmentId.close();
        database.close();
    }

    //Returns multiple rows of the ConfigurationItem table, according to the name of a Configuration object
    public Cursor getConfigItems(Configuration configuration) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(TABLE_CONFIG_ITEM, new String[]{COLUMN_NAME, COLUMN_COMPARTMENT_ID, COLUMN_ITEM_NAME}, COLUMN_NAME + "=?", new String[]{configuration.getName()}, null, null, null);
    }

    //Takes multiple rows from the ConfigurationItem Table, and fills a Arraylist with new ConfigurationItems
    public ArrayList<ConfigurationItem> fillConfigItems(Cursor query) {
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

    //Returns a row with the compartmentID of a compartment with a given description.
    public Cursor getCompartmentId(ConfigurationItem item) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(TABLE_COMPARTMENT, new String[]{COLUMN_COMPARTMENT_ID}, COLUMN_DESCRIPTION + "=?", new String[]{item.getCompartment().getDescription()}, null, null, null);
    }

    //Returns a row with a compartment, with a given compartmentID
    public Cursor getCompartment(String compId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(TABLE_COMPARTMENT, new String[]{COLUMN_COMPARTMENT_ID, COLUMN_DESCRIPTION}, COLUMN_COMPARTMENT_ID + "=?", new String[]{compId}, null, null, null);
    }

    //Takes a row with a compartment and sets it in a ConfigurationItem
    public void filloutCompInConfigItem(Cursor query, ConfigurationItem configItem) {
        if (query.moveToFirst()) {
            do {
                int compId = query.getInt(0);
                String compDescription = query.getString(1);
                configItem.getCompartment().setCompartmentId(compId);
                configItem.getCompartment().setDescription(compDescription);
            } while (query.moveToNext());
        }
        query.close();
    }

    //Returns multiple rows with all the compartments form the Compartment Table
    public Cursor getCompartments() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(TABLE_COMPARTMENT, new String[]{COLUMN_COMPARTMENT_ID, COLUMN_DESCRIPTION}, null, null, null, null, null);
    }

    //Takes multiple rows form the compartment table filled with CompartmentID's and descriptions, and adds them to an arraylist.
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

    //Returns multiple rows, with all the configurations from the Configuration table
    public Cursor getConfiguration() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(TABLE_CONFIG, new String[]{COLUMN_NAME, COLUMN_WEEKDAY}, null, null, null, null, null);
    }

    //Takes multiple rows with configurations from the Configuration table, and puts them in an arraylist
    public List<Configuration> fillConfigurations(Cursor query) {
        List<Configuration> configurations = new ArrayList<>();
        if (query.moveToFirst()) {
            do {
                String name = query.getString(0);
                String weekday = query.getString(1);
                Configuration configuration = new Configuration(name, weekday);
                configurations.add(configuration);
            } while (query.moveToNext());
        }
        query.close();
        return configurations;

    }

    public List<Compartment> getUsedCompartmentsOfCurrentDay() {
        // Get current day of the week
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Get configurations based on current day of the week
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor configurationCursor = database.query(TABLE_CONFIG, new String[]{COLUMN_NAME, COLUMN_WEEKDAY},
                COLUMN_WEEKDAY + "=?", new String[]{getWeekdayString(currentDayOfWeek)},
                null, null, null);

        List<Configuration> configurations = fillConfigurations(configurationCursor);
        configurationCursor.close();

        List<Compartment> usedCompartments = new ArrayList<>();

        // For every configuration based on current day
        for (Configuration configuration : configurations) {

            Cursor configItemCursor = getConfigItems(configuration);
            ArrayList<ConfigurationItem> configItems = fillConfigItems(configItemCursor);
            configItemCursor.close();

            // Add used compartments and return to a list
            for (ConfigurationItem configItem : configItems) {
                Compartment compartment = configItem.getCompartment();
                if (!usedCompartments.contains(compartment)) {
                    usedCompartments.add(compartment);
                }
            }
        }

        return usedCompartments;
    }

    // Method to get current day of the week
    private String getWeekdayString(int dayOfWeek) {
        String[] weekdays = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        if (dayOfWeek >= 1 && dayOfWeek <= 7) {
            return weekdays[dayOfWeek];
        }
        return "";
    }

    // Deletes geofence based on the geofence name in de database
    public void deleteGeofence(String geofenceName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GEOFENCE, COLUMN_GEOFENCE_NAME + " = ?", new String[]{geofenceName});
        db.close();
    }

    // Adding geofence to the database based on current location
    public void addGeofence(Geofence geofence) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, geofence.getName());
        values.put(COLUMN_GEOFENCE_LATITUDE, geofence.getLatitude());
        values.put(COLUMN_GEOFENCE_LONGITUDE, geofence.getLongitude());
        values.put(COLUMN_GEOFENCE_RADIUS, geofence.getRadius());
        db.insert(TABLE_GEOFENCE, null, values);
        db.close();
    }

    // Retrieving all geofences stored in database and return all objects as a list
    public List<Geofence> getAllGeofences() {
        List<Geofence> geofenceList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GEOFENCE, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GEOFENCE_NAME));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GEOFENCE_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GEOFENCE_LONGITUDE));
                float radius = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_GEOFENCE_RADIUS));
                Geofence geofence = new Geofence(name, latitude, longitude, radius);
                geofenceList.add(geofence);
            } while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        db.close();
        return geofenceList;
    }
}
