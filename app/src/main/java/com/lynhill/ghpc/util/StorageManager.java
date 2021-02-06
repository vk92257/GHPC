package com.lynhill.ghpc.util;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageManager {

    private static StorageManager instance;
    public static final String PREF_NAME = "app_settings";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static StorageManager getInstance(Context context) {
        if (instance == null)
            instance = new StorageManager(context.getApplicationContext());
        return instance;
    }

    private StorageManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void setUserAddress(String token) {
        editor.putString("address", token);
        editor.commit();
    }
    public String getUserAddress() {
        return preferences.getString("address", "");
    }
    public void setUserPhoneDOB(String token) {
        editor.putString("dob", token);
        editor.commit();
    }
    public String getUserDOB() {
        return preferences.getString("dob", "");
    }
    public void setUserName(String token) {
        editor.putString("username", token);
        editor.commit();
    }
    public String getUserName() {
        return preferences.getString("username", "");
    }
    public String getUserEmail() {
        return preferences.getString("email", "");
    }
    public void setUserEmail(String token) {
        editor.putString("email", token);
        editor.commit();
    }
    public String getUserPhoneNumber() {
        return preferences.getString("phone", "");
    }
    public void setUserPhoneNumber(String token) {
        editor.putString("phone", token);
        editor.commit();
    }


}