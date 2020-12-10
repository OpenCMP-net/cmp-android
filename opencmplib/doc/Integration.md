#Integration

##Voraussetzungen:
* Minimum API: 24

##Integration
Die Integration erfolgt in der Application Klasse:
```
package com.example.inappcmp;

import com.opencmp.inapplib.OpenCmp;
import com.opencmp.inapplib.OpenCmpStore;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // CMP initialisation
        OpenCmp.initialize(this, "domain.com", this::handleError);
    }

    private void handleError(Exception e) {
        Log.e("OpenCmp", e.getMessage(), e);
    }
}

```
##Features
###Button fuer nachtraegliche Einstellungen
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
###Reagieren auf Erstellung des/Aenderung beim Consent
Der Consent wird in den Shared Preferences abgelegt. Jede Aenderung am Consent kann ueber einen Listener durch den Publisher oder die Vendoren abgefragt werden.

Integration mit Listener:
```
public class App extends Application {
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    @Override
    public void onCreate() {
        super.onCreate();

        // CMP initialisation
        OpenCmp.initialize(this, "domain.com", this::handleError);

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
```
Die Auflistung der im Consent enthaltenen Informationen finden Sie hier: 