
package com.example.miraclepack;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BluetoothConnection extends AppCompatActivity {

    private BluetoothAdapter BA;
    private BluetoothSocket BS;
    private BluetoothDevice bluetoothDevice;
    private BluetoothConnection bluetooth;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Handler handler;
//    private final BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private byte[] mmBuffer; // mmBuffer store for the stream
    private static final String TAG = "MY_APP_DEBUG_TAG";

    public BluetoothConnection() {
        this.BA = BluetoothAdapter.getDefaultAdapter();
    }
    public BluetoothAdapter getBA() {
        return BA;
    }

    public void setBA(BluetoothAdapter BA) {
        this.BA = BA;
    }

    @SuppressLint("MissingPermission")
    public void makeConnection(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            try {   //try catch method to catch any exceptions for when the connection cannot be established
                bluetoothDevice = bluetooth.getBA().getRemoteDevice(new byte[]{0x00, 0x21, 0x13, 0x00, 0x6C, 0x7A}); //HC-05 module mac address for a direct connection
                int counter = 0;
                do {
                    BS = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);    // to make an connection possible an UUID is required, the stated MY_UUID is the most universal UUID available
                    BS.connect();
                    counter++;
                } while (!BS.isConnected() && counter < 3);  //If the bluetooth socket is not connected we try to make a connection multiple times

            } catch (Exception e) {
                Toast.makeText(context, "Kan apparaat niet vinden", Toast.LENGTH_SHORT).show(); //User feedback if an connection cannot be established
            }
//                    Log.d("BluetoothTest", "Bonded: " + BS.isConnected());    //only necessary for debugging and error validation with logcat
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

    @SuppressLint("MissingPermission")

    public void setupInputOutputStream(Context context) {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
                bluetoothDevice = bluetooth.getBA().getRemoteDevice(new byte[]{0x00, 0x21, 0x13, 0x00, 0x6C, 0x7A}); //HC-05 module mac address for a direct connection
                BS = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            }
        }catch(Exception e){
            Toast.makeText(context, "Kan apparaat niet vinden2", Toast.LENGTH_SHORT).show();
        }

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = BS.getInputStream();
        } catch (Exception e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = BS.getOutputStream();
        } catch (Exception e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }
    public byte[] inputStreamDataCollection() {
        byte[] message = new byte[1024];
        mmBuffer = new byte[1024];
        int numBytes = 0; // bytes returned from read()
        try {
            // Read from the InputStream.
            numBytes = mmInStream.read(mmBuffer);
        } catch (IOException e) {
            Log.d(TAG, "Input stream was disconnected", e);
        }
        int counter = 0;
        byte currentByte = 0;
        int messageCounter = 0;
        // Keep listening to the InputStream until an exception occurs.
            for (byte Byte: mmBuffer) {
                currentByte = Byte;
                message[counter++] = Byte;
                if(currentByte == 59) return message; // 59 is the ascii value for an ;
        }
    }

}
