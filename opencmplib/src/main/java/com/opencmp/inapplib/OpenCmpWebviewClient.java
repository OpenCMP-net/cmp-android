package com.opencmp.inapplib;

import android.content.Context;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OpenCmpWebviewClient extends WebViewClient {
    private Context context;

    public OpenCmpWebviewClient(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        Log.i("Loading URL:", url);
//        view.loadUrl(url);
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("url", url);
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Log.w("SSL error:", error.getUrl());
        handler.proceed(); // Ignore SSL certificate errors
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.w("onReceivedError", description);
        Toast.makeText(this.context, description, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d("onPageFinished", url);
        view.loadUrl("javascript:window.opencmpInAppProxy.debugHtml(window.document.body.innerHTML);");
    }



/*    private static void evaluateJavascript(WebView view){
        view.evaluateJavascript("window.document.body.innerHTML", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String html) {
                Log.d("HTML", html);
                // code here
            }
        });
    }*/
}
