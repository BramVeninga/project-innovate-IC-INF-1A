
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
    public void checkBluetoothEnabled(BluetoothAdapter BA, int BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE) {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            if (BA != null && !BA.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE);
            }
        }
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

    public BluetoothAdapter getBA() {
        return BA;
    }

    public void setBA(BluetoothAdapter BA) {
        this.BA = BA;
    }
}
