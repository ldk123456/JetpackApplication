package com.example.jetpackapplication;

import android.app.Application;
import android.content.Context;

import com.example.libnetwork.ApiService;

public class MainApplication extends Application {
    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();

        ApiService.init("http://123.56.232.18:8080/serverdemo", null);
    }

    public static Context applicationContext() {
        return sAppContext;
    }
}
