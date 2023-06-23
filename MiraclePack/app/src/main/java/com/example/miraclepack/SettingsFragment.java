package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private View openLocationSettings;
    private View openSupportSettings;
    private View importDatabase;
    private View exportDatabase;

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
        importDatabase = view.findViewById(R.id.importDatabase);
        exportDatabase = view.findViewById(R.id.exportDatabase);

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

        importDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImportDatabaseActivity.class);
                startActivity(intent);
            }
        });

        exportDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExportDatabaseActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
