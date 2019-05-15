package com.mmednet.library.layout;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Title:CustomApplication
 * <p>
 * Description:启动项
 * </p>
 * Author Jming.L
 * Date 2017/12/18 13:58
 */
public class CustomApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks{

    private Activity mCurActivity;
    private List<Activity> mActivityList;
    private static CustomApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        registerActivityLifecycleCallbacks(this);
        mActivityList = new ArrayList<>();
    }

    public static CustomApplication getInstance() {
        return mInstance;
    }


    /**
     * 退出
     */
    public final void exit() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public List<Activity> getActivities(){
        return mActivityList;
    }

    public Activity getCurActivity() {
        return mCurActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.mCurActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityList.remove(activity);
    }

}
