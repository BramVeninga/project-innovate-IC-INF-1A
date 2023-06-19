package com.example.miraclepack;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class HomeFragment extends Fragment {
    // Variables
    private Button viewContentButton;
    private boolean bluetoothConnected = false;
    private boolean bluetoothAllowed = true;
    private boolean batteryCharging = false;
    private String configNameString = "Inhoud van de tas";
    private boolean gpsConnected = true;
    private boolean gpsAllowed = true;
    private TextView gpsText;
    private ImageView gpsImage;
    private ImageView bluetoothImage;
    private TextView bluetoothText;
    private ImageView batteryImage;
    private TextView batteryText;
    private TextView name;
    private TextView configName;
    private int batteryPercentage = 60;
    private String batteryState;
    private Button bluetoothButton;
    private BluetoothConnection bluetooth;
    private static final int BLUETOOTH_PERMISSION_CODE = 1;
    private static final int BLUETOOTH_ADMIN_PERMISSION_CODE = 2;
    private static final int BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE = 3;
    private static final int PERMISSION_ALL = 4;
    private AppService appService;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Define variables
        this.bluetooth = new BluetoothConnection();
        viewContentButton = view.findViewById(R.id.viewContentButton);
        name = view.findViewById(R.id.configName);
        bluetoothButton = view.findViewById(R.id.bluetoothAddDevice);
        bluetoothText = view.findViewById(R.id.bluetoothState);
        bluetoothImage = view.findViewById(R.id.bluetoothStatusImage);
        batteryText = view.findViewById(R.id.batteryPercentage);
        batteryImage = view.findViewById(R.id.batteryStatusImage);
        configName = view.findViewById(R.id.configName);
        gpsImage = view.findViewById(R.id.gpsStateImage);
        gpsText = view.findViewById(R.id.gpsState);

        if (!hasPermissions(getContext(), Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_ALL);
        }

        // Functions
        setBluetoothState();
        setBatteryState();
        setBatteryImageState();
        setConfigName();
        isGpsConnected();
        checkBluetoothEnabled(bluetooth.getBA(), BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE);

        // Button that opens the bagfragment
        viewContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new BagFragment());
            }
        });
        bluetoothButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (appService != null){
                    appService.getBluetooth().makeConnection(getContext());
                    appService.getBluetooth().setupInputOutputStream(getContext());
                }
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        bindWithService();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkBluetoothEnabled(BluetoothAdapter BA, int BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE) { //method to check if the Bluetooth Adapter is enabled
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            if (BA != null && !BA.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); // If the bluetooth adapter isn't null and isn't enabled, make an request to user to enable bluetooth
                startActivityForResult(enableBluetooth, BLUETOOTH_ENABLE_REQUEST_PERMISSION_CODE);
            }
        }
    }
    // Replace the fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        getActivity().setTitle("Tas");
    }

    // Set the bluetooth image and text
    public boolean setBluetoothState() {
        if (!bluetoothAllowed) {
            bluetoothText.setText("Bluetooth uitgeschakeld");
            bluetoothImage.setImageResource(R.drawable.baseline_bluetooth_disabled_24);
            return false;
        }
        else if (!bluetoothConnected) {
            bluetoothText.setText("Niet verbonden");
            bluetoothImage.setImageResource(R.drawable.baseline_bluetooth_24);
            return false;
        }
        else {
            bluetoothText.setText("Verbonden");
            bluetoothImage.setImageResource(R.drawable.baseline_bluetooth_connected_24);
            return true;
        }
    }

    // Set the battery image
    public void setBatteryImageState() {
        if (!setBluetoothState()) {
            batteryText.setText("Geen batterij gevonden");
            batteryImage.setImageResource(R.drawable.baseline_battery_unknown_24);
        }
        else if (batteryCharging) {
            batteryImage.setImageResource(R.drawable.baseline_battery_charging_full_24);
        }
        else if (batteryPercentage <= 100 && batteryPercentage >= 87.5) {
            batteryImage.setImageResource(R.drawable.baseline_battery_full_24);
        }
        else if (batteryPercentage < 87.5 && batteryPercentage >= 75 ) {
            batteryImage.setImageResource(R.drawable.baseline_battery_6_bar_24);
        }
        else if (batteryPercentage < 75 && batteryPercentage >= 62.5 ) {
            batteryImage.setImageResource(R.drawable.baseline_battery_5_bar_24);
        }
        else if (batteryPercentage < 62.5 && batteryPercentage >= 50 ) {
            batteryImage.setImageResource(R.drawable.baseline_battery_4_bar_24);
        }
        else if (batteryPercentage < 50 && batteryPercentage >= 37.5 ) {
            batteryImage.setImageResource(R.drawable.baseline_battery_3_bar_24);
        }
        else if (batteryPercentage < 37.5 && batteryPercentage >= 25 ) {
            batteryImage.setImageResource(R.drawable.baseline_battery_2_bar_24);
        }
        else if (batteryPercentage < 25 && batteryPercentage >= 12.5 ) {
            batteryImage.setImageResource(R.drawable.baseline_battery_1_bar_24);
        }
        else if (batteryPercentage < 12.5 && batteryPercentage >= 0 ) {
            batteryImage.setImageResource(R.drawable.baseline_battery_alert_24);
        }
    }

    // Set the battery text
    public void setBatteryState() {
        batteryState = String.valueOf(batteryPercentage);
        batteryText.setText(batteryState + "%");
    }

    // Set the config name
    public void setConfigName() {
        configName.setText(configNameString);
    }

    // Check if gps is connected and set the right image and text
    public boolean isGpsConnected() {
        if (!gpsAllowed) {
            gpsText.setText("Gps inschakelen");
            gpsImage.setImageResource(R.drawable.baseline_gps_off_24);
            return false;
        }
        else if (!gpsConnected) {
            gpsText.setText("Gps verbinden");
            gpsImage.setImageResource(R.drawable.baseline_gps_not_fixed_24);
            return false;
        }
        else {
            gpsText.setText("Verbonden");
            gpsImage.setImageResource(R.drawable.baseline_gps_fixed_24);
            return true;
        }
    }
    private void bindWithService() {
        Intent serviceIntent = new Intent(getContext(), AppService.class);
        getActivity().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           AppService.MyBinder binder = (AppService.MyBinder) service;
           appService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}

