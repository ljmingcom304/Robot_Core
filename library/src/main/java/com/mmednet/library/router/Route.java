package com.mmednet.library.router;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Title:Route
 * <p>
 * Description:组件化路由
 * </p>
 * Author Jming.L
 * Date 2018/5/25 16:15
 */
public class Route {

    private Context context;
    private List<RouteBean> beans;
    private static final String TAG = Route.class.getSimpleName();

    private Route() {
    }

    private enum Singleton {
        INSTANCE;
        private Route route;

        Singleton() {
            route = new Route();
        }

        public Route getInstance() {
            return route;
        }
    }

    public static void initRoute(Context context) {
        Route route = Singleton.INSTANCE.getInstance();
        route.context = context;
        route.beans = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(context.getPackageCodePath(),
                PackageManager.GET_ACTIVITIES);
        ActivityInfo[] activities = info.activities;
        for (ActivityInfo act : activities) {
            try {
                Class<?> aClass = Class.forName(act.name);
                if (aClass.isAnnotationPresent(RouteNode.class)) {
                    RouteNode annotation = aClass.getAnnotation(RouteNode.class);
                    RouteBean bean = new RouteBean();
                    bean.setClassName(act.name);
                    bean.setHost(annotation.host());
                    bean.setPath(annotation.path());
                    bean.setDes(annotation.desc());
                    bean.setPriority(annotation.priority());
                    route.beans.add(bean);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(route.beans, new Comparator<RouteBean>() {
            @Override
            public int compare(RouteBean o1, RouteBean o2) {
                return o2.getPriority() - o1.getPriority();
            }
        });
    }

    /**
     * 通过Application中上下文启动
     *
     * @param intent 启动意图
     */
    public static boolean startActivity(@NonNull RouteIntent intent) {
        Route route = Singleton.INSTANCE.getInstance();
        List<RouteBean> beans = route.beans;
        Context context = route.context;
        if (beans == null || context == null) {
            throw new RuntimeException("Route not initialized!");
        }
        boolean flag = false;
        for (RouteBean bean : beans) {
            if (TextUtils.equals(bean.getHost(), intent.host())
                    && TextUtils.equals(bean.getPath(), intent.path())) {
                flag = true;
                intent.setClassName(context.getPackageName(), bean.getClassName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            }
        }
        if (!flag) {
            for (RouteBean bean : beans) {
                Log.e(TAG, "[Host=" + bean.getHost() + "][Path=" + bean.getPath() + "]");
            }
            Log.e(TAG, "STATE_ERROR:[Host=" + intent.host() + "][Path=" + intent.path() + "]");
        }
        return flag;
    }

    /**
     * 根据当前上下文启动
     *
     * @param context 当前上下文
     * @param intent  启动移除
     */
    public static boolean startActivity(Context context, @NonNull RouteIntent intent) {
        Route route = Singleton.INSTANCE.getInstance();
        List<RouteBean> beans = route.beans;
        if (beans == null) {
            throw new RuntimeException("Route not initialized!");
        }
        boolean flag = false;
        for (RouteBean bean : beans) {
            if (TextUtils.equals(bean.getHost(), intent.host())
                    && TextUtils.equals(bean.getPath(), intent.path())) {
                flag = true;
                intent.setClassName(context.getPackageName(), bean.getClassName());
                context.startActivity(intent);
                break;
            }
        }
        if (!flag) {
            for (RouteBean bean : beans) {
                Log.e(TAG, "[Host=" + bean.getHost() + "][Path=" + bean.getPath() + "]");
            }
            Log.e(TAG, "STATE_ERROR:[Host=" + intent.host() + "][Path=" + intent.path() + "]");
        }
        return flag;
    }

    public static boolean startActivityForResult(@NonNull Activity activity, @NonNull RouteIntent intent, int requestCode) {
        Route route = Singleton.INSTANCE.getInstance();
        List<RouteBean> beans = route.beans;
        Context context = route.context;
        if (beans == null || context == null) {
            throw new RuntimeException("Route not initialized!");
        }
        boolean flag = false;
        for (RouteBean bean : beans) {
            if (TextUtils.equals(bean.getHost(), intent.host())
                    && TextUtils.equals(bean.getPath(), intent.path())) {
                flag = true;
                intent.setClassName(context.getPackageName(), bean.getClassName());
                activity.startActivityForResult(intent, requestCode);
                break;
            }
        }
        if (!flag) {
            for (RouteBean bean : beans) {
                Log.e(TAG, "[Host=" + bean.getHost() + "][Path=" + bean.getPath() + "]");
            }
            Log.e(TAG, "STATE_ERROR:[Host=" + intent.host() + "][Path=" + intent.path() + "]");
        }
        return flag;
    }

    public static boolean startActivityForResult(@NonNull Fragment fragment, @NonNull RouteIntent intent, int requestCode) {
        Route route = Singleton.INSTANCE.getInstance();
        List<RouteBean> beans = route.beans;
        Context context = route.context;
        if (beans == null || context == null) {
            throw new RuntimeException("Route not initialized!");
        }
        boolean flag = false;
        for (RouteBean bean : beans) {
            if (TextUtils.equals(bean.getHost(), intent.host())
                    && TextUtils.equals(bean.getPath(), intent.path())) {
                flag = true;
                intent.setClassName(context.getPackageName(), bean.getClassName());
                fragment.startActivityForResult(intent, requestCode);
                break;
            }
        }
        if (!flag) {
            for (RouteBean bean : beans) {
                Log.e(TAG, "[Host=" + bean.getHost() + "][Path=" + bean.getPath() + "]");
            }
            Log.e(TAG, "STATE_ERROR:[Host=" + intent.host() + "][Path=" + intent.path() + "]");
        }
        return flag;
    }

}
