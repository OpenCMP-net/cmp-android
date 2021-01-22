# Integration

## Voraussetzungen:
* Minimum API: 24
* Android Studio

## Struktur

Das Package enthaelt zwei Order:
- app: Beispiel-Integration
- opencmplib: Die CMP-Library, die in die App importiert werden muss
## Integration
Die Integration erfolgt in der Application Klasse:
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
## Features
### Button fuer nachtraegliche Einstellungen
Es kann ein Button konfiguriert werden, ueber den der User nachtraeglich das CMP oeffnen und Einstellungen aendern kann. Dazu muss ein Button mit einer bestimmten Id erstellt werden:
```
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@id/setup_button_id"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fab_margin"
        app:srcCompat="@android:drawable/ic_dialog_alert" />
```
### Zugriff auf Consent
Der Consent ist in den Default Shared Preferences gespeichert und kann direkt von dort gelesen und auch auf Aenderungen reagiert werden.
Die im Consent enthaltenen Key-Value-Paare entnehmen Sie bitte der Spezifikation der IAB:
https://github.com/InteractiveAdvertisingBureau/GDPR-Transparency-and-Consent-Framework/blob/master/TCFv2/IAB%20Tech%20Lab%20-%20CMP%20API%20v2.md#how-is-a-cmp-used-in-app 

#### Wie koennen Vendoren Aenderungen am Consent erkennen?
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
