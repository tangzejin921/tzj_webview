package com.tzj.webview.delegate;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.tzj.webview.TzjWebView;


/**
 *
 */
public abstract class DefWebChromeClient extends WebChromeClient{
    protected TzjWebView mWebView;

    public void setmWebView(TzjWebView mWebView) {
        this.mWebView = mWebView;
    }

    @Override
    public void onPermissionRequest(PermissionRequest request) {
//        super.onPermissionRequest(request);//要去了
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
//        super.onPermissionRequestCanceled(request);//要去了
    }

    public boolean MyopenFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        return false;
    }
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        return false;
    }


}
