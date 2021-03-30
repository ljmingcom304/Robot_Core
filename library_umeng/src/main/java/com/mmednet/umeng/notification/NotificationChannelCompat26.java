package com.mmednet.umeng.notification;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import java.util.Locale;

/**
 * Title:NotificationChannelCompat26
 * <p>
 * Description:适配Android O版本的通知栏
 * </p >
 * Author Jming.L
 * Date 2021/3/26 14:11
 */
class NotificationChannelCompat26 {

    private static final String CHANNEL_ID = "tip_channel_001";
    private static String CHANNEL_NAME = "channel_name";
    private static String CHANNEL_DESC = "notification_desc";

    static String getChannelId(Context context) {
        /*
         * 适配关键：target 8.0+必须设置一个channel，8.0以下一定要返回null！否则通知栏弹不出
         */
        return isBuildAndTargetO(context) ? CHANNEL_ID : null;
    }

    static void createMessageNotificationChannel(Context context) {
        /*
         * 适配关键：只有8.0+的机器才能创建NotificationChannel，否则会找不到类。target 8.0+才需要去创建一个channel，否则就用默认通道即null
         */
        if (!isBuildAndTargetO(context)) {
            return;
        }
        configLanguage(context);
        NotificationChannel channel;
        NotificationManager manager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        if (manager != null) {
            channel = manager.getNotificationChannel(CHANNEL_ID); // 已经存在就不要再创建了，无法修改通道配置
            if (channel == null) {
                channel = buildMessageChannel();
                manager.createNotificationChannel(channel);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static NotificationChannel buildMessageChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNEL_DESC);
        channel.enableVibration(true);
        channel.setShowBadge(false);
        return channel;
    }

    private static boolean isBuildAndTargetO(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.O;
    }

    private static void configLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language != null && language.endsWith("zh")) {
            // default channel
            CHANNEL_NAME = "通知名称";
            CHANNEL_DESC = "通知描述";
        }
    }
}
