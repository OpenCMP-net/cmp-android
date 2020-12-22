package com.example.inappcmp;

import android.app.Application;
import android.util.Log;

import com.opencmp.inapplib.OpenCmp;
import com.opencmp.inapplib.OpenCmpConfig;
import com.opencmp.inapplib.OpenCmpStore;
import com.opencmp.inapplib.Property;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // CMP initialisation
        OpenCmpConfig config = new OpenCmpConfig.Builder("traffective.com")
            // optional, if it is skipped the library takes default SharedPreference name
            .setStorageName("open_cmp.storage")
            // Optional value, null as default
            // Error handling
            .setErrorHandler(this::handleError)
            // Optional value, null as default
            // Check Consent Changes, you can extend interface OpenCmpStore
            .setChangesListener(this::consentChanged)
            .build();

        OpenCmp.initialize(this, config);
    }

    private void handleError(Exception e) {
        Log.e("OpenCmp", e.getMessage(), e);
    }

    private void consentChanged(OpenCmpStore store, Property property) {
        Log.i("OpenCmp", "Consent changed: " + property.name());
    }
}
