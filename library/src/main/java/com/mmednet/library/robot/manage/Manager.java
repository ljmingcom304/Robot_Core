package com.mmednet.library.robot.manage;

import android.content.Context;

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
     */
    void wakeUp(boolean wakeup);
}
