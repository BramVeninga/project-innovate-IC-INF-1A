package com.example.miraclepack;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BagFragment extends Fragment {

    private FloatingActionButton addBagContent;

    public BagFragment() {
        // Required empty public constructor
    }

    MyDatabaseHelper myDB;
    ArrayList<String> bagContentName, bagContentCompartment;
    CustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myDB = new MyDatabaseHelper(getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bag, container, false);

        Spinner weekDaySpinner = view.findViewById(R.id.weekdaySpinner);

        List<Configuration> weekDays = myDB.fillConfigurations(myDB.getConfiguration());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        for (Configuration configuration: weekDays) {
            adapter.add(configuration.getWeekday());
        }
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekDaySpinner.setAdapter(adapter);

        Calendar today = Calendar.getInstance();
        String day = today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        Integer count = determineSpinnerStartIndex(weekDays, day);
        weekDaySpinner.setSelection(count);

        addBagContent = view.findViewById(R.id.addBagContent);
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

        return view;
    }

    @NonNull
    private static Integer determineSpinnerStartIndex(List<Configuration> list, String day) {
        Integer count = 0;
        for (Configuration configuration: list) {
            if(day == configuration.getWeekday()) {
                return count;
            } else {
                count++;
            }
        }
        return 0;
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