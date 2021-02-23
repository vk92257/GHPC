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

    public void setBackPressed(Boolean token) {
        editor.putBoolean("backPress", token);
        editor.commit();
    }

    public boolean isBackPressed() {
        return preferences.getBoolean("backPress", false);
    }



    public void setCurrentUser(int token) {
        editor.putInt("currentUser", token);
        editor.commit();
    }

    public int getCurrentUser() {
        return preferences.getInt("currentUser", -1);
    }

    public void setLoanStatus(String token) {
        editor.putString("loanStatus", token);
        editor.commit();
    }

    public String getLoanStatus() {
        return preferences.getString("loanStatus", "");
    }

    public void setUserProject(String token) {
        editor.putString("project", token);
        editor.commit();
    }

    public String getUserProject() {
        return preferences.getString("project", "");
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

    public String getUserSignature() {
        return preferences.getString("sign", "");
    }

    public void setUserSignature(String token) {
        editor.putString("sign", token);
        editor.commit();
    }

}