package com.opencmp.inapplib;

import java.util.Map;

public interface OpenCmpStore {
    void clear();
    void update(Map<Property, Object> values);
    ConsentString getConsentString();
}
