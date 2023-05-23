package com.example.miraclepack;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddActivity extends AppCompatActivity {

    Spinner weekdaySpinnerInput;
    EditText subjectNameInput, compartmentIdInput;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setSupportActionBar(findViewById(R.id.toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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