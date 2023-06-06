package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;


public class HomeFragment extends Fragment {

    private Button viewContentButton;
    private boolean bluetoothConnected = true;
    private boolean bluetoothAllowed = true;
    private boolean batteryCharging = false;
    private String configNameString = "Tas van maandag";
    private boolean gpsConnected = true;
    private boolean gpsAllowed = true;
    TextView gpsText;
    ImageView gpsImage;
    ImageView bluetoothImage;
    TextView bluetoothText;
    ImageView batteryImage;
    TextView batteryText;
    TextView name;
    TextView configName;
    private int batteryPercentage = 60;
    private String batteryState;


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

        viewContentButton = view.findViewById(R.id.viewContentButton);

        name = view.findViewById(R.id.configName);

        bluetoothText = view.findViewById(R.id.bluetoothState);
        bluetoothImage = view.findViewById(R.id.bluetoothStatusImage);
        batteryText = view.findViewById(R.id.batteryPercentage);
        batteryImage = view.findViewById(R.id.batteryStatusImage);
        configName = view.findViewById(R.id.configName);
        gpsImage = view.findViewById(R.id.gpsStateImage);
        gpsText = view.findViewById(R.id.gpsState);

        setBluetoothState();
        setBatteryState();
        setBatteryImageState();
        setConfigName();
        isGpsConnected();

        viewContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new BagFragment());
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        getActivity().setTitle("Tas");
    }

    public boolean setBluetoothState() {
        if (!bluetoothAllowed) {
            bluetoothText.setText("Bluetooth inschakelen");
            bluetoothImage.setImageResource(R.drawable.baseline_bluetooth_disabled_24);
            return false;
        }
        else if (!bluetoothConnected) {
            bluetoothText.setText("Tas verbinden");
            bluetoothImage.setImageResource(R.drawable.baseline_bluetooth_24);
            return false;
        }
        else {
            bluetoothText.setText("Verbonden");
            bluetoothImage.setImageResource(R.drawable.baseline_bluetooth_connected_24);
            return true;
        }
    }

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

    public void setBatteryState() {
        batteryState = String.valueOf(batteryPercentage);
        batteryText.setText(batteryState + "%");
    }

    public void setConfigName() {
        configName.setText(configNameString);
    }

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
}