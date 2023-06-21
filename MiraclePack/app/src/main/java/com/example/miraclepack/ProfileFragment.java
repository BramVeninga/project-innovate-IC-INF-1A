package com.example.miraclepack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
import java.util.Locale;


public class ProfileFragment extends Fragment {

    private Button signOutButton;
    private Button passwordResetButton;
    private SessionManager sessionManager;
    private TextView emailTextView;
    private TextView birthdateTextView;
    private EditText phonenumberEditText;
    private EditText usernameEditText;
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
        birthdateTextView = view.findViewById(R.id.birthdateTextView);
        phonenumberEditText = view.findViewById(R.id.phonenumberEditText);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordResetButton = view.findViewById(R.id.buttonPasswordReset);

        usernameEditText.setSingleLine(true);
        String loggedInEmail = getLoggedInEmail();
        emailTextView.setText(loggedInEmail);

        // Get selected date
        String selectedDate = sessionManager.getSelectedDate();
        if (!selectedDate.isEmpty()) {
            birthdateTextView.setText(selectedDate);
        }
        // Get phone number
        String phonenumber = sessionManager.getPhonenumber();
        if (!phonenumber.isEmpty()) {
            phonenumberEditText.setText(phonenumber);
        }
        // Get Username
        String username = sessionManager.getUsername();
        if (!username.isEmpty()) {
            usernameEditText.setText(username);
        }

        // Only allow numbers for the phone number
        phonenumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        phonenumberEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

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

        passwordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPasswordResetActivity();
            }
        });

        birthdateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create and show the date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), datePickerListener, year, month, day);
                datePickerDialog.show();
            }
        });
        // Save the phone number in the sessionManager
        phonenumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Save phone number
                sessionManager.setPhonenumber(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Save username
                sessionManager.setUsername(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
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

    public void openLoginFragment(View view) {
        replaceFragment(new LoginFragment());
    }

    public void openPasswordResetActivity() {
        Intent intent = new Intent(getActivity(), PasswordResetActivity.class);
        startActivity(intent);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Update the date of birth TextView with the selected date
            String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year);
            birthdateTextView.setText(selectedDate);

            // Save the selected date
            sessionManager.setSelectedDate(selectedDate);
        }
    };

}

