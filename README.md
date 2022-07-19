# Library_Core

实际业务需求开发的常用框架

## 核心依赖库

* library:通用库:分词、加密、数据库、网络、日志、异常处理、机器语音、模块路由、表单注入(DefApplication继承MultiDexApplication突破65535的限制，很多第三方库设置multiDexEnabled为true，如果不继承MultiDexApplication，由于类加载器的委托机制会导致加载不到相应的类。)
* library_baidu_voice:语音识别唤醒文本转语音
* library_baidu_face:百度人脸识别
* library_baidu_ocr:百度文字识别
* library_baidu_player:百度视频播放器
* library_klyl:康力优蓝库工程
* library_xyzj:小鱼在家库工程
* library_reader:身份证读取器
* library_zxing:ZXing二维码
* library_umeng:友盟推送统计
* library_graphql:Graphql接口

## Gradle依赖刷新

```
gradle build --refresh-dependencies
```

## 二维码使用参照

[二维码](https://github.com/yuzhiqiang1993/zxing/releases)

## WebView和HTML交互

```java
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
```

## 各种工具类集合
implementation 'com.blankj:utilcode:1.23.2'

## Graphql相关

### 插件安装

* classpath 'com.apollographql.apollo:apollo-gradle-plugin:2.5.2'
* apply plugin: 'com.apollographql.apollo'
* implementation 'com.apollographql.apollo:apollo-api:2.5.2'
* implementation 'com.apollographql.apollo:apollo-runtime:2.5.2'

### schema.json文件下载

* 安装Node环境
* 安装npm install -g apollo-codegen
* 生成文件apollo-codegen download-schema http://api-expert-mmed.ebilin.net:4000/graphql --output schema.json

### 常见问题

* Can't query `accid` on type `Doctor`：请更新请更新schema.json文件
* Field `certificates` of type `Doctor` must have a selection of sub-fields：该参数需要声明为一个存在属性的对象

## Git操作

### 初始导入

```
git init
git remote add origin http://your_username@git.xxx.com/your_username/demo.git
git add .
git commit -m "Initial commit"
git pull origin master
git push -u origin master		//-u表示设置为上游分支
```

### 批量提交

```
git add -A
git commit -m '备注信息'
git pull
git push
```

### 分支查看

```
git branch --all
```

### 切换分支

```
git checkout 本地分支名
```

### 拉取远程分支并创建本地分支

```
git checkout -b 本地分支名 origin/远程分支名
```

### 创建本地分支

```
git push origin feature/target:feature/target		//生成远程分支
git checkout feature/target				            //切换到本地分支
git branch -u origin/feature/target			        //建立本地分支与远程分支的映射关系
```

### 新建远程分支
```

git pull						                    //检出代码到工作区
git checkout -b feature/target origin/master 		//从远程检出代码并
```

### 查看分支关联关系

```
git branch -vv
```

### 删除本地分支

```
git branch -d branch_name
```

### 删除远程分支

```
git branch -r -d origin/branch-name
git push origin :branch-name
```

### 其他操作

```
查看分支：git branch
创建分支：git branch <name>
切换分支：git checkout <name>
创建+切换分支：git checkout -b <name>
合并某分支到当前分支：git merge <name>
删除分支：git branch -d <name>
git checkout --track origin/branch_name             //本地会新建一个分支名叫 branch_name ，会自动跟踪远程的同名分支 branch_name
```

