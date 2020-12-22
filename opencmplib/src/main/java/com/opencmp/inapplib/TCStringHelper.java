package com.opencmp.inapplib;

import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TCStringHelper {
    static Map<Property, Object> buildPreferences(ConsentString consentString) {
        // Preferences
        Map<Property, Object> preferences = new HashMap<>();
        preferences.put(Property.IABTCF_TCString, consentString.tcf);
        preferences.put(Property.IABTCF_CustomTCString, consentString.custom);
        preferences.put(Property.IABTCF_GoogleConsent, consentString.google);
        preferences.put(Property.OpenCmp_Meta, consentString.meta.toString());

        Iterator<String> prefIterator = consentString.preferences.keys();
        while (prefIterator.hasNext()) {
            String key = prefIterator.next();
            Property property = Property.valueOf(key);
            try {
                preferences.put(Property.valueOf(key), consentString.preferences.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (Property property : Property.values()) {
            Log.d("OpenCmp", property.name() + ": " + preferences.get(property));
        }
        return preferences;
    }
}
