package com.opencmp.inapplib;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OpenCmpWebviewClient extends WebViewClient {

    @Override
    public void onLoadResource(WebView view, String url) {
        Log.i("Loading URL:", url);
//        view.loadUrl(url);
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("url", url);
        // Url in externem Browser laden
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(i);
//        view.loadUrl(url);
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Log.w("SSL error:", error.getUrl());
        Toast.makeText(view.getContext(), "SSL error occured", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.w("onReceivedError", description);
        Toast.makeText(view.getContext(), description, Toast.LENGTH_SHORT).show();
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
