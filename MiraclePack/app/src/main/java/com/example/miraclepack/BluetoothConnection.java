
package com.example.miraclepack;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.util.Log;


public class BluetoothConnection extends AppCompatActivity {

    private BluetoothAdapter BA;

    public BluetoothConnection() {
        this.BA = BluetoothAdapter.getDefaultAdapter();
    }
    public BluetoothAdapter getBA() {
        return BA;
    }

    public void setBA(BluetoothAdapter BA) {
        this.BA = BA;
    }



    public void getBluetoothPermissions() {
        BA = BluetoothAdapter.getDefaultAdapter();
        Log.d("BluetoothTest", String.valueOf(BA));
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 2);
        }
    }

}
