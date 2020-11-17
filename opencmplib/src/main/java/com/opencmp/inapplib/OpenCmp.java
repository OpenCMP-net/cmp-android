package com.opencmp.inapplib;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.PopupWindow;

import com.opencmp.inapplib.util.StreamUtil;

import java.io.InputStream;
import java.util.Map;

/**
 * Diese Klasse initialisiert und betreibt die Webview, in dem das CMP laeuft.
 * Sie implementiert {@link JsProxyInterface}, um auf Aufrufe aus dem CMP heraus zu reagieren.
 */
public class OpenCmp implements JsProxyInterface {
    private static OpenCmp instance;
    private OpenCmpContext context;
    private WebView cmpView;
    private boolean windowHasFocus = false;
    private boolean waitingForPopup = false;
    private OpenCmpStore store;
    PopupWindow popupWindow;

    /**
     * Das Anzeigen des Popup koennte passieren, bevor die App (Activity) den Focus hat. Das wuerde zu einer Exception fuehren, deshalb dieser Workaround.
     *
     * @param focus
     * @throws Exception
     */
    public void setWindowHasFocus(boolean focus) throws Exception {
        windowHasFocus = focus;
        if (focus && waitingForPopup) {
            waitingForPopup = false;
            showUi();
        }
    }

    private OpenCmp(OpenCmpContext context) {
        this.context = context;
    }

    /**
     * Initialisiert das CMP
     *
     * @throws Exception
     */
    private void init() throws Exception {
        store = new OpenCmpStore(PreferenceManager.getDefaultSharedPreferences(this.context.context));
        loadCmp();
        initSettingsButton();
        initClearButton();
    }

    /**
     * Button zum Loeschen des Consent initialisieren
     */
    private void initClearButton() {
        if (context.clearButton == null) {
            return;
        }
        context.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    store.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Button zum Anzeigen des Popup initialisieren
     */
    private void initSettingsButton() {
        if (context.settingsButton == null) {
            return;
        }
        context.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    triggerShowUi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Das CMP implementiert eine Schnittstelle, mit der man das Anzeigen triggern kann.
     * Dieser Aufruf (triggerShowUi) bewirkt, dass das CMP auf dem {@link JsProxy} die Methode showUi aufruft.
     */
    private void triggerShowUi() {
        cmpView.loadUrl("javascript:__tcfapi(\"showUi\", 2, function(){})");
//        cmpView.evaluateJavascript("__tcfapi(\"showUi\", 2, function(){})", value -> {
//
//        });
    }

    /**
     * Erstellt eine Webview und laedt das CMP.
     *
     * @throws Exception
     */
    private void loadCmp() throws Exception {
        try {
            // Html laden
            InputStream template = context.context.getResources().openRawResource(R.raw.cmp);
            String html = StreamUtil.toString(template);
            template.close();
            // Variablen ersetzen
            html = html.replaceAll("\\$domain", context.domain);
            // in Webview oeffnen
            cmpView = new OpenCmpWebView(context.context);
            cmpView.addJavascriptInterface(new JsProxy(this), "opencmpInAppProxy");
            cmpView.loadData(html, "text/html; charset=utf-8", "UTF-8");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Initialisiert die Lib. Im Prinzip ist das ein Singleton, kann aber gerne geaendert werden.
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static OpenCmp setup(OpenCmpContext context) throws Exception {
        OpenCmp openCmp = getInstance(context);
        openCmp.init();
        return openCmp;
    }

    private static OpenCmp getInstance(OpenCmpContext context) {
        if (instance == null) {
            instance = new OpenCmp(context);
        }
        return instance;
    }

    /**
     * Hilfsfunktion zum Anzeigen des Popup.
     *
     * @throws Exception
     */
    private void showUi() throws Exception {
        if (this.windowHasFocus == false) {
            waitingForPopup = true;
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.opencmp_popup, null);
        popupWindow = new PopupWindow(context.activity);
//        View popupView = android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(cmpView);
        popupWindow.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(1500);
        View view = context.activity.getWindow().getDecorView();
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

    }

    @Override
    public void onReceiveConsentString(ConsentString consentString) {
        Log.d("ConsentString received:", consentString.toString());
        updateConsentStore(consentString);
    }

    @Override
    public ConsentString onRequestConsentString() {
        ConsentString consentString = store.getConsentString();
        return consentString;
    }

    @Override
    public void onShowUi() {
        try {
            showUi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHideUi() {
        try {
            if (popupWindow == null) {
                return;
            }
            popupWindow.dismiss();
            popupWindow = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateConsentStore(ConsentString consentString) {
        if (consentString == null) {
            return;
        }
        Map<OpenCmpStore.Property, Object> preferencesMap = TCStringHelper.buildPreferences(consentString);
        store.update(preferencesMap);
    }
}
