# tzj_webview
webview

**只支持 android**
## 功能
flutter 调用android 的 webview

## 为什么要做下面的 工程引入
    因为工程结构被我改了，

    原来是：
    android
    ios
    lib
    pubspec.yaml

    被我改为了
    android
        src
            main
            flutter
                pubspec.yaml

    所以要做一些改动


## 工程引入
- pub 加入
    ```pub
    dev_dependencies:
      tzj_webview:
        git:
          url: git://github.com/tzjandroid/tzj_webview.git
          path: tzj_webview/src/flutter
    ```
- android 工程下的 settings.gradle 中改为如下
    ```Gradle
    def flutterProjectRoot = rootProject.projectDir.parentFile.toPath()

    def plugins = new Properties()
    def pluginsFile = new File(flutterProjectRoot.toFile(), '.flutter-plugins')
    if (pluginsFile.exists()) {
        pluginsFile.withReader('UTF-8') { reader -> plugins.load(reader) }
    }

    plugins.each { name, path ->
        def pluginDirectory = flutterProjectRoot.resolve(path).resolve('android').toFile()
        if(!pluginDirectory.exists()){
            pluginDirectory = flutterProjectRoot.resolve(path).getParent().getParent().toFile()
        }
        if(pluginDirectory.exists()){
            include ":$name"
            project(":$name").projectDir = pluginDirectory
        }
    }
    ```
- android 工程下的 build.gradle  加入
    ```Gradle
    rootProject.extensions.add("tzj_webview",Type.isFlutterPlugin.name())
    enum Type{
        isAPP,
        isModule,
        isFlutterPlugin;
    }
    project.ext {
        ext._compileSdkVersion = 27
        ext._buildToolsVersion = '27.0.3'
        ext._minSdkVersion = 16
        ext._targetSdkVersion = 27
        ext._supportVersion = "27.1.1"
        ext.javaVersion = JavaVersion.VERSION_1_8
    }
    ```

## example
example 目录有个 demo

# js调用api 

## 刷新
```java
/**
 * 网页出错情况下 返回上一个 Url
 * 其他情况刷新
 */
Refresh.refresh();
```
## title
```java
/**
* 显示标题
*/
Title.show();

/**
* 隐藏标题
*/
Title.hide();
```
## 用户
```java
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
需要提供接口 javascript:setUserInfo(json)
