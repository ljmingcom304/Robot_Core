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

## Gradle依赖刷新
gradle build --refresh-dependencies

## 二维码使用参照
二维码：https://github.com/yuzhiqiang1993/zxing/releases


