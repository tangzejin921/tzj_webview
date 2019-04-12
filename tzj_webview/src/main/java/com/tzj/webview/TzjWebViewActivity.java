package com.tzj.webview;

import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.tzj.webview.delegate.DefWebChromeClient;
import com.tzj.webview.js.JSRefresh;
import com.tzj.webview.js.JSTitle;
import com.tzj.webview.js.JSUserInfo;


public class TzjWebViewActivity extends AppCompatActivity {

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, TzjWebViewActivity.class);
        starter.putExtra("url",url);
        context.startActivity(starter);
    }

    /**
     * @param context
     * @param url
     * @param showTitle 强制标题的显示还是影藏
     */
    public static void start(Context context, String url,Boolean showTitle,String title) {
        Intent starter = new Intent(context, TzjWebViewActivity.class);
        starter.putExtra("url",url);
        starter.putExtra("showTitle",showTitle);
        starter.putExtra("title",title);
        context.startActivity(starter);
    }

    private TzjWebView mWebView;
    private String url;
    private Boolean showTitle;
    private String title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        if (getIntent().hasExtra("showTitle")){
            showTitle = getIntent().getBooleanExtra("showTitle",true);
        }
        title = getIntent().getStringExtra("title");
        setContentView(mWebView = new TzjWebView(this));
        mWebView.addWebChromeClient(new DefWebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)){
                    setTitle(title);
                }
            }
        });
        if(showTitle != null){
            if (showTitle){
                setTitleVisibility(View.VISIBLE);
                setTitle(title);
            }else{
                setTitleVisibility(View.GONE);
            }
        }else{
            mWebView.interfaceJs(new JSTitle(getSupportActionBar()));
        }
        mWebView.interfaceJs(new JSRefresh());
        mWebView.interfaceJs(new JSUserInfo());
        mWebView.loadUrl(url);
        mWebView.addJavascriptInterface(null,"");
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle(title);
            // 显示返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 去掉logo图标
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    public void setTitleVisibility(int visibility){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            if (visibility == View.GONE){
                actionBar.hide();
            }else{
                actionBar.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mWebView.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWebView.onActivityResult(requestCode,resultCode,data);
    }

}
