package com.example.inappcmp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.opencmp.inapplib.OpenCmp;
import com.opencmp.inapplib.OpenCmpConfig;
import com.opencmp.inapplib.OpenCmpStore;
import com.opencmp.inapplib.Property;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Clear Consent
        clearConsent();

        // CMP initialisation
        OpenCmpConfig config = new OpenCmpConfig.Builder("traffective.com")
                // Error handling
                .setErrorHandler(this::handleError)
                // Check Consent Changes, you can extend interface OpenCmpStore
                .setChangesListener(this::consentChanged)
                .build();

        OpenCmp.initialize(this, config);
    }

    private void clearConsent() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().clear().commit();
    }

    private void handleError(Exception e) {
        Log.e("OpenCmp", e.getMessage(), e);
    }

    private void consentChanged(OpenCmpStore store, Property property) {
        Log.i("OpenCmp", "Consent changed: " + property.name());
    }
}
