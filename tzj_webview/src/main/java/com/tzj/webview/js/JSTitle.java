package com.tzj.webview.js;

import android.support.v7.app.ActionBar;
import android.webkit.JavascriptInterface;

/**
 * 控制标题
 */
public class JSTitle extends BaseJs{
    private ActionBar actionBar;

    public JSTitle(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    /**
     * 显示标题
     */
    @JavascriptInterface
    public void show(){
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                if (actionBar !=null){
                    actionBar.show();
                }
            }
        });
    }

    /**
     * 影藏标题
     */
    @JavascriptInterface
    public void hide(){
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                if (actionBar !=null){
                    actionBar.hide();
                }
            }
        });
    }
}
