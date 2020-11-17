package com.opencmp.inapplib;

import org.json.JSONObject;

public interface JsProxyInterface {
    public void onReceiveConsentString(ConsentString consentString);
    public ConsentString onRequestConsentString();
    public void onShowUi();

    public void onHideUi();
}
