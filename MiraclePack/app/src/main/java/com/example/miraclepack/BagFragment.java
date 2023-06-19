package com.example.miraclepack;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//This class forms a blueprint for the backend of a page of the application.
//On this page an user can select a weekday and the user will then be show, a list of items belonging to that configuration.
public class BagFragment extends Fragment {

    private FloatingActionButton addBagContent;
    private MyDatabaseHelper myDB;
    private List<Configuration> weekDays;
    private Configuration selectedWeekday;
    private RecyclerView itemList;
    private Spinner weekDaySpinner;
    private ArrayList<ConfigurationItem> configItems;
    private AppService appService;
    private boolean serviceBound = false;

    public BagFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myDB = new MyDatabaseHelper(getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bag, container, false);

        //retrieves Views from layout
        weekDaySpinner = view.findViewById(R.id.weekdaySpinner);
        itemList = (RecyclerView) view.findViewById(R.id.itemList);
        addBagContent = view.findViewById(R.id.addBagContent);

        //fills the adapter and attaches it to the Spinner
        weekDays = myDB.fillConfigurations(myDB.getConfiguration());
        ArrayAdapter<String> adapter = setWeekdaySpinnerAdapter(weekDays);
        weekDaySpinner.setAdapter(adapter);

        addBagContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                String configName = "";
                for (Configuration configuration: weekDays) {
                    if (weekDaySpinner.getSelectedItem().toString() == configuration.getWeekday()) {
                        configName = configuration.getName();
                        break;
                    }
                }
                intent.putExtra("selectedConfig", configName);
                startActivity(intent);
            }
        });

        weekDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (appService != null) {
                    recyclerViewSetup(position, BagFragment.this.itemList, appService.getMatchingCompartments());
                    setSelectedWeekday(weekDaySpinner.getAdapter().getItem(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void setSelectedWeekday(String weekday) {
        for (Configuration configuration: weekDays) {
            if (configuration.getWeekday().equals(weekday)) {
                this.selectedWeekday = configuration;
                if (appService != null) {
                    appService.setSelectedWeekday(this.selectedWeekday);
                }
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bindService();
    }

    private void bindService() {
        Intent serviceIntent = new Intent(getContext(), AppService.class);
        getActivity().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    //Returns an index fo the Spinner, so the Spinner can be opened with the current day as default.
    @NonNull
    private Integer getWeekdaySpinnerIndex(List<Configuration> configList) {
        String day = getToday();
        Integer weekDaySpinnerIndex = determineSpinnerStartIndex(configList, day);
        if (weekDaySpinnerIndex == -1) {
            weekDaySpinnerIndex = 0;
            Log.d("Error", "getWeekdaySpinnerIndex: Error, day not found.");
        }
        return weekDaySpinnerIndex;
    }

    @Nullable
    private static String getToday() {
        Calendar today = Calendar.getInstance();
        String day = today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        return day;
    }

    //Returns an adapter with all the configurations from the SQLite database.
    @NonNull
    private ArrayAdapter<String> setWeekdaySpinnerAdapter(List<Configuration> configList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        for (Configuration configuration: configList) {
            adapter.add(configuration.getWeekday());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    //Fills the recyclerView with data from all the compartments relevant to the selected day.
    private void recyclerViewSetup(Integer count, RecyclerView recyclerView, ArrayList<Compartment> matchingCompartments) {
        if (appService != null) {
            ArrayList<ConfigurationItem> configItemList = appService.compareCompartmentsAndConfigurations(weekDays.get(count));
            CustomAdapter recyclerAdapter = new CustomAdapter(getContext(), configItemList);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    //Gets the position of the entered day from the list. Defaults to -1 if there is no configuration for said day.
    @NonNull
    private static Integer determineSpinnerStartIndex(List<Configuration> list, String day) {
        Integer count = 0;
        for (Configuration configuration: list) {
            if(day.equals(configuration.getWeekday())) {
                return count;
            } else {
                count++;
            }
        }
        return -1;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppService.MyBinder binder = (AppService.MyBinder) service;
            appService = binder.getService();
            serviceBound = true;

            selectedWeekday = appService.getSelectedWeekday();

            String today = getToday();

            if (selectedWeekday == null) setSelectedWeekday(today);

            Integer weekDaySpinnerIndex = getWeekdaySpinnerIndex(weekDays);
            weekDaySpinner.setSelection(determineSpinnerStartIndex(weekDays, selectedWeekday.getWeekday()));

            recyclerViewSetup(weekDaySpinnerIndex, itemList, appService.getMatchingCompartments());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
}