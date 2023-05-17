package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity
{

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private AppCompatButton buttonMakeAccount;
    private boolean isLoggedIn;

    // Login check
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);

        // Controleer of de gebruiker is ingelogd
        if (isLoggedIn)
        {
            // Gebruiker is al ingelogd, ga naar de profielpagina
            goToProfilePage();
        } else
        {
            // Gebruiker is niet ingelogd, ga naar de inlogpagina
            goToLoginPage();
        }
    }

    private void goToProfilePage()
    {
        // Navigeer naar de profielpagina
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToLoginPage()
    {
        // Navigeer naar de inlogpagina
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

