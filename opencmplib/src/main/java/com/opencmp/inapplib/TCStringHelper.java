package com.opencmp.inapplib;

import android.util.Log;

import com.iabtcf.decoder.TCString;
import com.iabtcf.utils.IntIterable;

import java.util.HashMap;
import java.util.Map;

public class TCStringHelper {
    static Map<OpenCmpStore.Property, Object> buildPreferences(ConsentString consentString) {
        String tcf = consentString.tcf;
        TCString tcString = TCString.decode(tcf);
        // IAB
        Map<OpenCmpStore.Property, Object> preferences = new HashMap<>();
        preferences.put(OpenCmpStore.Property.IABTCF_TCString, tcf);
        preferences.put(OpenCmpStore.Property.IABTCF_CmpSdkID, tcString.getCmpId());
        preferences.put(OpenCmpStore.Property.IABTCF_CmpSdkVersion, tcString.getCmpVersion());
        preferences.put(OpenCmpStore.Property.IABTCF_PolicyVersion, tcString.getTcfPolicyVersion());
        preferences.put(OpenCmpStore.Property.IABTCF_gdprApplies, 1);
        preferences.put(OpenCmpStore.Property.IABTCF_PublisherCC, tcString.getPublisherCC());
        preferences.put(OpenCmpStore.Property.IABTCF_UseNonStandardStacks, 0);
        preferences.put(OpenCmpStore.Property.IABTCF_VendorConsents, toBinaryString(tcString.getVendorConsent()));
        preferences.put(OpenCmpStore.Property.IABTCF_VendorLegitimateInterests, toBinaryString(tcString.getVendorLegitimateInterest()));
        preferences.put(OpenCmpStore.Property.IABTCF_PurposeConsents, toBinaryString(tcString.getPurposesConsent()));
        preferences.put(OpenCmpStore.Property.IABTCF_PurposeLegitimateInterests, toBinaryString(tcString.getPurposesLITransparency()));
        preferences.put(OpenCmpStore.Property.IABTCF_SpecialFeaturesOptIns, toBinaryString(tcString.getSpecialFeatureOptIns()));
        preferences.put(OpenCmpStore.Property.IABTCF_PublisherConsent, toBinaryString(tcString.getPubPurposesConsent()));
        preferences.put(OpenCmpStore.Property.IABTCF_PublisherLegitimateInterests, toBinaryString(tcString.getPubPurposesLITransparency()));
        preferences.put(OpenCmpStore.Property.IABTCF_PublisherCustomPurposesConsents, toBinaryString(tcString.getCustomPurposesConsent()));
        preferences.put(OpenCmpStore.Property.IABTCF_PublisherCustomPurposesLegitimateInterests, toBinaryString(tcString.getCustomPurposesLITransparency()));
        preferences.put(OpenCmpStore.Property.OpenCmp_Meta, consentString.meta.toString());
        // Custom
        TCString customString = TCString.decode(consentString.custom);
        preferences.put(OpenCmpStore.Property.IABTCF_CustomVendorConsents, toBinaryString(customString.getVendorConsent()));
        preferences.put(OpenCmpStore.Property.IABTCF_CustomVendorLegitimateInterests, toBinaryString(customString.getVendorLegitimateInterest()));

        for (OpenCmpStore.Property property : OpenCmpStore.Property.values()) {
            Log.d("OpenCmp", property.name() + ": " + preferences.get(property));
        }
        return preferences;
    }

    private static int getMax(IntIterable bits) {
        int max = 0;
        for (Integer bit : bits) {
            if (bit > max) {
                max = bit;
            }
        }
        return max;
    }

    private static String toBinaryString(IntIterable bits) {
        char[] binaryString = new char[getMax(bits)];
        for (int i = 0; i < binaryString.length; i++) {
            if (bits.contains(i + 1)) {
                binaryString[i] = '1';
            } else {
                binaryString[i] = '0';
            }
        }
        return new String(binaryString);
    }
}
