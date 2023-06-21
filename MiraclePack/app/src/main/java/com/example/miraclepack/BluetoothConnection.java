
package com.example.miraclepack;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


public class BluetoothConnection extends AppCompatActivity {

    public static final byte[] targetBluetoothAddress = {0x00, 0x21, 0x13, 0x00, 0x6C, 0x7A};
    private BluetoothAdapter BA;
    private BluetoothSocket BS;
    private BluetoothDevice bluetoothDevice;
    private BluetoothConnection bluetooth;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Handler handler;
//    private final BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private byte[] sendMessage = {115, 101, 110, 100, 59};
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
                bluetoothDevice = this.getBA().getRemoteDevice(targetBluetoothAddress); //HC-05 module mac address for a direct connection
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

    public void inputOutputStream(Context context) {
        outputStreamWrite(sendMessage);
        byte[] inputBytes = inputStreamDataCollection();
        inputStreamDataProcessing(inputBytes);
    }

    @SuppressLint("MissingPermission")
    public void setupInputOutputStream(Context context) {
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

    public void outputStreamWrite(byte[] message) {
        try {
            mmOutStream.write(message);
        }catch (Exception e){
            Log.e(TAG, "Error occurred when writing with the output stream", e);
        }
    }

    public byte[] inputStreamDataCollection() {
        byte[] message = new byte[1024];
        byte[] tempBuffer = new byte[1024];
        int bufferByte = 0; // bytes returned from read
        int counter = 0;
        try {
            while (counter >= 1023) {
                bufferByte = mmInStream.read();
                tempBuffer[counter++] = (byte)bufferByte;
                if (bufferByte == 59) {
                    message = tempBuffer;
                    return message;
                }
            }
        }catch (Exception e){
            Log.d(TAG, "Input stream was disconnected", e);
        }
        return message;
    }

    //This method takes an byte Array from the inputStream and turns it into an ArrayList which only
    //holds the compartments which are filled according to the bluetooth device.
    public ArrayList<Compartment> inputStreamDataProcessing(byte[] input){
        String inputString = getMicroPythonDictContent(input);
        ArrayList<String[]> compartmentDataPostSplit = dictContentToMatrix(inputString);
        ArrayList<Compartment> result = dictMatrixToCompartmentList(compartmentDataPostSplit);
        if (result == null) return new ArrayList<>();
        return result;
    }

    //Takes a microPython dictionary, represented through ASCII in a byte Array,
    //and turns it into an string.
    @NonNull
    private static String getMicroPythonDictContent(byte[] input) {
        int openingAccoladeIndex = 0;
        int closingAccoladeIndex = 0;
        //Turn the byte array into a String
        if (!checkArrayFilled(input)) return null;
        String inputString = new String(input, StandardCharsets.UTF_8);
        //Strip the accolades from the String
        openingAccoladeIndex = inputString.lastIndexOf("{");
        closingAccoladeIndex = inputString.lastIndexOf("}");
        inputString = inputString.substring(++openingAccoladeIndex, closingAccoladeIndex);
        Log.d("BluetoothMessage", "Message: " + inputString);
        return inputString;
    }

    public static boolean checkArrayFilled(byte[] bytes) {
        for (byte b : bytes) {
            if (b != 0) {
                return true;
            }
        }
        return false;
    }

    //Takes a string, which has a microPython dictionary as content, and turns it into a matrix with String Arrays.
    @NonNull
    private static ArrayList<String[]> dictContentToMatrix(String inputString) {
        if (inputString == null) return null;
        ArrayList<String[]> compartmentDataPostSplit = new ArrayList<>();
        //First split the string into an array
        String[] inputArray = inputString.split(",");
        for (String compartmentData : inputArray) {
            //Then split the Keys from the values
            int compartmentArrayCounter = 0;
            String[] compartmentArray = compartmentData.split(":");
            for (String compartmentField : compartmentArray) {
                compartmentArray[compartmentArrayCounter++] = compartmentField.trim();
            }
            compartmentDataPostSplit.add(compartmentArray);
        }
        return compartmentDataPostSplit;
    }

    //Takes a Matrix with String Arrays and turns it into an ArrayList with compartment Objects,
    //which the bluetooth device says are filled.
    @NonNull
    private static ArrayList<Compartment> dictMatrixToCompartmentList(ArrayList<String[]> compartmentDataPostSplit) {
        if (compartmentDataPostSplit == null) return null;
        ArrayList<Compartment> filledCompartments = new ArrayList<>();
        for (String[] compartmentArray : compartmentDataPostSplit) {
            //Check if the compartment is filled, if so add it to the list
            if (compartmentArray[1].equals("True")) {
                int compartmentId = Integer.parseInt(compartmentArray[0].substring(1, compartmentArray[0].length() - 1)) ;
                filledCompartments.add(new Compartment(compartmentId, ""));
            }
        }
        return filledCompartments;
    }
}
