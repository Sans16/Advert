package com.example.ssanusi.advert;

import android.app.Application;

import com.example.ssanusi.advert.utilities.AppPreference;

public class advertApp extends Application {

        private static advertApp instance;

        @Override
        public void onCreate() {
            super.onCreate();
            AppPreference.setUpDefault(this);
            instance = this;
        }
        public static advertApp getInstance(){
            return instance;
        }
    }

