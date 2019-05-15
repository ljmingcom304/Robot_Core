package com.mmednet.library.robot.engine;

/**
 * Title:OnListener
 * <p>
 * Description:语音交互监听
 * </p>
 * Author Jming.L
 * Date 2017/9/4 17:31
 */
public interface OnListener {
    /**
     * 最终结果
     */
    boolean onResult(String result);

    /**
     * 中间信息
     */
    boolean onProgress(String result);

    /**
     * 语音唤醒
     */
    boolean onWakeup(String result);
}
