package com.example.miraclepack;

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

    // Login check
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonMakeAccount = findViewById(R.id.buttonMakeAccount);

        buttonMakeAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Get username and password entered by the user
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Perform signup logic
                boolean isSuccess = performSignUp(email, password);

                if (isSuccess)
                {
                    // Signup successful
                    DatabaseHelper dbHelper = new DatabaseHelper(SignUpActivity.this);
                    long result = dbHelper.addUser(email, password, SignUpActivity.this);
                    if (result != -1)
                    {
                        // Save the login details
                        dbHelper.insertLoginDetails(email, password, SignUpActivity.this);

                        Toast.makeText(SignUpActivity.this, "Account succesvol aangemaakt", Toast.LENGTH_SHORT).show();
                        finish(); // Close the signup page and go back to the previous screen
                    } else
                    {
                        Toast.makeText(SignUpActivity.this, "Het is niet gelukt om een account aan te maken. Probeer het opnieuw", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean performSignUp(String email, String password)
    {
        return !email.isEmpty() && !password.isEmpty();
    }
}


