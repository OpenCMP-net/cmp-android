package com.example.inappcmp;

import android.app.Application;
import android.util.Log;

import com.opencmp.inapplib.OpenCmp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OpenCmp.initialize(this, "traffective.com", this::handleError);
    }

    private void handleError(Exception e) {
        Log.e("OpenCmp", e.getMessage(), e);
    }
}
