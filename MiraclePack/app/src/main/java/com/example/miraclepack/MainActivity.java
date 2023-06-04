package com.example.miraclepack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.miraclepack.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private boolean isLoggedIn = false;

//    public BluetoothConnection ble;
    ListView lv;
    BroadcastReceiver broadcastReceiver;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.listview);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null){
            Toast.makeText(MainActivity.this, "BlUETOOTH NOT SUPPORTED", Toast.LENGTH_SHORT).show();
        }else{
            if(!bluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
            }
        }

//        ble = new BluetoothConnection();
//        ble.connectToBluetooth();
//        setSupportActionBar(binding.toolbar);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
//                != PackageManager.PERMISSION_GRANTED)

        // Check if the user is logged in
        isLoggedIn = checkIfLoggedIn();

        if (isLoggedIn) {
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
                if (isLoggedIn) {
                    replaceFragment(new ProfileFragment());
                } else {
                    replaceFragment(new LoginFragment()); // Show the LoginFragment for the user to log in
                }
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsFragment());
            } else if (itemId == R.id.AboutUs) {
                replaceFragment(new AboutFragment());
            }
            return true;
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 4);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_ADMIN)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 3);
        }
        bluetoothAdapter.startDiscovery();

        final ArrayList<String> arrayList = new ArrayList<>();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               String action = intent.getAction();
               if(action.equals(BluetoothDevice.ACTION_FOUND)){
                   BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                   Log.i("BLUETOOTH DEVICES", device.getName());
                   arrayList.add(device.getName());
               }

               if(arrayList.size()!=0){
                   ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                   lv.setAdapter((itemAdapter));
               }
            }
        };

        IntentFilter intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver((BroadcastReceiver) )
    }

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
        } else if (fragment instanceof SettingsFragment) {
            setTitle("Instellingen");
        } else if (fragment instanceof AboutFragment) {
            setTitle("Over ons");
        }
    }

    private boolean checkIfLoggedIn() {
        // Implement your authentication mechanism here
        // Return true if user is logged in, false otherwise
        // You can use a shared preference, database, or any other method to check the login status
        return false; // Replace with your logic
    }

    public void openSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


}

