package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.miraclepack.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private boolean isLoggedIn = false;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        // Check if the user is logged in
        isLoggedIn = checkIfLoggedIn();
        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            replaceFragment(new HomeFragment());
        } else {
            replaceFragment(new LoginFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.bag) {
                replaceFragment(new BagFragment());
            } else if (itemId == R.id.profile) {
                if (sessionManager.isLoggedIn()) {
                    replaceFragment(new ProfileFragment());
                } else {
                    replaceFragment(new LoginFragment()); // Show the LoginFragment for the user to log in
                }
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });

        // Background location service
        Intent serviceIntent = new Intent(this, AppService.class);
        startService(serviceIntent);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        if (fragment instanceof HomeFragment) {
            setTitle("Miracle-Pack");
        } else if (fragment instanceof BagFragment) {
            setTitle("Tas");
        } else if (fragment instanceof LoginFragment) {
            setTitle("Inhoud van de tas");
        } else if (fragment instanceof AboutFragment) {
            setTitle("Over ons");
        }
    }

    public void openSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private boolean checkIfLoggedIn() {
        // Add your logic here to check if the user is logged in
        return false;
    }
}
