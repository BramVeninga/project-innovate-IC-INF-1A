package com.example.miraclepack;

import com.example.miraclepack.DatabaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity
{
    private TextView email;
    private TextView wachtwoord;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loginpage);

        TextView email = findViewById(R.id.email);
        TextView wachtwoord = findViewById(R.id.password);
        MaterialButton buttonLogin = findViewById(R.id.buttonLogin);

        // Initialize database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString();
                String enteredWachtwoord = wachtwoord.getText().toString();

                // Check credentials in the database
                if (checkCredentials(enteredEmail, enteredWachtwoord)) {
                    // Successful login
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Proceed to the next activity or perform other actions
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkCredentials(String email, String wachtwoord) {
        // Query the database to check if the email and password exist
        String selection = "email = ? AND wachtwoord = ?";
        String[] selectionArgs = {email, wachtwoord};
        Cursor cursor = database.query("users", null, selection, selectionArgs, null, null, null);
        boolean hasCredentials = cursor.moveToFirst();
        ((Cursor) cursor).close();
        return hasCredentials;
    }
}


