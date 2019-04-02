package com.tzj.webview.entity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/2 14:54<br>
 * Description: 规范用户信息
 */
public class UserInfo {
    /**
     * 具体情况而定
     */
    private String userId;
    /**
     * 登录唯一代码
     */
    private String sessionId;
    private String idNo;
    private String name;
    private String phone;
    /**
     * 是否实名
     */
    private String isAuth;
    /**
     * 被操作人身份证
     */
    private String cIdNo;
    /**
     * 被操作人姓名
     */
    private String cName;
    /**
     * 被操作人手机
     */
    private String cPhone;

    public UserInfo() {

    }

    public UserInfo(String userId, String sessionId,
                    String idNo, String name, String phone, String isAuth,
                    String cIdNo, String cName, String cPhone) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.idNo = idNo;
        this.name = name;
        this.phone = phone;
        this.isAuth = isAuth;
        this.cIdNo = cIdNo;
        this.cName = cName;
        this.cPhone = cPhone;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }

    public void setcIdNo(String cIdNo) {
        this.cIdNo = cIdNo;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("sessionId",sessionId);
        map.put("idNo",idNo);
        map.put("name",name);
        map.put("phone",phone);
        map.put("isAuth",isAuth);
        map.put("cIdNo",cIdNo);
        map.put("cName",cName);
        map.put("cPhone",cPhone);
        return map;
    }

    @Override
    public String toString() {
        return new JSONObject(toMap()).toString();
    }
}
