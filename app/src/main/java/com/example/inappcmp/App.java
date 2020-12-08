package com.example.inappcmp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.opencmp.inapplib.OpenCmp;
import com.opencmp.inapplib.OpenCmpStore;

public class App extends Application {
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    @Override
    public void onCreate() {
        super.onCreate();

        // CMP initialisation
        OpenCmp.initialize(this, "traffective.com", this::handleError);

        // Check Consent Changes
        App app = this;
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i("OpenCmp", "Preferences changed:" + key);
                if (key.equals(OpenCmpStore.Property.IABTCF_TCString.name())) {
                    app.consentChanged();
                }
            }
        };
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(prefListener);
    }

    private void handleError(Exception e) {
        Log.e("OpenCmp", e.getMessage(), e);
    }

    private void consentChanged() {
        Log.i("OpenCmp", "Consent changed");
    }
}
