package com.example.miraclepack;

import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoginFragment extends Fragment {
    private TextView email;
    private TextView wachtwoord;
    private SQLiteDatabase database;
    private SessionManager sessionManager;

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
        sessionManager = new SessionManager(requireContext());

        email = view.findViewById(R.id.email);
        wachtwoord = view.findViewById(R.id.password);
        AppCompatButton buttonLogin = view.findViewById(R.id.buttonLogin);
        AppCompatButton buttonPasswordReset = view.findViewById(R.id.buttonPasswordReset);

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
                    Toast.makeText(requireContext(), "Login successvol", Toast.LENGTH_SHORT).show();

                    // Set the user as logged in
                    sessionManager.setLoggedIn(true);

                    // Navigate to the ProfileFragment after a successful login
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new ProfileFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    // Login failed
                    Toast.makeText(requireContext(), "Ongeldig emailadres of wachtwoord", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonPasswordReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Launch the PasswordResetActivity
                Intent intent = new Intent(requireContext(), PasswordResetActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkCredentials(String email, String wachtwoord) {
        // Query the database to check if the email and password exist
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {email, wachtwoord};
        Cursor cursor = database.query("users", null, selection, selectionArgs, null, null, null);
        boolean hasCredentials = cursor.moveToFirst();
        cursor.close();
        return hasCredentials;
    }

}
