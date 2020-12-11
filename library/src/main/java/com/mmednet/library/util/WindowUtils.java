package com.mmednet.library.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * Title:WindowUtils
 * <p>
 * Description:Window窗体工具类
 * </p>
 * Author Jming.L
 * Date 2018/9/23 18:54
 */
public class WindowUtils {

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    public static void setStatusBarColor(Activity activity, boolean dark) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = window.getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            if (dark) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * 设置状态栏背景，一个Acitivty中只能调用一次，仅支持5.0以上
     * <style name="MainTheme" parent="Theme.AppCompat.Light.NoActionBar">
     * <item name="android:windowTranslucentStatus">true</item>
     * <item name="android:windowTranslucentNavigation">false</item>
     * <item name="android:statusBarColor" tools:ignore="NewApi">@android:color/transparent</item>
     * <*item name="android:windowLightStatusBar" tools:ignore="NewApi">false</item>
     * <item name="android:windowDisablePreview">true</item>
     * </style>
     */
    public static void setStatusBarDrawable(Activity activity, Drawable drawable) {
        if (activity == null) {
            return;
        }

        if (drawable == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FrameLayout rootView = (FrameLayout) activity.findViewById(android.R.id.content);
            int count = rootView.getChildCount();
            if (count > 0) {
                View layout = rootView.getChildAt(0);
                int statusBarHeight = getStatusBarHeight(activity);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) layout.getLayoutParams();
                layoutParams.topMargin = statusBarHeight;

                ImageView statusBarView;
                if (count > 1) {
                    statusBarView = (ImageView) rootView.getChildAt(1);
                } else {
                    statusBarView = new ImageView(activity);
                    LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
                    statusBarView.setScaleType(ImageView.ScaleType.FIT_XY);
                    statusBarView.setLayoutParams(viewParams);
                    rootView.addView(statusBarView);
                }
                statusBarView.setImageDrawable(drawable);
            }
        }
    }

    /**
     * 利用反射获取顶部状态栏高度
     */
    public static int getStatusBarHeight(Activity activity) {
        int result = 0; //获取状态栏高度的资源id
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 隐藏底部导航栏
     */
    public static void hideStatusBarBottom(Window window) {
        if (window != null) {
            final View decorView = window.getDecorView();
            if (decorView != null) {
                final int option = View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(option);
                decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(option);
                        }
                    }
                });
            }
        }
    }

    /**
     * 全屏显示
     */
    public static void hideStatusBar(Window window) {
        if (window != null) {
            //全屏显示
            View view = window.getDecorView();
            if (view != null) {
                view.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    }

}
