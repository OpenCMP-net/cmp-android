#Integration

##Requirements:
* Minimum API: 24
* Android Studio

##Source Structure
The sources contain two folders:
- opencmplib: The library itself, that has to be imported into the consuming project
- app: Integration demo

##Integration
The integration can be done in the class Application:
```
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
        OpenCmpConfig config = new OpenCmpConfig.Builder("domain.de")
            // Error handling
            .setErrorHandler(this::handleError)
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
```

##Features

###Button for changing consent settings
A button can be configured to enable the user to open the CMP UI again to make changes with the previously stored consent.
To display the button a specific Id has to be added to the layout:
```
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@id/setup_button_id"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fab_margin"
        app:srcCompat="@android:drawable/ic_dialog_alert" />
```
###How to access the consent
The consent is stored in the Shared Preferences und can be read directly from there. The app can also listen and react to changes to the consent.

Please read the IAB specs for the specific names of the key value pairs that are stored:
https://github.com/InteractiveAdvertisingBureau/GDPR-Transparency-and-Consent-Framework/blob/master/TCFv2/IAB%20Tech%20Lab%20-%20CMP%20API%20v2.md#how-is-a-cmp-used-in-app 

####How can vendors listen to changes of the consent?
```
Context mContext = getApplicationContext();
SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
SharedPreferences.OnSharedPreferenceChangeListener mListener;
mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
                        if (key.equals([Specific Consent Key])) {
                                   // Update Consent settings
                                   }
                        }
            };


mPreferences.registerOnSharedPreferenceChangeListener(mListener);
```