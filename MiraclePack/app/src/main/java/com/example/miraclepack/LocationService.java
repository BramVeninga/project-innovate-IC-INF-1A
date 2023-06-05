package com.example.miraclepack;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class LocationService extends Service implements LocationListener {
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    private static final float GEOFENCE_RADIUS = 75;

    private LocationManager locationManager;
    private MyDatabaseHelper myDB;
    private List<Geofence> geofenceList;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myDB = MyDatabaseHelper.getInstance(this);
        geofenceList = myDB.getAllGeofences();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        startLocationUpdates();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        checkGeofences(location);
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkGeofences(Location currentLocation) {
        for (Geofence geofence : geofenceList) {
            float distance = currentLocation.distanceTo(geofence.getLocation());
            if (distance > GEOFENCE_RADIUS) {
                if (!isLocationInsideGeofence(currentLocation, geofence)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        showNotification();
                    }
                }
            }
        }
    }

    private boolean isLocationInsideGeofence(Location location, Geofence geofence) {
        float[] results = new float[1];
        Location.distanceBetween(
                location.getLatitude(), location.getLongitude(),
                geofence.getLatitude(), geofence.getLongitude(),
                results);
        float distanceInMeters = results[0];
        return distanceInMeters <= GEOFENCE_RADIUS;
    }

    private void showNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.baseline_notifications_24)
                        .setContentTitle("Waarschuwing!")
                        .setContentText("Je bent mogelijk iets vergeten. Tik om het overzicht van de tas te bekijken!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            // When notification permission is not granted
        }
    }

    private Notification createNotification() {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("MiraclePack")
                .setContentText("Je locatie wordt gebruikt op de achtergrond...")
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
}
