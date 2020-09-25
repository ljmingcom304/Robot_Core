package com.mmednet.library.robot.manage;

import android.content.Context;

import org.jetbrains.annotations.Nullable;

/**
 * Title:Manager
 * <p>
 * Description:控制器
 * </p>
 * Author Jming.L
 * Date 2017/9/4 17:24
 */
public interface Manager {

    /**
     * 初始资源
     *
     * @param context 上下文
     */
    void init(Context context);

    /**
     * 回收资源
     *
     * @param context 上下文
     */
    void release(Context context);

    /**
     * 文本转语音
     *
     * @param text     转换文本内容
     * @param callback 回调转换结果
     */
    void text2Speech(String text, Callback callback);

    /**
     * 语音转文本
     *
     * @param callback 回调转换结果
     */
    void speech2Text(Callback callback);

    /**
     * 语音识别唤醒
     *
     * @param wakeup true:启动语音识别；false:关闭语音识别
     */
    void wakeUp(boolean wakeup);

    /**
     * 拍照
     *
     * @param callback 回调拍照结果
     */
    void takePicture(Callback callback);

    /**
     * 人脸识别
     *
     * @param callback 回调识别结果
     * @param dir      人脸库目录
     */
    void openFaceRecognition(Callback callback, @Nullable String dir);
}
