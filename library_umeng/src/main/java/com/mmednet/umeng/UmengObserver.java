package com.mmednet.umeng;

import android.content.Context;

import com.umeng.message.entity.UMessage;

/**
 * Title:UmengObserver
 * <p>
 * Description:友盟消息观察者
 * </p >
 * Author Jming.L
 * Date 2021/3/30 15:29
 */
public interface UmengObserver {
    void onMessage(Context context, UMessage uMessage);
}
