# Library_Core

## 核心依赖库
* library:通用库
    ①DefApplication继承MultiDexApplication突破65535的限制，很多第三方库设置multiDexEnabled为true，如果不继承MultiDexApplication，由于类加载器的委托机制会导致加载不到相应的类。
* library_baidu_voice:语音识别唤醒文本转语音
* library_baidu_face:百度人脸识别
* library_baidu_ocr:百度文字识别
* library_baidu_player:百度视频播放器
* library_klyl:康力优蓝库工程
* library_xyzj:小鱼在家库工程
* library_reader:身份证读取器
* library_zxing:ZXing二维码
* library_umeng:友盟推送统计
* library_graphql:B端Graphql接口

## Gradle依赖刷新
gradle build --refresh-dependencies

## 二维码使用参照
二维码：https://github.com/yuzhiqiang1993/zxing/releases

## WebView和HTML交互
WebView webView = new WebView(this);
webView.loadUrl("https://testres.mmednet.com/static/media/wechat_mini_pgm/html/middle.html");
webView.getSettings().setJavaScriptEnabled(true);
webView.addJavascriptInterface(new Message(), "message");

private class Message {
    @JavascriptInterface
    public void show() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:show(" + "'abcd'" + ")");//调用HTML方法
            }
        });
    }
    @JavascriptInterface
    public void call(String message) {
        Log.e("Message", "HTML消息" + message);//被HTML调用
    }
}

## Graphql相关
### 插件安装
classpath 'com.apollographql.apollo:apollo-gradle-plugin:2.5.2'
apply plugin: 'com.apollographql.apollo'
implementation 'com.apollographql.apollo:apollo-api:2.5.2'
implementation 'com.apollographql.apollo:apollo-runtime:2.5.2'
### schema.json文件下载
* 安装Node环境
* 安装npm install -g apollo-codegen
* 生成文件apollo-codegen download-schema http://api-expert-mmed.ebilin.net:4000/graphql --output schema.json
### 常见问题
* Can't query `accid` on type `Doctor`：请更新请更新schema.json文件
* Field `certificates` of type `Doctor` must have a selection of sub-fields：该参数需要声明为一个存在属性的对象


