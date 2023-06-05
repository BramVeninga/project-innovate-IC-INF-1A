package com.example.miraclepack;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity implements LocationListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 2;
    private MyDatabaseHelper myDB;
    private LocationManager locationManager;
    private RecyclerView geofenceRecyclerView;
    private List<Geofence> geofenceList;
    private FloatingActionButton addLocation;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        myDB = MyDatabaseHelper.getInstance(this);

        geofenceRecyclerView = findViewById(R.id.geofenceRecyclerView);
        addLocation = findViewById(R.id.addLocation);

        recyclerViewSetup();

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddGeofenceDialog();
            }
        });



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_REQUEST_CODE);
        } else {
            createNotificationChannel();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, LocationService.class));
        } else {
            startService(new Intent(this, LocationService.class));
        }

    }

    private void recyclerViewSetup() {
        geofenceList = myDB.getAllGeofences();
        GeofenceAdapter recyclerAdapter = new GeofenceAdapter(LocationActivity.this, geofenceList);
        geofenceRecyclerView.setAdapter(recyclerAdapter);
        geofenceRecyclerView.setLayoutManager(new LinearLayoutManager(LocationActivity.this));
    }

    private void showAddGeofenceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setTitle("Vul de naam van de locatie in:");

        builder.setPositiveButton("Bevestigen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    addGeofenceToDatabase(name);
                }
            }
        });
        builder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addGeofenceToDatabase(String name) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();
                float radius = 75; // Set the desired radius

                Geofence geofence = new Geofence(name, latitude, longitude, radius);
                myDB.addGeofence(geofence);

                geofenceList.add(geofence);
                Objects.requireNonNull(geofenceRecyclerView.getAdapter()).notifyItemInserted(geofenceList.size() - 1);
                geofenceRecyclerView.smoothScrollToPosition(geofenceList.size() - 1);
            } else {
                Toast.makeText(this, "Kan huidige locatie niet ophalen", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Toestemming tot locatie geweigerd", Toast.LENGTH_SHORT).show();
        }
    }

    private Location getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                List<String> providers = locationManager.getProviders(true);
                Location bestLocation = null;
                for (String provider : providers) {
                    Location location = locationManager.getLastKnownLocation(provider);
                    if (location != null && (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy())) {
                        bestLocation = location;
                    }
                }
                return bestLocation;
            }
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Toestemming voor locatie geweigerd", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createNotificationChannel();
            } else {
                Toast.makeText(this, "Toestemming voor notificaties geweigerd", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, (float) 0, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Location updates
        checkGeofences(location);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    private void checkGeofences(Location currentLocation) {
        for (Geofence geofence : geofenceList) {
            float distance = currentLocation.distanceTo(geofence.getLocation());
            if (distance > geofence.getRadius()) {
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
        return distanceInMeters <= geofence.getRadius();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void showNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Waarschuwing!")
                    .setContentText("Je bent mogelijk iets vergeten. Tik om het overzicht van de tas te bekijken!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_REQUEST_CODE);
        }
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Geofence Channel";
            String description = "Channel for Geofence Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
