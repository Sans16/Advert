package com.example.ssanusi.advert.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.ssanusi.advert.BuildConfig;
import com.example.ssanusi.advert.model.RegistrationRequest;

public class AppPreference {
    private static SharedPreferences sharedPreferences;
    public static final String USER_TOKEN = "user_token";
    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_LAST_NAME = "lastName";
    public static final String USER_EMAIL = "email";
    public static final String USER_PHONE = "phone";
    public static final String IS_LOGGED_IN = "is_logged_in";

    public static SharedPreferences setUpDefault(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences;
    }

    public static void setUserToken(String token) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(USER_TOKEN, token).apply();
        }
    }

    public static String getUserToken() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(USER_TOKEN, BuildConfig.FLAVOR);
        }
        return BuildConfig.FLAVOR;
    }

    public static void setUserDetails(RegistrationRequest userDetails) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_EMAIL, userDetails.getEmail());
            editor.putString(USER_PHONE, userDetails.getPhonenumber());
            editor.putString(USER_FIRST_NAME, userDetails.getFirstname());
            editor.putString(USER_LAST_NAME, userDetails.getLastname());
            editor.apply();
        }
    }

    public static RegistrationRequest getUserDetails() {
        RegistrationRequest userDetails = new RegistrationRequest();
        if (sharedPreferences != null) {
            userDetails.setEmail(sharedPreferences.getString(USER_EMAIL, BuildConfig.FLAVOR));
            userDetails.setFirstname(sharedPreferences.getString(USER_FIRST_NAME, BuildConfig.FLAVOR));
            userDetails.setLastname(sharedPreferences.getString(USER_LAST_NAME, BuildConfig.FLAVOR));
            userDetails.setPhonenumber(sharedPreferences.getString(USER_PHONE, BuildConfig.FLAVOR));
            return userDetails;
        }
        return null;
    }

     public static void setIsLoggedIn(boolean status){
        if (sharedPreferences!=null){
            sharedPreferences.edit().putBoolean(IS_LOGGED_IN,status).apply();
        }
     }

     public static boolean isLoggedIN(){
        return  sharedPreferences!=null && sharedPreferences.getBoolean(IS_LOGGED_IN,false);
     }
}
