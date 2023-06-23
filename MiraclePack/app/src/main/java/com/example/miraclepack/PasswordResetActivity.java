package com.example.miraclepack;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        setContentView(R.layout.fragment_passwordreset);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        repeatPasswordEditText = findViewById(R.id.editTextRepeatPassword);

        AppCompatButton buttonPasswordReset = findViewById(R.id.buttonPasswordReset);
        buttonPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPasswordReset(v);
            }
        });
    }

    public void onClickPasswordReset(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();

        if (validateInputs(email, password, repeatPassword)) {
            // Perform password reset operation
            resetPassword(email, password);
            finish(); // Go back to the login screen
        }
    }

    private boolean validateInputs(String email, String password, String repeatPassword) {
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(this, "Vul alle velden in", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(repeatPassword)) {
            Toast.makeText(this, "De wachtwoorden komen niet overeen", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetPassword(String email, String password) {
        database = dbHelper.getWritableDatabase();

        // Update password
        ContentValues values = new ContentValues();
        values.put("password", password);
        database.update("users", values, "email=?", new String[]{email});

        Toast.makeText(this, "Wachtwoord reset voltooid", Toast.LENGTH_SHORT).show();
    }
}
