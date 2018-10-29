package com.example.ssanusi.advert;

import android.app.Application;

    public class advertApp extends Application {

        private static advertApp instance;

        @Override
        public void onCreate() {
            super.onCreate();
           // AppRxPreference.setUpDefault(this);
            instance = this;
        }

        public static advertApp getInstance(){
            return instance;
        }
    }

