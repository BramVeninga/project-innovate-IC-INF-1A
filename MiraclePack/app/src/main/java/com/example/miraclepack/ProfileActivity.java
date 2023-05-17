package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity
{
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);


        // Controleer of de gebruiker is ingelogd
        if (isLoggedIn)
        {
            // Gebruiker is ingelogd, toon de profielpagina
            setContentView(R.layout.fragment_profile);
        }else
        {
            // Gebruiker is niet ingelogd, ga naar de inlogpagina
            setContentView(R.layout.fragment_loginpage);
        }
    }


}
