package com.example.miraclepack;

import android.location.Location;

public class Geofence {
    private final String name;
    private final double latitude;
    private final double longitude;
    private final float radius;

    public Geofence(String name, double latitude, double longitude, float radius) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    // Base variables for geofence
    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getRadius() {
        return radius;
    }

    // Getting location based on lat and long
    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}

