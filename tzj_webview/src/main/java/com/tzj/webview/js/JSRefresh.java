package com.tzj.webview.js;

import android.webkit.JavascriptInterface;

import com.tzj.webview.TzjWebView;
import com.tzj.webview.utils.UtilWebView;

/**
 * 无网刷新
 */
public class JSRefresh extends BaseJs{
    /**
     * 网页出错情况下 返回上一个 Url
     * 其他情况刷新
     */
    @JavascriptInterface
    public void refresh(){
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                if (UtilWebView.isNetConnected(mWebView.getContext())){
                    if (mWebView.getUrl() == TzjWebView.NET_ERR_HTML){
                        if (mWebView.canGoBack()){
                            mWebView.goBack();
                        }
                    }else{
                        mWebView.reload();
                    }
                }
            }
        });
    }
}
