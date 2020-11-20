package com.opencmp.inapplib;

public interface JsProxyInterface {
    void onReceiveConsentString(ConsentString consentString);
    ConsentString onRequestConsentString();

    void onShowUi();
    void onHideUi();
}
