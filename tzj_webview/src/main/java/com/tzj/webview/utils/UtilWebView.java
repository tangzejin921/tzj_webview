package com.tzj.webview.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.io.File;

public class UtilWebView {

    /**
     * 加 UserAgent
     */
    public static String addUserAgent(WebView webView, String key, String value){
        String userAgentString = webView.getSettings().getUserAgentString();
        userAgentString += " "+key+"/"+value;
        webView.getSettings().setUserAgentString(userAgentString);
        return userAgentString;
    }

    /**
     * 页面加载完成才可以
     * 保存 Storage
     */
    public static void setStorage(WebView webView,String key,String val) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("window.localStorage.setItem('" + key + "','" + val + "');", null);
        } else {
            webView.loadUrl("javascript:localStorage.setItem('" + key + "','" + val + "');");
        }
    }

    /**
     * 页面加载完成才可以
     * 获取 Storage
     * //todo 4.4 以下获取不到
     */
    public static void getStorage(WebView webView,String key,ValueCallback callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("window.localStorage.getItem('" + key + "');", callback);
        } else {
            webView.loadUrl("javascript:localStorage.getItem('" + key + "');");
        }
    }
    /**
     * 清除 cookies
     */
    public static void clearCookies(Context ctx) {
        CookieSyncManager csm = CookieSyncManager.createInstance(ctx.getApplicationContext());
        csm.startSync();
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            cm.flush();
        }
        csm.stopSync();
    }

    /**
     * 添加 cookies
     */
    private void setCookie(Context ctx,String url,String cookie){
        CookieSyncManager csm = CookieSyncManager.createInstance(ctx.getApplicationContext());
        csm.startSync();
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.setCookie(url, cookie);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            cm.flush();
        }
        csm.stopSync();
    }

    /**
     * 获取 cookies
     */
    private String getCookie(Context ctx,String url){
        CookieSyncManager csm = CookieSyncManager.createInstance(ctx.getApplicationContext());
        csm.startSync();
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        String cookie = cm.getCookie(url);
        csm.stopSync();
        return cookie;
    }

    /**
     * 是否有网
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) { // 连接上网络
            return true;
        } else {   // 没有连接上
            return false;
        }
    }

    /**
     * 图片等
     */
    public static Uri parUri(Context ctx, File cameraFile) {
        Uri imageUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }
}
