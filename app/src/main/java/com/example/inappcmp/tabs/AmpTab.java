package com.example.inappcmp.tabs;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.inappcmp.AmpWebviewClient;
import com.example.inappcmp.R;
import com.opencmp.inapplib.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class AmpTab extends Fragment {
    View root;
    WebView myWebView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_amp, container, false);

        myWebView = (WebView) root.findViewById(R.id.ampWebview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        myWebView.setWebViewClient(new AmpWebviewClient());

        Button clearStorageButton = (Button) root.findViewById(R.id.clearStorageButton);
        clearStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearStorageAndReload();
            }
        });

        Button setCookieButton = (Button) root.findViewById(R.id.setCookieButton);
        setCookieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCookieAndReload();
            }
        });

        return root;
    }

    private void setCookieAndReload() {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(myWebView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(myWebView, true); // API 21
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        String consentString = getConsentString();
        cookieManager.setCookie("https://amp.opencmp.net", "euconsent-v2=" + consentString + "; path=/; domain=amp.opencmp.net; secure; SameSite=None");
        cookieSyncManager.sync();
        reload();
    }

    private void clearStorageAndReload() {
        WebStorage.getInstance().deleteAllData();
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null);
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(myWebView.getContext());
            cookieSyncMngr.startSync();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
        reload();
    }

    private void reload() {
        WebView myWebView = (WebView) root.findViewById(R.id.ampWebview);
        WebSettings webSettings = myWebView.getSettings();
        myWebView.clearHistory();
        myWebView.clearFormData();
        myWebView.clearCache(true);

        InputStream template = getResources().openRawResource(R.raw.amp);
        String html = StreamUtil.toString(template);
        try {
            template.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myWebView.loadDataWithBaseURL("https://traffective.com", html, "text/html; charset=utf-8", "UTF-8", "");
        Log.d(getClass().getName(), html);
    }

    private String getConsentString() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String consentString = prefs.getString("IABTCF_TCString", null);
        return consentString;
    }
}