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

public class AddActivity extends AppCompatActivity {

    EditText itemNameInput, compartmentNameInput;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setSupportActionBar(findViewById(R.id.toolbar));

        Intent intent = getIntent();
        String selectedConfig = intent.getStringExtra("selectedConfig");

        itemNameInput = (EditText) findViewById(R.id.itemName);
        compartmentNameInput = (EditText) findViewById(R.id.compartmentNameInput);
        addButton = (Button) findViewById(R.id.addButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
        ConfigurationItem item = new ConfigurationItem();
        item.setConfigurationName(selectedConfig);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = itemNameInput.getText().toString();
                String compartmentName = compartmentNameInput.getText().toString();
                item.setName(name);
                item.setCompartmentName(compartmentName);
                myDB.updateConfigItem(item);
            }
        });

//        Spinner spinner = findViewById(R.id.weekdaySpinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weekdays, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        weekdaySpinnerInput = spinner;
//        subjectNameInput = findViewById(R.id.subjectName);
//        compartmentIdInput = findViewById(R.id.compartment);
//        addButton = findViewById(R.id.addButton);
//
//        addButton.setOnClickListener(view -> {
//            MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
//            myDB.addBagContent(weekdaySpinnerInput.getSelectedItem().toString().trim(),
//                    subjectNameInput.getText().toString().trim(),
//                    Integer.valueOf(compartmentIdInput.getText().toString().trim()));
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}