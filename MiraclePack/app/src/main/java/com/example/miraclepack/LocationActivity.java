package com.example.miraclepack;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private List<Geofence> geofenceList;

    private boolean notificationDisplayed = false;

    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }

        Button addGeofence = findViewById(R.id.addLocation);
        addGeofence.setOnClickListener(v -> createGeofences());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Toestemming tot locatie geweigerd", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Receive updates for location
        lastLocation = location;
        checkGeofences(location);
    }

    private void checkGeofences(Location currentLocation) {
        boolean shouldShowNotification = false;

        for (Geofence geofence : geofenceList) {
            float distance = currentLocation.distanceTo(geofence.getLocation());
            if (distance > geofence.getRadius()) {
                shouldShowNotification = true;
                break;
            }
        }

        if (shouldShowNotification && !notificationDisplayed) {
            showNotification();
            notificationDisplayed = true;
        } else if (!shouldShowNotification && notificationDisplayed) {
            notificationDisplayed = false;
        }
    }

    private void showNotification() {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("LET OP!")
                .setContentText("U verlaat uw locatie, zonder X!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Toestemming voor meldingen geweigerd", Toast.LENGTH_SHORT).show();
        } else {
            notificationManager.notify(0, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Geofence Channel";
            String description = "Notification Channel for Geofencing";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    private void createGeofences() {
        List<Geofence> geofences = new ArrayList<>();

        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            geofences.add(new Geofence("NHLStenden", latitude, longitude, 75));

        } else {
            Toast.makeText(this, "Kan de huidige locatie niet ophalen", Toast.LENGTH_SHORT).show();
        }
    }
}
