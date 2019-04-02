package com.tzj.webview.delegate;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.widget.Toast;

/**
 * 权限
 */
public class PermissionDelegate extends DefWebChromeClient {

    @Override
    public void onPermissionRequest(final PermissionRequest request) {
        super.onPermissionRequest(request);
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                Context context = mWebView.getContext();
                Activity activity = (Activity) context;
                if (context instanceof Activity) {
                    if (!mWebView.requestPermissions(activity, request.getResources())) {
                        Toast.makeText(activity.getApplicationContext(), "请求权限失败", Toast.LENGTH_LONG).show();
                        request.deny();
                    }
                }
            }
        });

    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        super.onPermissionRequestCanceled(request);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//        super.onGeolocationPermissionsShowPrompt(origin, callback);
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                Context context = mWebView.getContext();
                Activity activity = (Activity) context;
                if (context instanceof Activity) {
                    if (!mWebView.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
                        Toast.makeText(activity.getApplicationContext(), "请求权限失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
