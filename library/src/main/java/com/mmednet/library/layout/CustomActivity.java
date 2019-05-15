package com.mmednet.library.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mmednet.library.robot.voice.VoiceBinder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Title:CustomActivity
 * <p>
 * Description:
 * </p>
 * Author Jming.L
 * Date 2017/12/18 14:35
 */
public abstract class CustomActivity extends FragmentActivity implements Layout {

    protected String TAG = this.getClass().getSimpleName();
    private OnLayoutListener mLayoutListener;
    private OnActivityResultListener mOnActivityResultListener;
    //上次点击事件
    private long lastClickTime;
    //防止重复点击设置的标志，涉及到点击打开其他Activity时，将该标志设置为false，在onResume事件中设置为true
    private boolean clickable;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setClickable(true);
        }
    };

    public CustomActivity() {
        VoiceBinder.init(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.clickable = true;
        if (mLayoutListener != null)
            mLayoutListener.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setClickable(true);
        if (mLayoutListener != null)
            mLayoutListener.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLayoutListener != null)
            mLayoutListener.onPause();
    }

    @Override
    protected void onStop() {
        setClickable(true);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLayoutListener != null)
            mLayoutListener.onDestroy();
    }

    @Override
    public final void setOnLayoutListener(OnLayoutListener layoutListener) {
        mLayoutListener = layoutListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
            if (isShouldHideInput(getCurrentFocus())) {
                return true;
            }
            //解决快速点击重复打开页面的问题
            if (isFastClick()) {
                return true;
            }
            if (!isClickable()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint( "RestrictedApi" )
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        setClickable(false);
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void finish() {
        setClickable(false);
        super.finish();
    }

    /**
     * 设置点击状态
     */
    private void setClickable(boolean clickable) {
        this.clickable = clickable;
        //5秒后自动取消不可点击状态
        if (!clickable) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 5000);
        }
    }

    /**
     * 当前是否可以点击
     */
    private boolean isClickable() {
        return clickable;
    }

    private boolean isShouldHideInput(View v) {
        if (v != null && (v instanceof EditText)) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isActive()) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            return false;
        }
        return false;
    }

    private boolean isFastClick() {
        long time = System.currentTimeMillis();
        long space = time - lastClickTime;
        lastClickTime = time;
        return space <= 200;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mOnActivityResultListener != null) {
            mOnActivityResultListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setOnActivityResultListener(OnActivityResultListener listener) {
        mOnActivityResultListener = listener;
    }
}
