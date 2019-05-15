package com.mmednet.library.log.crash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.mmednet.library.util.UIUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Title:UncaughtCrashHandler
 * <p>
 * Description:保证页面不会崩溃
 * </p>
 * Author Jming.L
 * Date 2018/6/26 15:30
 */
public class UncaughtCrashHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Looper.loop();
                } catch (Exception e) {
                    finishActivity();
                    UIUtils.showToast(mContext, "Error" );
                    e.printStackTrace();
                }
            }
        }
    };

    public UncaughtCrashHandler(Context context) {
        this.mContext = context;
        mHandler.post(mRunnable);
    }

    //结束当前Acitivty
    private void finishActivity() {
        try {
            @SuppressLint( "PrivateApi" )
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            @SuppressWarnings( "unchecked" )
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    activity.finish();
                    return;
                }
            }
        } catch (Exception e) {
            //如果无法正常结束Activity则直接杀死线程
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }
}
