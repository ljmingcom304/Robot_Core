# Library_Core

核心依赖库

* library:通用库
    ①DefApplication继承MultiDexApplication突破65535的限制，很多第三方库设置multiDexEnabled为true，如果不继承MultiDexApplication，由于类加载器的委托机制会导致加载不到相应的类。
* library_baidu_voice:语音识别唤醒文本转语音
* library_baidu_face:百度人脸识别
* library_baidu_ocr:百度文字识别
* library_baidu_player:百度视频播放器
* library_klyl:康力优蓝库工程
* library_xyzj:小鱼在家库工程
* library_reader:身份证读取器


gradle build --refresh-dependencies

二维码：https://github.com/yuzhiqiang1993/zxing/releases


######################################################################

```
//打不包含依赖的jar包
def SDK_BASENAME = "mmednet-core-";
def SDK_VERSION = "v1.2.3";
def sdkDestinationPath = "build/libs/";
def zipFile = file('build/intermediates/bundles/default/classes.jar')
task deleteBuild(type: Delete) {
    delete sdkDestinationPath + SDK_BASENAME + SDK_VERSION + ".jar"
}
task makeJar(type: Jar) {
    from zipTree(zipFile)
    from fileTree(dir: 'src/main', includes: ['assets*//**//**'])
    baseName = SDK_BASENAME + SDK_VERSION
    destinationDir = file(sdkDestinationPath)
    //去掉不要的类
    exclude('**//*BuildConfig.class')
    exclude('**//*BuildConfig\$*.class')
    exclude('**//*R.class')
    exclude('**//*R\$*.class')
    exclude('test/')
    exclude('androidTest/')
}
```