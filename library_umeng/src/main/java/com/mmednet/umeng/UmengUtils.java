package com.mmednet.umeng;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.mmednet.umeng.notification.UmengNotification;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observer;

/**
 * Title: UPushUtils
 * <p>
 * Description:友盟推送工具类
 * </p>
 * Author Zsy
 * Date 2019/3/7  11:18
 */
public class UmengUtils {

    private static final List<UmengObserver> mObservers = new ArrayList<>();
    private static final String TAG = UmengUtils.class.getSimpleName();
    private static String umengToken;

    public static void initUmeng(Context context, String umengKey, String umengSecret) {
        //友盟组件初始化
        UMConfigure.init(context, umengKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, umengSecret);
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        UMConfigure.setLogEnabled(true);
    }

    /**
     * 初始化友盟推送
     *
     * @param context     上下文
     * @param packageName 清单文件AndroidManifest.xml中包名，可能和ApplicationId中的不一致
     * @param logoRes     资源ID
     */
    public static void initUmengPush(Context context, String packageName, final int logoRes) {
        try {
            PushAgent pushAgent = PushAgent.getInstance(context);
            UmengNotification notification = new UmengNotification(context);
            if (packageName != null) {
                pushAgent.setResourcePackageName(packageName);
            }
            //Vivo Honor个别手机出现(mPushAgent.register方法应该在主线程和子线程都被调用的Toast)
            pushAgent.register(new IUmengRegisterCallback() {
                @Override
                public void onSuccess(String deviceToken) {
                    umengToken = deviceToken;
                    Log.i(TAG, "友盟注册成功：deviceToken：-------->  " + deviceToken + " 机型 " + Build.MANUFACTURER);
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.e(TAG, "友盟注册失败：" + s + "=" + s1);
                }
            });
            pushAgent.setDisplayNotificationNumber(3);
            //接收消息时处理
            pushAgent.setMessageHandler(new UmengMessageHandler() {
                @Override
                public Notification getNotification(Context context, UMessage msg) {
                    if (msg.builder_id == 1) {
                        return notification.makeNotification(msg.title, msg.text, msg.text, logoRes);
                    }
                    return super.getNotification(context, msg);
                }
            });
            //点击消息时处理
            pushAgent.setNotificationClickHandler(new UmengNotificationClickHandler() {

                @Override
                public void handleMessage(Context context, UMessage uMessage) {
                    super.handleMessage(context, uMessage);
                    Log.i(TAG, "友盟消息测试：" + uMessage.after_open + "=" + uMessage.text);
                    for (UmengObserver observer : mObservers) {
                        observer.onMessage(context, uMessage);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 友盟消息观察者
     *
     * @param observer 观察者
     * @param register 注册|注销
     */
    public static void registerObserver(UmengObserver observer, boolean register) {
        if (register) {
            mObservers.add(observer);
        } else {
            mObservers.remove(observer);
        }
    }

    public static String getDeviceToken() {
        return umengToken == null ? android.os.Build.SERIAL : umengToken;
    }

    /**
     * 登录时收集账号相关信息
     */
    public static void loginIn(String userId) {
        //当用户使用自有账号登录时，可以这样统计：
        MobclickAgent.onProfileSignIn(userId);
    }

    /**
     * 登出时收集账号相关信息
     */
    public static void loginOut() {
        MobclickAgent.onProfileSignOff();
    }


}
