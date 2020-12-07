package com.example.inappcmp;

import android.app.Application;
import android.util.Log;

import com.opencmp.inapplib.OpenCmp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.opencmp.inapplib.OpenCmpStore;

public class App extends Application {
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    @Override
    public void onCreate() {
        super.onCreate();

        OpenCmp.initialize(this, "traffective.com", this::handleError);

        // Check Consent Changes
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i("OpenCmp", "Preferences changed:" + key);
                if (key.equals(OpenCmpStore.Property.IABTCF_TCString.name())) {
                    App.this.consentChanged();
                }
            }
        };
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(prefListener);
    }

    private void consentChanged() {
        Log.i("OpenCmp", "Consent changed");
    }

    private void handleError(Exception e) {
        Log.e("OpenCmp", e.getMessage(), e);
    }
}