package com.opencmp.inapplib;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

public class OpenCmpWebView extends WebView {
    public OpenCmpWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public OpenCmpWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenCmpWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        setWebViewClient(new OpenCmpWebviewClient());
//        setWebChromeClient(new WebChromeClient());
        setBackgroundColor(Color.TRANSPARENT);
    }

    public void resolvePromise(String promiseId, JSONObject jsonObject) {
        WebView webView = this;
        // The call to JS must be executed in the UI thread
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:window.trfCmpResolvePromise('" + promiseId + "', " + jsonObject.toString() + ")");
            }
        });
    }
}
