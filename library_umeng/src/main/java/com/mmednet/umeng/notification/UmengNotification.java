package com.mmednet.umeng.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

/**
 * 音视频聊天通知栏
 * Created by huangjun on 2015/5/14.
 */
public class UmengNotification {

    private Context context;
    private int notifyId;
    private NotificationManager notificationManager;
    private Notification notification;

    public UmengNotification(Context context) {
        this(context, 100);
    }

    public UmengNotification(Context context, int notifyId) {
        this.context = context;
        this.notifyId = notifyId;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannelCompat26.createMessageNotificationChannel(context);
    }

    public Notification makeNotification(String title, String content, String tickerText, int iconId) {
        return makeNotification(null, title, content, tickerText, iconId, false, false);
    }

    public Notification makeNotification(Intent intent, String title, String content, String tickerText, int iconId) {
        return makeNotification(intent, title, content, tickerText, iconId, false, false);
    }

    /**
     * @param intent     意图
     * @param title      通知栏标题
     * @param content    通知栏内容
     * @param tickerText 状态栏文字
     * @param iconId     状态栏图标
     * @param ring       是否响铃
     * @param vibrate    是否震动
     * @return
     */
    private Notification makeNotification(Intent intent, String title, String content, String tickerText,
                                          int iconId, boolean ring, boolean vibrate) {

        String channelId = NotificationChannelCompat26.getChannelId(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setTicker(tickerText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(iconId);
        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        } else {
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    notifyId, new Intent(), PendingIntent.FLAG_NO_CREATE);
            builder.setContentIntent(pendingIntent);
        }
        int defaults = Notification.DEFAULT_LIGHTS;
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (ring) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        builder.setDefaults(defaults);
        this.notification = builder.build();
        return notification;
    }

    /**
     * 打开取消通知
     *
     * @param active 是否开启通知
     */
    public void activeNotification(boolean active) {
        if (notificationManager != null) {
            if (active) {
                notificationManager.notify(notifyId, notification);
            } else {
                notificationManager.cancel(notifyId);
            }
        }
    }


}
