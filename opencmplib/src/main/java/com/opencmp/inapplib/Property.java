package com.opencmp.inapplib;

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
    OpenCmp_Meta(String.class),
    IABTCF_CustomVendorConsents(String.class),
    IABTCF_CustomVendorLegitimateInterests(String.class),
    // Custom
    IABTCF_CustomTCString(String.class),
    IABTCF_GoogleConsent(String.class);

    final Class<?> type;

    Property(Class<?> type) {
        this.type = type;
    }
}
