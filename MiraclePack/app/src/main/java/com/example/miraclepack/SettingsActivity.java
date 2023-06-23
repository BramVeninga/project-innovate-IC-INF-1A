package com.example.miraclepack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button aboutUsButton;
    private Switch darkModeSwitch;
    private LinearLayout helpAndSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

//        aboutUsButton = findViewById(R.id.aboutUsButton);
//        darkModeSwitch = findViewById(R.id.switchDarkMode);
//        helpAndSupport = findViewById(R.id.helpAndSupport);

//        aboutUsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
//                startActivity(intent);
//            }
//        });

        helpAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipient = "info@miraclepack.nl";
                String subject = "Hulp en ondersteuning";
                composeEmail(recipient, subject);
            }
        });

        darkModeSwitch.setChecked(isDarkModeEnabled());
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveDarkModeEnabled(isChecked);
                if (isChecked) {
                    enableDarkMode();
                } else {
                    disableDarkMode();
                }
            }
        });

        if (isDarkModeEnabled()) {
            enableDarkMode();
        }
    }

    private boolean isDarkModeEnabled() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean("dark_mode_enabled", false);
    }

    private void saveDarkModeEnabled(boolean enabled) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dark_mode_enabled", enabled);
        editor.apply();
    }

    private void enableDarkMode() {
        setTheme(R.style.Theme_MiraclePack_Dark);
        // Pas hier extra instellingen toe voor dark mode indien nodig
    }

    private void disableDarkMode() {
        setTheme(R.style.Base_Theme_MiraclePack);
        // Pas hier extra instellingen toe om dark mode uit te schakelen indien nodig
    }

    private void composeEmail(String recipient, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + recipient));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(intent, "Kies een e-mailapplicatie"));
    }


}
