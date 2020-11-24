package com.example.jetpackapplication;

import android.app.Application;
import android.content.Context;

import com.example.libnetwork.ApiService;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ApiService.init("http://123.56.232.18:8080/serverdemo", null);
    }
}
