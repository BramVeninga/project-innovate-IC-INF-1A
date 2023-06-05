package com.example.miraclepack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class HomeBagContentActivity extends AppCompatActivity {

    boolean compartmentLaptop = true;
    boolean compartmentFirst = false;
    boolean compartmentSecond = false;
    boolean compartmentFront = false;

    TextView laptopCompartment;
    TextView firstMainCompartment;
    TextView secondMainCompartment;
    TextView frontCompartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_bag_content);

        setSupportActionBar(findViewById(R.id.toolbar));

        laptopCompartment = findViewById(R.id.laptopCompartmentText);
        firstMainCompartment = findViewById(R.id.firstMainCompartmentText);
        secondMainCompartment = findViewById(R.id.secondMainCompartmentText);
        frontCompartment = findViewById(R.id.frontCompartmentText);

        setCompartmentStatus();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String getName = intent.getStringExtra("name");

        setTitle(getName);
    }

    public void setCompartmentStatus() {
        if (compartmentLaptop) {
            laptopCompartment.setText("Laptopvak: vol");
        }
        else {
            laptopCompartment.setText("Laptopvak: leeg");
        }
        if (compartmentFirst) {
            firstMainCompartment.setText("Eerste hoofdvak: vol");
        }
        else {
            firstMainCompartment.setText("Eerste hoofdvak: leeg");
        }
        if (compartmentSecond) {
            secondMainCompartment.setText("Tweede hoofdvak: vol");
        }
        else {
            secondMainCompartment.setText("Tweede hoofdvak: leeg");
        }
        if (compartmentFront) {
            frontCompartment.setText("Voorvak: vol");
        }
        else {
            frontCompartment.setText("Voorvak: leeg");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}