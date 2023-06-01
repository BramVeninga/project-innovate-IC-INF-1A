package com.example.miraclepack;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

    DatabaseHelper myDB;
    ArrayList<String> bagContentName, bagContentCompartment;
    CustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bag, container, false);

        Spinner weekDaySpinner = view.findViewById(R.id.weekdaySpinner);

        List<String> weekDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        for (int i = 0; i < 7; i++) {
            String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
            weekDays.add(day);
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, weekDays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekDaySpinner.setAdapter(adapter);

        Calendar today = Calendar.getInstance();
        int currentDayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int selectedPosition = currentDayOfWeek - 1;

        weekDaySpinner.setSelection(selectedPosition);


        addBagContent = view.findViewById(R.id.addBagContent);
        addBagContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        return view;
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


//        myDB = new MyDatabaseHelper(BagFragment.this);
//        //
//
//        storeDataInArrays();
//
//        customAdapter = new CustomAdapter(BagFragment.this, );
//        recyclerView.setAdapter(customAdapter);
//        recyclerView.setLayoutManger(new LinearLayoutManager(BagFragment.this));
//
//    }



//
//    void storeDataInArrays() {
//        Cursor cursor = myDB.readAllData();
//        if(cursor.getCount() == 0) {
//            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            while (cursor.moveToNext()) {
//
//            }
//        }
