package com.opencmp.inapplib;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SharedPreferenceOpenCmpStore implements OpenCmpStore {

    private final SharedPreferences preferences;

    private final OnConsentChangedListener listener;

    @SuppressWarnings("FieldCanBeLocal")
    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public SharedPreferenceOpenCmpStore(SharedPreferences preferences, @Nullable OnConsentChangedListener changesListener) {
        this.preferences = preferences;
        preferenceChangeListener = createListener();
        listener = changesListener;

        if (listener != null)
            preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

    }

    private SharedPreferences.OnSharedPreferenceChangeListener createListener() {
        return new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                //Log.i("OpenCmp", "Preferences changed:" + key);

                try {
                    Property property = Property.valueOf(key);
                    List<Property> properties = Arrays.asList(Property.values());
                    if (properties.contains(property)) {
                        listener.onConsentChanged(SharedPreferenceOpenCmpStore.this, property);
                    }
                } catch (Throwable error) {
                    Log.w("OpenCmp", "Incorrect property name", error);
                }
            }
        };
    }

    @Override
    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        for (Property property : Property.values()) {
            editor.remove(property.name());
        }
        editor.apply();
    }

    @Override
    public void update(Map<Property, Object> values) {
        SharedPreferences.Editor editor = preferences.edit();
        for (Map.Entry<Property, Object> entry : values.entrySet()) {
            Object value = entry.getValue();
            Property property = entry.getKey();
            String key = property.name();
            if (value == null) {
                editor.remove(key);
            } else if (property.type == Integer.class) {
                editor.putInt(key, (Integer) value);
            } else if (property.type == String.class) {
                editor.putString(key, (String) value);
            }
        }
        editor.apply();
    }

    @Override
    public ConsentString getConsentString() {
        ConsentString cs = new ConsentString();
        cs.tcf = preferences.getString(Property.IABTCF_TCString.name(), null);
        String metaJson = preferences.getString(Property.OpenCmp_Meta.name(), (new JSONObject()).toString());
        try {
            cs.meta = new JSONObject(metaJson);
        } catch (JSONException e) {
            e.printStackTrace();
            cs.meta = new JSONObject();
        }
        return cs;
    }

}
