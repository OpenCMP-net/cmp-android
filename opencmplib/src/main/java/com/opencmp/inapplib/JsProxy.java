package com.opencmp.inapplib;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class JsProxy {
    /**
     * Handler fuer Callbacks
     */
    private final JsProxyInterface handler;
    private final OpenCmpWebView webView;

    public JsProxy(JsProxyInterface handler, OpenCmpWebView webView) {
        this.handler = handler;
        this.webView = webView;
    }

    /**
     * CMP holt sich den Consent aus SharedPreferences
     *
     * @return
     * @throws JSONException
     */
    @JavascriptInterface
    public void getConsent(String promiseId) throws JSONException {
        Log.d("JsProxy", "getConsent " + promiseId);
        JSONObject json = this.handler.onRequestConsentString().toJson();
        webView.resolvePromise(promiseId, json);
    }

    /**
     * CMP speichert den Consent in SharedPreferences
     *
     * @param consentStringJson
     * @throws JSONException
     */
    @JavascriptInterface
    public void setConsent(String consentStringJson) throws JSONException {
        Log.d("setConsent", consentStringJson);
        JSONObject json = new JSONObject(consentStringJson);
        this.handler.onReceiveConsentString(ConsentString.fromJson(json));
    }

    /**
     * Reine Testmethode
     *
     * @param html
     */
    @JavascriptInterface
    public void debugHtml(String html) {
        Log.d("debugHtml", html);
    }

    /**
     * CMP will Popup anzeigen
     */
    @JavascriptInterface
    public void showUi() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                handler.onShowUi();
            }
        });
    }

    /**
     * CMP will Popup schliessen
     */
    @JavascriptInterface
    public void hideUi() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                handler.onHideUi();
            }
        });
    }
}
