package com.example.inappcmp;

import android.app.Application;

import com.opencmp.inapplib.OpenCmp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OpenCmp.initialize(this, "traffective.com");
    }
}
