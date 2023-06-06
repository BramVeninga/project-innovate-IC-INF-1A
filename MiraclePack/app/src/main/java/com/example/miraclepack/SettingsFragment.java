package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout; // Importeer LinearLayout
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private Button aboutUsButton;
    private View openLocationSettings;
    private View openSupportSettings;

    // ...
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Retrieve button from settings layout
        openLocationSettings = view.findViewById(R.id.locationButton);
        openSupportSettings = view.findViewById(R.id.support);

        // Action according to button
        openLocationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);

                startActivity(intent);
            }
        });

        openSupportSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SupportActivity.class);

                startActivity(intent);
            }
        });

        return view;
    }

    // ...
}
