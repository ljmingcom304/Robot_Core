package com.mmednet.baidu;

public interface Message {
    /**
     * 成功
     */
    int SUCCESS = 0;
    /**
     * 失败
     */
    int FAILURE = 1;
    /**
     * 临时
     */
    int PROGRESS = 2;

    /**
     * 取消
     */
    int CANCEL = 3;

    /**
     * 开始
     */
    int STARTED = 4;

    /**
     * 停止
     */
    int STOPED = 5;

    /**
     * 返回结果
     *
     * @param status 状态码
     * @param word   结果信息
     */
    void onResult(int status, String word);
}
