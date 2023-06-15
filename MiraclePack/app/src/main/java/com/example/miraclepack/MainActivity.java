package com.example.miraclepack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import com.example.miraclepack.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
//    private boolean isLoggedIn = false;
    private SessionManager sessionManager;
//    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    private static final int BLUETOOTH_PERMISSION_CODE = 1;
//    private static final int BLUETOOTH_ADMIN_PERMISSION_CODE = 2;
//    private static final int BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE = 3;
//    private static final int PERMISSION_ALL = 4;
//    private BluetoothSocket BS;
//    private BluetoothAdapter BA;
//    private Set<BluetoothDevice> pairedDevices;
//    private Button bluetoothButton;
//    private BluetoothDevice bluetoothDevice;
//    private BluetoothConnection bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        // Check if the user is logged in
        sessionManager = new SessionManager(this);
//        this.bluetooth = new BluetoothConnection();

        if (sessionManager.isLoggedIn()) {
            replaceFragment(new HomeFragment());
        } else {
            replaceFragment(new LoginFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.bag) {
                replaceFragment(new BagFragment());
            } else if (itemId == R.id.profile) {
                if (sessionManager.isLoggedIn()) {
                    replaceFragment(new ProfileFragment());
                } else {
                    replaceFragment(new LoginFragment()); // Show the LoginFragment for the user to log in
                }
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });

//        //acquiring the necessary permissions for the bluetooth connection
//        if (!hasPermissions(this, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_ALL);
//        }

//        bluetooth.checkBluetoothEnabled(bluetooth.getBA(), BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE);
//        checkBluetoothEnabled(bluetooth.getBA(), BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE);

//        bluetoothButton = findViewById(R.id.bluetoothAddDevice);
//
//        bluetoothButton.setOnClickListener(new View.OnClickListener() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onClick(View v) {
////            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
////                pairedDevices = BA.getBondedDevices();
////                    Log.d("BluetoothTest", pairedDevices.toString());
////            }
//            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED){
//                bluetoothDevice = BA.getRemoteDevice(new byte[] {0x00,0x21,0x13,0x00,0x6C,0x7A});
//                int counter = 0;
//                do {
//                    try {
//                        BS = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
//                        BS.connect();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    counter++;
//                } while(!BS.isConnected() && counter < 3);
//
//                Log.d("BluetoothTest", "Bonded: " + BS.isConnected());
//            }
//        }
//
//    });


    }
//    public void checkBluetoothEnabled(BluetoothAdapter BA, int BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE) {
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
//            if (BA != null && !BA.isEnabled()) {
//                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBluetooth, BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE);
//            }
//        }
//    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        if (fragment instanceof HomeFragment) {
            setTitle("Miracle-Pack");
        } else if (fragment instanceof BagFragment) {
            setTitle("Tas");
        } else if (fragment instanceof LoginFragment) {
            setTitle("Inhoud van de tas");
        } else if (fragment instanceof AboutFragment) {
            setTitle("Over ons");
        }
    }

    private boolean checkIfLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_logged_in", false);
    }


    public void openSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
//    public static boolean hasPermissions(Context context, String... permissions) {
//        if (context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }


}
