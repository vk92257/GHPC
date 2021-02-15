package com.lynhill.ghpc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lynhill.ghpc.pojo.Representatives;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {


//    public static void saveArrayList(Context c, ArrayList<Representatives> list, String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
//        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        editor.remove(key); //removing older value first
//        editor.putString(key, json);
//        editor.apply();
//    }
    public static void saveStringArrayList(Context c, ArrayList<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.remove(key); //removing older value first
        editor.putString(key, json);
        editor.apply();
    }

    public static void saveArrayList(Context c, ArrayList<Representatives> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.remove(key); //removing older value first
        editor.putString(key, json);
        editor.apply();
    }

    public static ArrayList<Representatives> getArrayList(Context context, String key) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = prefs.getString(key, null);
            Type arrayListType = new TypeToken<ArrayList<Representatives>>(){}.getType();
            ArrayList<Representatives> yourList = gson.fromJson(prefs.getString(key,""), arrayListType);

//            Type type = new TypeToken<ArrayList<String>>() {
//
//            }.getType();
//            return gson.fromJson(json, type);
//        } catch (Exception e) {
//            return new ArrayList<Representatives>();
            return yourList;
        }catch (Exception e){
            return null;
        }

    }


    public static ArrayList<String> getStringArrayList(Context context, String key) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = prefs.getString(key, null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }
//    public static ArrayList<Representatives> getArrayList(Context context, String key) {
//        try {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            Gson gson = new Gson();
//            String json = prefs.getString(key, null);
//            Type type = new TypeToken<ArrayList<String>>() {
//            }.getType();
//            return gson.fromJson(json, type);
//        } catch (Exception e) {
//            return new ArrayList<Representatives>();
//        }
//    }

}
