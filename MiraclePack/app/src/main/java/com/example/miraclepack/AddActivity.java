package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

//This is a blueprint for a page of the application.
//On this page, the user can added new items to the selected configuration.
public class AddActivity extends AppCompatActivity {

    private EditText itemNameInput;
    private Spinner compartmentNameSpinner;
    private Button addButton;
    private ImageButton backButton;
    private List<Compartment> compartments;
    private MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setSupportActionBar(findViewById(R.id.toolbar));

        Intent intent = getIntent();
        String selectedConfig = intent.getStringExtra("selectedConfig");

        itemNameInput = findViewById(R.id.itemName);
        compartmentNameSpinner = findViewById(R.id.compartmentNameSpinner);
        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);
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
                Intent backIntent = new Intent(AddActivity.this, AddActivity.class);
                startActivity(backIntent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(AddActivity.this, MainActivity.class);
                backIntent.putExtra("navigatedBack", true);
                startActivity(backIntent);
                finish();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    //This methode is responsible for filling the Spinner with compartments from the database.
    public void fillCompartmentNameSpinner(List<Compartment> compartmentsList) {
        compartmentsList = myDB.fillCompartments(myDB.getCompartments());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_spinner_item);
        for (Compartment compartment : compartmentsList) {
            adapter.add(compartment.getDescription());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        compartmentNameSpinner.setAdapter(adapter);
    }
}