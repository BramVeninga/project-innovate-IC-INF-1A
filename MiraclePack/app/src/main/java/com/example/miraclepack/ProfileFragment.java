package com.example.miraclepack;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ProfileFragment extends Fragment {

    private Button signOutButton;
    private Button passwordResetButton;
    private SessionManager sessionManager;
    private TextView emailTextView;
    private SQLiteDatabase database;


    public ProfileFragment() {
        // Empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sessionManager = new SessionManager(requireContext());
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        database = dbHelper.getReadableDatabase();


        signOutButton = view.findViewById(R.id.buttonSignOut);
        emailTextView = view.findViewById(R.id.EmailTextView);
        String loggedInEmail = getLoggedInEmail();
        emailTextView.setText(loggedInEmail);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the user as logged out
                sessionManager.setLoggedIn(false);

                Toast.makeText(requireContext(), "Succesvol uitgelogd", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.frame_layout, new LoginFragment())
                        .commit();
            }
        });

        passwordResetButton = view.findViewById(R.id.buttonPasswordReset);
        passwordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPasswordResetActivity();
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // Clear the back stack
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private String getLoggedInEmail() {
        return sessionManager.getEmail();
    }

//    private String getLoggedInEmail() {
//        String loggedInEmail = "";
//        String loggedInPassword = sessionManager.getPassword();
//        String emailColumn = DatabaseHelper.getColumnEmail();
//
//        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
//        SQLiteDatabase db = databaseHelper.getReadableDatabase();
//
//        String selection = "password = ?";
//        String[] selectionArgs = {loggedInPassword};
//
//        Cursor cursor = db.query("users", new String[]{emailColumn}, selection, selectionArgs, null, null, null);
//        if (cursor.moveToFirst()) {
//            int columnIndex = cursor.getColumnIndex(emailColumn);
//            if (columnIndex >= 0) {
//                loggedInEmail = cursor.getString(columnIndex);
//            }
//        }
//
//        cursor.close();
//        db.close();
//
//        return loggedInEmail;
//    }

    public void openLoginFragment(View view) {
        replaceFragment(new LoginFragment());
    }

    public void openPasswordResetActivity() {
        Intent intent = new Intent(getActivity(), PasswordResetActivity.class);
        startActivity(intent);
    }
}

