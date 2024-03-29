package com.opencmp.inapplib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opencmp.inapplib.errors.CmpLoadingException;
import com.opencmp.inapplib.errors.HideUiException;
import com.opencmp.inapplib.errors.ShowUiException;
import com.opencmp.inapplib.util.StreamUtil;

import java.io.InputStream;
import java.util.Map;

/**
 * Diese Klasse initialisiert und betreibt die Webview, in dem das CMP laeuft.
 * Sie implementiert {@link JsProxyInterface}, um auf Aufrufe aus dem CMP heraus zu reagieren.
 */
public class OpenCmp implements JsProxyInterface {

    private final OpenCmpContext context;
    private final OpenCmpStore store;
    private final Context appContext;

    private boolean windowHasFocus = false;
    private boolean waitingForPopup = false;
    private OpenCmpWebView cmpView;
    private PopupWindow popupWindow;

    private OpenCmpErrorHandler errorHandler = error -> {
    };

    private Activity currentActivity;

    private static OpenCmp instance;

    public static void initialize(@NonNull Application app, @NonNull OpenCmpConfig config) {

        final OpenCmpContext cmpContext = new OpenCmpContext(config.domain);
        final String storeName = config.storageName != null ? config.storageName : PreferenceManager.getDefaultSharedPreferencesName(app);
        final SharedPreferences prefs = app.getSharedPreferences(storeName, Context.MODE_PRIVATE);
        final SharedPreferenceOpenCmpStore store = new SharedPreferenceOpenCmpStore(prefs, config.changesListener);

        final OpenCmp openCmp = new OpenCmp(app, cmpContext, store, config.errorHandler);

        WebView.setWebContentsDebuggingEnabled(true);

        app.registerActivityLifecycleCallbacks(new OpenCmpLifecycleListener(openCmp));

        instance = openCmp;
    }


    void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
        setupButtons();
    }

    void disableActivity(Activity activity) {
        if (currentActivity == activity)
            currentActivity = null;
    }

    /**
     * Das Anzeigen des Popup koennte passieren, bevor die App (Activity) den Focus hat. Das wuerde zu einer Exception fuehren, deshalb dieser Workaround.
     *
     * @param focus
     */
    public void setWindowHasFocus(boolean focus) {
        windowHasFocus = focus;
        if (focus && waitingForPopup) {
            waitingForPopup = false;
            try {
                showUi();
            } catch (Exception e) {
                errorHandler.onOpenCmpError(new ShowUiException(e));
            }
        }
    }

    private OpenCmp(
            Context appContext,
            OpenCmpContext context,
            OpenCmpStore store,
            @Nullable OpenCmpErrorHandler errorHandler
    ) {
        this.context = context;
        this.appContext = appContext;
        this.store = store;

        if (errorHandler != null)
            this.errorHandler = errorHandler;

        try {
            loadCmp();
        } catch (Exception e) {
            this.errorHandler.onOpenCmpError(e);
        }
    }


    private void setupButtons() {
        if (currentActivity == null) return;
        initSettingsButton(currentActivity.findViewById(R.id.setup_button_id));
        initClearButton(currentActivity.findViewById(R.id.clear_button_id));
    }

    /**
     * Button zum Loeschen des Consent initialisieren
     */
    private void initClearButton(View btnView) {
        if (btnView != null) {
            btnView.setOnClickListener(v -> store.clear());
        }
    }

    /**
     * Button zum Anzeigen des Popup initialisieren
     */
    private void initSettingsButton(View btnView) {
        if (btnView != null) {
            btnView.setOnClickListener(v -> triggerShowUi());
        }
    }

    /**
     * Zeigt das CMP an.
     */
    public static void show() {
        instance.triggerShowUi();
    }

    /**
     * Das CMP implementiert eine Schnittstelle, mit der man das Anzeigen triggern kann.
     * Dieser Aufruf (triggerShowUi) bewirkt, dass das CMP auf dem {@link JsProxy} die Methode showUi aufruft.
     */
    private void triggerShowUi() {
        cmpView.loadUrl("javascript:__tcfapi(\"showUi\", 2, function(){})");
    }

    /**
     * Erstellt eine Webview und laedt das CMP.
     *
     * @throws Exception
     */
    private void loadCmp() throws Exception {
        try {
            // Html laden
            InputStream template = appContext.getResources().openRawResource(R.raw.cmp);
            String html = StreamUtil.toString(template);
            template.close();
            // Variablen ersetzen
            html = html.replaceAll("\\$domain", context.getDomain());
            // in Webview oeffnen
            cmpView = new OpenCmpWebView(appContext);
            cmpView.addJavascriptInterface(new JsProxy(this, cmpView), "opencmpInAppProxy");

            cmpView.clearHistory();
            cmpView.clearFormData();
            cmpView.clearCache(true);

            cmpView.loadData(html, "text/html; charset=utf-8", "UTF-8");
        } catch (Exception e) {
            throw new CmpLoadingException(e);
        }
    }

    /**
     * Hilfsfunktion zum Anzeigen des Popup.
     *
     * @throws Exception
     */
    private void showUi() throws Exception {

        if (currentActivity == null || !windowHasFocus) {
            waitingForPopup = true;
            return;
        }

        popupWindow = new PopupWindow(appContext);

        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(cmpView);
        popupWindow.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
//        popupWindow.setHeight(1500);
        View view = currentActivity.getWindow().getDecorView();
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onReceiveConsentString(ConsentString consentString) {
        Log.d("ConsentString received:", consentString.toString());
        updateConsentStore(consentString);
    }

    @Override
    public ConsentString onRequestConsentString() {
        return store.getConsentString();
    }

    @Override
    public void onShowUi() {
        try {
            showUi();
        } catch (Exception e) {
            errorHandler.onOpenCmpError(new ShowUiException(e));
            e.printStackTrace();
        }
    }

    @Override
    public void onHideUi() {
        try {
            if (popupWindow != null)
                popupWindow.dismiss();

            popupWindow = null;
        } catch (Exception e) {
            errorHandler.onOpenCmpError(new HideUiException(e));
            e.printStackTrace();
        }
    }

    private void updateConsentStore(ConsentString consentString) {
        if (consentString != null) {
            Map<Property, Object> preferencesMap = TCStringHelper.buildPreferences(consentString);
            store.update(preferencesMap);
        }
    }
}
