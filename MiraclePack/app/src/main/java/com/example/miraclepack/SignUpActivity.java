package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity
{

    private EditText editTextEmail;
    private EditText editTextPassword;
    private AppCompatButton buttonMakeAccount;
    private boolean isLoggedIn;

    // Login check
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonMakeAccount = findViewById(R.id.buttonMakeAccount);

        buttonMakeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username and password entered by the user
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Perform signup logic
                boolean isSuccess = performSignUp(email, password);

                if (isSuccess) {
                    // Signup successful
                    Toast.makeText(SignUpActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the signup activity and go back to the previous screen
                } else {
                    // Signup failed
                    Toast.makeText(SignUpActivity.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean performSignUp(String email, String password) {
        // Implement your signup logic here
        // This could involve validating input, checking if the username is available,
        // and saving the user's information to a database or remote server

        // Return true if signup is successful, false otherwise
        // For this example, we'll assume signup is successful if both username and password are non-empty
        return !email.isEmpty() && !password.isEmpty();
    }
}


