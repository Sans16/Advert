package com.example.ssanusi.advert.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.ssanusi.advert.BuildConfig;

public class AppPreference {
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences setUpDefault(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences;
    }


    public static final String USER_TOKEN = "user_token";

    public static void setUserToken(String token){
        if (sharedPreferences!=null){
            sharedPreferences.edit().putString(USER_TOKEN,token).apply();
        }
    }

    public static String getUserToken(){
        if (sharedPreferences!=null){
           return sharedPreferences.getString(USER_TOKEN, BuildConfig.FLAVOR);
        }
       return BuildConfig.FLAVOR;
    }
}
