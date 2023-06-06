package com.example.miraclepack;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openEmailApp();
    }

    private void openEmailApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"voorbeeld@email.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hulp en ondersteuning | MiraclePack");

            startActivity(Intent.createChooser(intent, "Kies de app voor e-mail"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(SupportActivity.this, "Er is geen e-mailclient geïnstalleerd.", Toast.LENGTH_SHORT).show();
        }
    }
}
