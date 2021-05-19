package com.example.inappcmp;

import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AmpWebviewClient extends WebViewClient {

//    @Nullable
//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//        String url = request.getMethod(); // API 21
//        return super.shouldInterceptRequest(view, request);
//    }

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
        Toast.makeText(view.getContext(), description, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d("onPageFinished", url);
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
