package com.tzj.webview.js;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.tzj.webview.entity.UserInfo;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/2 11:48<br>
 * Description: 用户信息
 */
public class JSUserInfo extends BaseJs{
    public static IUserLogin login;
    private IUserLogin loginTemp = new IUserLogin() {
        @Override
        public boolean isLogin() {
            if (login == null){
                throw new RuntimeException("请给 UserInfo.login 赋值");
            }
            return login.isLogin();
        }

        @Override
        public UserInfo userInfo() {
            if (login == null){
                throw new RuntimeException("请给 UserInfo.login 赋值");
            }
            return login.userInfo();
        }

        @Override
        public void toLogin(Context ctx) {
            if (login == null){
                throw new RuntimeException("请给 UserInfo.login 赋值");
            }
            login.toLogin(ctx);
        }

        @Override
        public void loginResult(boolean b) {
            if (login == null){
                throw new RuntimeException("请给 UserInfo.login 赋值");
            }
            getUserInfo();
        }
    };
    /**
     * 用户信息，返回空json表示没登录
     */
    @JavascriptInterface
    public void getUserInfo(){
        if(loginTemp.isLogin()){
            callBack("setUserInfo",loginTemp.userInfo().toMap());
        }else{
            callBack("setUserInfo",null);
        }
    }

    /**
     * 获取用户信息，如果没登录就登录
     */
    @JavascriptInterface
    public void getUserInfoOrLogin(){
        if (loginTemp.isLogin()){
            callBack("setUserInfo",loginTemp.userInfo().toMap());
        }else{
            loginTemp.toLogin(mWebView.getContext());
        }
    }
    public interface IUserLogin{
        /**
         * 是否登录
         */
        boolean isLogin();

        /**
         * 登录信息
         * {
         *  "userId":"具体情况而定",
         * 	"sessionId":"",
         * 	"idNo":"身份证",
         * 	"name":"姓名",
         * 	"phone":"手机",
         * 	"isAuth":"是否实名",
         * 	"cIdNo":"被操作人身份证",
         * 	"cName":"被操作人姓名",
         * 	"cPhone":"被操作人手机",
         * }
         */
        UserInfo userInfo();

        /**
         * 去登录界面
         */
        void toLogin(Context ctx);

        /**
         * 登录成功回调
         */
        void loginResult(boolean b);
    }

}
