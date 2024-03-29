package com.example.miraclepack;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AppService extends Service implements LocationListener {
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    private static final float GEOFENCE_RADIUS = 75;
    private final MyBinder myBinder = new MyBinder();
    private LocationManager locationManager;
    private List<Geofence> geofenceList;
    private MyDatabaseHelper myDB;
    private List<Compartment> usedCompartments;
    private ArrayList<Compartment> matchingCompartments;
    private Configuration selectedWeekday;
    private boolean isInList = false;
    private BluetoothConnection bluetooth;

    @Override
    public void onCreate() {
        super.onCreate();
        bluetooth = new BluetoothConnection();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myDB = MyDatabaseHelper.getInstance(this);
        geofenceList = myDB.getAllGeofences();
        usedCompartments = myDB.getUsedCompartmentsOfCurrentDay();
        Log.d("AppServiceMessage", "Service is gestart");
        setToday();

        // Initialise timer and task
        Timer dataUpdateTimer = new Timer();
        TimerTask dataUpdateTask = new TimerTask() {
            @Override
            public void run() {
                changeBagStatus();
            }
        };

        // Execute method every 10 seconds
        long updateInterval = 5 * 1000 ; // 5 seconds
        dataUpdateTimer.schedule(dataUpdateTask, updateInterval, updateInterval);
    }

    // Starting foreground service and background location
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        startLocationUpdates();
        return START_STICKY;
    }

    // Kill service if app is closed in task manager
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf(); // Stop the service when the app is removed from the recent tasks
        locationManager.removeUpdates(this); // Stop location updates
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    // Ensuring service does not support binding so that other components can't bind to the service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    // Check if current location has changed in order to check for geofence
    @Override
    public void onLocationChanged(Location location) {
        checkGeofences(location);
    }

    public MyBinder getMyBinder() {
        return myBinder;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public List<Geofence> getGeofenceList() {
        return geofenceList;
    }

    public void setGeofenceList(List<Geofence> geofenceList) {
        this.geofenceList = geofenceList;
    }

    public MyDatabaseHelper getMyDB() {
        return myDB;
    }

    public void setMyDB(MyDatabaseHelper myDB) {
        this.myDB = myDB;
    }

    public List<Compartment> getUsedCompartments() {
        return usedCompartments;
    }

    public void setUsedCompartments(List<Compartment> usedCompartments) {
        this.usedCompartments = usedCompartments;
    }

    public ArrayList<Compartment> getMatchingCompartments() {
        return matchingCompartments;
    }

    public Configuration getSelectedWeekday() {
        return selectedWeekday;
    }

    public void setSelectedWeekday(Configuration selectedWeekday) {
        this.selectedWeekday = selectedWeekday;
    }

    public BluetoothConnection getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(BluetoothConnection bluetooth) {
        this.bluetooth = bluetooth;
    }

    //Sets the selectedWeekday to today.
    private void setToday() {
        List<Configuration> weekDays = myDB.fillConfigurations(myDB.getConfiguration());

        Calendar today = Calendar.getInstance();
        String day = today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        for (Configuration configuration : weekDays) {
            if (configuration.getWeekday().equals(day)) {
                this.selectedWeekday = configuration;
                setSelectedWeekday(this.selectedWeekday);

            }
        }
    }

    //Retrieves the filled compartments from the bluetooth device.
    public void changeBagStatus() {
        matchingCompartments = this.getBluetooth().getCompartmentDataFromBluetoothDevice();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    // Check if currently inside or outside geofence and if something inside bag is missing
    private void checkGeofences(Location currentLocation) {
        for (Geofence geofence : geofenceList) {
            float distance = currentLocation.distanceTo(geofence.getLocation());
            if (distance > GEOFENCE_RADIUS && !isLocationInsideGeofence(currentLocation, geofence) && isContentInsideBag()) {
                showNotification();
            }
            break;
        }
    }

    // Extra check if inside geofence in case there are multiple geofences
    private boolean isLocationInsideGeofence(Location location, Geofence geofence) {
        float[] results = new float[1];
        Location.distanceBetween(
                location.getLatitude(), location.getLongitude(),
                geofence.getLatitude(), geofence.getLongitude(),
                results);
        float distanceInMeters = results[0];
        return distanceInMeters <= GEOFENCE_RADIUS;
    }

    // Check if all content is inside bag according to configuration
    private boolean isContentInsideBag() {
        // The configuration items get updated, with if they are correctly filled.
        ArrayList<ConfigurationItem> configurationItems = compareCompartmentsAndConfigurations(selectedWeekday);
        boolean contentMissing = false;
        for (ConfigurationItem configurationItem : configurationItems) {
            if (!configurationItem.isStatus()) {
                contentMissing = true;
                break;
            }
        }
        return contentMissing;
    }

    //Checks if the compartments are filled correctly and sets their filled status to true.
    public ArrayList<ConfigurationItem> compareCompartmentsAndConfigurations(Configuration configuration) {
        this.changeBagStatus();
        ArrayList<ConfigurationItem> configItemList = new ArrayList<>();
        configItemList = myDB.fillConfigItems(myDB.getConfigItems(configuration));

        for (ConfigurationItem configurationItem : configItemList) {
            this.isInList = false;
            int counter = 1;
            for (Compartment compartment : matchingCompartments) {
                if (isCompartmentCorrectlyFilled(compartment.getCompartmentId(), configurationItem.getCompartment().getCompartmentId(), configurationItem.getName(), counter, matchingCompartments.size())) {
                    configurationItem.setStatus(true);
                    break;
                }
                counter++;
            }
        }
        return configItemList;
    }

    //Checks if the compartment is filled when it should be filled, or left empty when it should be empty.
    //It does this according to the configuration.
    public boolean isCompartmentCorrectlyFilled(int compartmentID, int configurationCompartmentID, String configurationItemName, int counter, int listLength) {
        if (compartmentID == configurationCompartmentID) {
            this.isInList = true;
            if (!configurationItemName.equals("Leeg")) {
                return true;
            }
        }
        return !isInList && counter >= listLength && configurationItemName.equals("Leeg");
    }

    private void showNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "bag"); // Stel het doelfragment in als "bag"

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); // Voeg de FLAG_IMMUTABLE-vlag toe

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.baseline_notifications_24)
                        .setContentTitle("Waarschuwing!")
                        .setContentText("Je bent mogelijk iets vergeten. Tik om het overzicht van de tas te bekijken!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent) // Stel de intent in als de klikintentie
                        .setAutoCancel(true); // Sluit de notificatie nadat erop is geklikt

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    // Creating and building notification and channel
    private Notification createNotification() {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("MiraclePack")
                .setContentText("MiraclePack draait momenteel op de achtergrond...")
                .setPriority(NotificationCompat.PRIORITY_LOW);
        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MiraclePack Channel";
            String description = "Channel for MiraclePack Notifications";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public class MyBinder extends Binder {
        AppService getService() {
            return AppService.this;
        }
    }
}
