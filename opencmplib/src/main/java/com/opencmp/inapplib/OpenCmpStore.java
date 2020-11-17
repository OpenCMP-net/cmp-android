package com.opencmp.inapplib;

import android.content.SharedPreferences;
import android.icu.lang.UProperty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.function.Predicate;

public class OpenCmpStore {
    public enum Property {
        IABTCF_CmpSdkID(Integer.class),
        IABTCF_CmpSdkVersion(Integer.class),
        IABTCF_PolicyVersion(Integer.class),
        IABTCF_gdprApplies(Integer.class),
        IABTCF_PublisherCC(String.class),
        IABTCF_UseNonStandardStacks(Integer.class),
        IABTCF_TCString(String.class),
        IABTCF_VendorConsents(String.class),
        IABTCF_VendorLegitimateInterests(String.class),
        IABTCF_PurposeConsents(String.class),
        IABTCF_PurposeLegitimateInterests(String.class),
        IABTCF_SpecialFeaturesOptIns(String.class),
        IABTCF_PublisherConsent(String.class),
        IABTCF_PublisherLegitimateInterests(String.class),
        IABTCF_PublisherCustomPurposesConsents(String.class),
        IABTCF_PublisherCustomPurposesLegitimateInterests(String.class),
        OpenCmp_Meta(String.class);


        private Class type;

        private Property(Class type) {
            this.type = type;
        }
    }

    SharedPreferences preferences;

    public OpenCmpStore(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        for (Property property : Property.values()) {
            editor.remove(property.name());
        }
        editor.commit();
    }

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
        editor.commit();
    }

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
