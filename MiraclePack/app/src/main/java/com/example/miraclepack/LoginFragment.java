package com.example.miraclepack;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.miraclepack.DatabaseHelper;
import com.example.miraclepack.R;

public class LoginFragment extends Fragment {
    private TextView email;
    private TextView wachtwoord;
    private SQLiteDatabase database;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loginpage, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.email);
        wachtwoord = view.findViewById(R.id.password);
        AppCompatButton buttonLogin = view.findViewById(R.id.buttonLogin);

        // Initialize database
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        database = dbHelper.getReadableDatabase();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString();
                String enteredWachtwoord = wachtwoord.getText().toString();

                // Check credentials in the database
                if (checkCredentials(enteredEmail, enteredWachtwoord)) {
                    // Successful login
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();
                    // Proceed to the next activity or perform other actions
                } else {
                    // Login failed
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
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
        cursor.close();
        return hasCredentials;
    }
}
