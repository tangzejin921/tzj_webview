package com.tzj.webview.js;

import com.tzj.webview.TzjWebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaseJs implements IJs {
    protected TzjWebView mWebView;

    @Override
    public void setWebView(TzjWebView webView) {
        mWebView = webView;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    /**
     * 生成调用 js 内容
     * @param methed 方法名
     * @param map    参数
     */
    public void callBack(final String methed,Map<String, Object> map) {
        if (map == null){
            map = new HashMap<String,Object>();
        }
        final Map<String, Object> temp = map;
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb = new StringBuffer();
                sb.append("javascript:")
                        .append(methed)
                        .append("('")
                        .append(new JSONObject(temp).toString())
                        .append("')");
                mWebView.loadJs(sb.toString());
            }
        });
    }
}
