package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private RecyclerView itemList;
    private Spinner weekDaySpinner;
    private ArrayList<ConfigurationItem> configItems;

    public BagFragment() {
        // Required empty public constructor
    }

    ArrayList<String> bagContentName, bagContentCompartment;
    CustomAdapter customAdapter;

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

        Integer weekDaySpinnerIndex = getWeekdaySpinnerIndex(weekDays);
        weekDaySpinner.setSelection(weekDaySpinnerIndex);
        recyclerViewSetup(weekDaySpinnerIndex, configItems, itemList);

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
                recyclerViewSetup(position, BagFragment.this.configItems, BagFragment.this.itemList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    //Returns an index fo the Spinner, so the Spinner can be opened with the current day as default.
    @NonNull
    private Integer getWeekdaySpinnerIndex(List<Configuration> configList) {
        Calendar today = Calendar.getInstance();
        String day = today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        Integer weekDaySpinnerIndex = determineSpinnerStartIndex(configList, day);
        if (weekDaySpinnerIndex == -1) {
            weekDaySpinnerIndex = 0;
            Log.d("Error", "getWeekdaySpinnerIndex: Error, day not found.");
        }
        return weekDaySpinnerIndex;
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
    private void recyclerViewSetup(Integer count, ArrayList<ConfigurationItem> configItemList, RecyclerView recyclerView) {
        configItemList = myDB.fillConfigItems(myDB.getConfigItems(weekDays.get(count)));
        CustomAdapter recyclerAdapter = new CustomAdapter(getContext(), configItemList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    private void changeImageViewColor() {
        ImageView imageView = getView().findViewById(R.id.bagStatus);

        boolean isConditionTrue = true; // Condition

        if (isConditionTrue) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            imageView.clearColorFilter();
        }
    }
}