package com.example.miraclepack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    EditText itemNameInput;
    Spinner compartmentNameSpinner;
    Button addButton;
    List<Compartment> compartments;
    MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setSupportActionBar(findViewById(R.id.toolbar));

        Intent intent = getIntent();
        String selectedConfig = intent.getStringExtra("selectedConfig");

        itemNameInput = (EditText) findViewById(R.id.itemName);
        compartmentNameSpinner = (Spinner) findViewById(R.id.compartmentNameSpinner);
        addButton = (Button) findViewById(R.id.addButton);
        compartments = new ArrayList<>();
        myDB = new MyDatabaseHelper(AddActivity.this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ConfigurationItem item = new ConfigurationItem();
        item.setConfigurationName(selectedConfig);

        this.fillCompartmentNameSpinner(compartments);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = itemNameInput.getText().toString();
                String compartmentName = compartmentNameSpinner.getSelectedItem().toString();
                item.setName(name);
                item.getCompartment().setDescription(compartmentName);
                myDB.updateConfigItem(item);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fillCompartmentNameSpinner(List<Compartment> compartmentsList) {
        compartmentsList = myDB.fillCompartments(myDB.getCompartments());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_spinner_item);
        for (Compartment compartment: compartmentsList) {
            adapter.add(compartment.getDescription());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        compartmentNameSpinner.setAdapter(adapter);
    }
}