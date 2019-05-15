package com.mmednet.library.layout;

/**
 * Title:OnLayoutListener
 * <p>
 * Description:布局监听
 * </p>
 * Author Jming.L
 * Date 2018/3/21 17:46
 */
public interface OnLayoutListener {

    /**
     * 创建
     */
    void onCreate();

    /**
     * 交互
     */
    void onResume();

    /**
     * 停止
     */
    void onPause();

    /**
     * 销毁
     */
    void onDestroy();

}
