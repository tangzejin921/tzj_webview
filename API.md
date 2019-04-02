# js 接口 调用
## userAgent 中你将可以获得
app/(android-包名/ios-BundId)

## js 调用平台
#### 刷新
```text
/**
 * 网页出错情况下 返回上一个 Url
 * 其他情况刷新
 */
Refresh.refresh();
```
#### title
```text
/**
 * 显示标题
 */
Title.show();

/**
 * 隐藏标题
 */
Title.hide();
```
#### 用户
```text
/**
 * 用户信息，
 * 会回调 javascript:setUserInfo(json)
 * json 为 空json表示没登录
 */
UserInfo.getUserInfo();
/**
 * 获取用户信息，如果没登录会跳转登录页面
 * 会回调 javascript:setUserInfo(json)
 * json 为 空json表示没登录
 */
UserInfo.getUserInfoOrLogin();
```
## 平台调用 js

#### 用户
```text
/**
    {
          "userId":"具体情况而定",
          "sessionId":"",
          "idNo":"身份证",
          "name":"姓名",
          "phone":"手机",
          "isAuth":"是否实名",
          "cIdNo":"被操作人身份证",
          "cName":"被操作人姓名",
          "cPhone":"被操作人手机",
    }
 */
javascript:setUserInfo(json)
```