package com.tzj.webview.delegate;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.tzj.webview.TzjWebView;
import com.tzj.webview.utils.UtilWebView;

/**
 * 断网
 */
public class NetErrDelegate extends DefWebViewClient{
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (!UtilWebView.isNetConnected(view.getContext())){
            if (!TzjWebView.NET_ERR_HTML.equals(mWebView.getUrl())){
                mWebView.loadUrl(TzjWebView.NET_ERR_HTML);
                return;
            }
        }
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (!UtilWebView.isNetConnected(view.getContext())){
            if (!TzjWebView.NET_ERR_HTML.equals(mWebView.getUrl())){
                mWebView.loadUrl(TzjWebView.NET_ERR_HTML);
                return;
            }
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        handler.proceed();
    }
}
