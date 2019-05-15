package com.mmednet.library.robot.manage;

/**
 * Title:Callback
 * <p>
 * Description:机器交互回调
 * </p>
 * Author Jming.L
 * Date 2017/9/4 17:32
 */
public interface Callback {

    /**
     * 成功
     */
    int SUCCESS = 0;
    /**
     * 失败
     */
    int FAILURE = 1;
    /**
     * 进度
     */
    int PROGRESS = 2;
    /**
     * 唤醒
     */
    int WAKEUP = 3;

    void onResult(int status, String result);
}
