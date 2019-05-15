package com.mmednet.library.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmednet.library.Library;


/**
 * 界面操作
 */
@SuppressWarnings( "deprecation" )
public class UIUtils {

    private static String oldMsg;
    private static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    /**
     * 提示弹框
     */
    @Deprecated
    public static void showToast(String s) {
        showToast(getContext(), s);
    }

    /**
     * 提示弹框
     */
    public static void showToast(Context context, String s) {
        if (s == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            LinearLayout layout = (LinearLayout) toast.getView();
            if (layout.getChildCount() > 0) {
                TextView textView = (TextView) layout.getChildAt(0);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    /**
     * 提示弹框
     */
    @Deprecated
    public static void showToast(int resId) {
        showToast(getContext().getString(resId));
    }

    /**
     * 提示弹框
     */
    public static void showToast(Context context, int resId) {
        showToast(context.getString(resId));
    }


    /**
     * 获取主线程上下文
     */
    public static Context getContext() {
        //AndroidManifest.xml中配置Application的名字，否则会报空指针
        return Library.getInstance().getContext();
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return new Handler(Looper.getMainLooper());
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    /**
     * 获取View
     */
    @Deprecated
    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 获取View
     */
    public static View inflate(Context context, int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    /**
     * 获取资源
     */
    @Deprecated
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取资源
     */
    public static Resources getResources(Context context) {
        return context.getResources();
    }

    /**
     * 获取文字
     */
    @Deprecated
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字
     */
    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * 获取文字
     */
    @Deprecated
    public static String getString(int resId, Object... args) {
        String content = getResources().getString(resId);
        if (args[0] == null) {
            args[0] = " ";
        }
        content = String.format(content, args);
        return content;
    }

    /**
     * 获取文字
     */
    public static String getString(Context context, int resId, Object... args) {
        String content = context.getResources().getString(resId);
        if (args[0] == null) {
            args[0] = " ";
        }
        content = String.format(content, args);
        return content;
    }

    /**
     * 获取文字数组
     */
    @Deprecated
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    @Deprecated
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(Context context, int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    @Deprecated
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    @Deprecated
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(Context context, int resId) {
        return context.getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    @Deprecated
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(Context context, int resId) {
        return context.getResources().getColorStateList(resId);
    }

    /**
     * 主线程中运行
     */
    public static void runInMainThread(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }


}
