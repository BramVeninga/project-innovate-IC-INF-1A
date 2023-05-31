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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;


public class HomeFragment extends Fragment {

    private Button viewContentButton;
    private boolean bluetoothConnected = true;
    private boolean bluetoothAllowed = true;
    private boolean batteryCharging = false;
    ImageView bluetoothImage;
    TextView bluetoothText;
    ImageView batteryImage;
    TextView batteryText;
    TextView name;
    private int batteryPercentage = 10;
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

        isBluetoothConnected();
        updateBatteryState();
        updateBatteryImageState();

        viewContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getName = name.getText().toString();
                Intent intent = new Intent(getActivity(), HomeBagContentActivity.class);
                intent.putExtra("name", getName);
                startActivity(intent);
            }
        });
        return view;
    }

    public boolean isBluetoothConnected() {
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

    public void updateBatteryImageState() {
        if (!isBluetoothConnected()) {
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

    public void updateBatteryState() {
        batteryState = String.valueOf(batteryPercentage);
        batteryText.setText(batteryState + "%");
    }
}