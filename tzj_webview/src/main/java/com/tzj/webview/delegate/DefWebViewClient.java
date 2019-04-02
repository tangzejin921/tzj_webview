package com.tzj.webview.delegate;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tzj.webview.TzjWebView;


/**
 *
 */
public abstract class DefWebViewClient extends WebViewClient{

    protected TzjWebView mWebView;

    public void setmWebView(TzjWebView mWebView) {
        this.mWebView = mWebView;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//        super.onReceivedSslError(view, handler, error);
    }
}
