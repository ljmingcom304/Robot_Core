package com.mmednet.umeng;

import android.content.Context;
import android.util.Log;

import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Title:UmengObserver
 * <p>
 * Description:友盟消息观察者
 * </p >
 * Author Jming.L
 * Date 2021/3/30 15:29
 */
public class UmengObserver extends UmengNotificationClickHandler {

    private static final String TAG = UmengUtils.class.getSimpleName();

    @Override
    public void handleMessage(Context context, UMessage uMessage) {
        super.handleMessage(context, uMessage);
        Log.i(TAG, "友盟消息测试：" + uMessage.after_open + "=" + uMessage.text);
    }

}
