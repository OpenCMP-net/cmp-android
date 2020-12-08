package com.opencmp.inapplib;

@FunctionalInterface
public interface OnConsentChangedListener {
    void onConsentChanged(OpenCmpStore store, Property property);
}
