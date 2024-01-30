package com.techrev.videocall.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {

    SharedPreferences pref;
    public static final String SharedpreferenceToken_ID = "eNotary";

    public MySharedPreference(Context context) {
        pref = context.getSharedPreferences(SharedpreferenceToken_ID, 0);
    }

    public void setBoolean(String key , Boolean check){
        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(key, check);
            editor.commit();
        } catch (Exception ex) {
            //Dataservice.insertStaticError(ex.getMessage(), "SetString", "sharedPreference");
            ex.printStackTrace();
        }
    }

    public void setString(String key, String value) {
        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception ex) {
            //Dataservice.insertStaticError(ex.getMessage(), "userID", "sharedPreference");
            ex.printStackTrace();
        }
    }

    public boolean getBoolean(String key) {
        boolean value = false;
        try {
            value = pref.getBoolean(key , false);
        } catch (Exception ex) {
            //Dataservice.insertStaticError(ex.getMessage(), "userID", "sharedPreference");
            ex.printStackTrace();
        }
        return value;
    }

    public String getString(String key) {
        String value = "";
        try {
            value = pref.getString(key, value);
        } catch (Exception ex) {
            //Dataservice.insertStaticError(ex.getMessage(), "userID", "sharedPreference");
            ex.printStackTrace();
        }
        return value;
    }

}
