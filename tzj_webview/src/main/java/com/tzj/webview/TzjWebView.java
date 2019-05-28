package com.tzj.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tzj.webview.delegate.DefWebChromeClient;
import com.tzj.webview.delegate.DefWebViewClient;
import com.tzj.webview.delegate.FullscreenDelegate;
import com.tzj.webview.delegate.HrefWebViewClient;
import com.tzj.webview.delegate.LoadingProgressDelegate;
import com.tzj.webview.delegate.NetErrDelegate;
import com.tzj.webview.delegate.PermissionDelegate;
import com.tzj.webview.delegate.UploadDelegate;
import com.tzj.webview.js.BaseJs;
import com.tzj.webview.utils.UtilWebView;


public class TzjWebView extends WebView {
    public static final String NET_ERR_HTML = "file:///android_asset/net_error.html";
//    private Activity mActivity;
    private WebViewClientDelegate webViewClient = new WebViewClientDelegate(this);
    private WebChromeClientDelegate webChromeClient = new WebChromeClientDelegate(this);

    /**
     * 第一次加载的url
     */
    private String originalUrl;
    /**
     * 最近的 url 包括不是 http 开头的
     */
    protected String currentUrl;
    /**
     * 最近http 开头的url
     */
    protected String currentHttpUrl;

    public TzjWebView(Context context) {
        super(context);
        init();
    }

    public TzjWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TzjWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TzjWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    protected void init() {
        if ((getContext() instanceof Activity)) {
            Activity mActivity = (Activity) getContext();
            //硬件加速
            mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        String userAgent = getContext().getPackageName();
        try {
            int id = getResources().getIdentifier("user_agent_app","string",getContext().getPackageName());
            String temp = getResources().getString(id);
            if (!TextUtils.isEmpty(temp)){
                userAgent = temp;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(userAgent)){
            UtilWebView.addUserAgent(this,"app",userAgent);
        }

        getSettings().setLoadsImagesAutomatically(true);//自动加载图片
//        getSettings().setBlockNetworkLoads(true);//不加载网络图片
        getSettings().setUseWideViewPort(false);//将图片调整到适合webview的大小
        getSettings().setDomStorageEnabled(true);//用于持久化的本地存储
        getSettings().setDatabaseEnabled(true);
        getSettings().setAppCacheEnabled(true);//缓存
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        //启用地理定位
        getSettings().setGeolocationEnabled(true);
        getSettings().setGeolocationDatabasePath(appCachePath);
        getSettings().setAppCachePath(appCachePath);
        getSettings().setAppCacheMaxSize(1024 * 1024 * 5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//https的URL时在5.0以上加载不了
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getSettings().setAllowFileAccessFromFileURLs(true);
        }
//        getSettings().setSupportZoom(true);//zoom
        getSettings().setBuiltInZoomControls(true);//设置支持缩放
        getSettings().setUseWideViewPort(true);//扩大比例的缩放
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        getSettings().setJavaScriptEnabled(true);//js
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        getSettings().setAllowFileAccess(true);//启用或禁止WebView访问文件数据
        //下载
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (null != url) {//这里会打开浏览器下载
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    getContext().startActivity(intent);
                }
            }
        });
        //
        addWebViewClient(new NetErrDelegate());
        addWebViewClient(new HrefWebViewClient());
        addWebChromeClient(new UploadDelegate());
        addWebChromeClient(new LoadingProgressDelegate());
        addWebChromeClient(new FullscreenDelegate());
        addWebChromeClient(new PermissionDelegate());
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
    }

    public void addWebViewClient(DefWebViewClient client) {
        webViewClient.addDelegate(client);
    }

    public void addWebChromeClient(DefWebChromeClient client) {
        webChromeClient.addDelegate(client);
    }

    public void loadJs(String js) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(js, null);
        } else {
            super.loadUrl(js);
        }
    }

    public void loadData(String data) {
        super.loadData(data, "text/html; charset=UTF-8", null);
    }


    @Override
    public void loadUrl(String url) {
        if (url == null){
            url = currentUrl;
        }
        if (url.toLowerCase().startsWith("http")) {
            if (originalUrl == null) {
                originalUrl = url;
            }
            super.loadUrl(currentHttpUrl = url);
        }
        super.loadUrl(currentUrl = url);
    }

    @SuppressLint("JavascriptInterface")
    public void interfaceJs(BaseJs js){
        js.setWebView(this);
        addJavascriptInterface(js,js.name());
    }

    @Override
    public String getOriginalUrl() {
        return originalUrl;
    }

    public String setUrl(String url){
        return currentUrl = url;
    }
    @Override
    public String getUrl() {
        return currentUrl;
    }

    public String getCurrentHttpUrl() {
        return currentHttpUrl;
    }

    /**
     *
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return webChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        return webChromeClient.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeTimers();
    }

    @Override
    public void destroy() {
        setWebViewClient(webViewClient = null);
        setWebChromeClient(webChromeClient = null);
//        mActivity = null;

        loadUrl("about:blank");
        zoomOut();
        setVisibility(GONE);
        removeAllViews();
        super.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
            goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public boolean requestPermissions(Activity act, String... lists) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String s : lists) {
                boolean permissionGranted = PermissionChecker.checkCallingOrSelfPermission(act, s) == PackageManager.PERMISSION_GRANTED;
                //只要有一个没权限的，请求全部权限
                if (!permissionGranted) {
                    ActivityCompat.requestPermissions(act, lists, hashCode()%10000);
                    return false;
                }
            }
        }
        return true;
    }
}
