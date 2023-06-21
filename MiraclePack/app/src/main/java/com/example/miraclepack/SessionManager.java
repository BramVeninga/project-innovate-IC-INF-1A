package com.example.miraclepack;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "session_pref";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_SELECTED_DATE = "selected_date";
    private static final String KEY_PHONENUMBER = "phonenumber";
    private static final String KEY_USERNAME = "username";




    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public void setSelectedDate(String selectedDate) {
        editor.putString(KEY_SELECTED_DATE, selectedDate);
        editor.apply();
    }

    public String getSelectedDate() {
        return sharedPreferences.getString(KEY_SELECTED_DATE, "");
    }

    public void setPhonenumber(String phonenumber) {
        editor.putString(KEY_PHONENUMBER, phonenumber);
        editor.apply();
    }

    public String getPhonenumber() {
        return sharedPreferences.getString(KEY_PHONENUMBER, "");
    }

    public void setUsername (String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

}
