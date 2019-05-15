package com.mmednet.library.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.mmednet.library.R;
import com.mmednet.library.util.WindowUtils;

/**
 * Title:BaseDialog
 * <p>
 * Description:基础弹窗
 * </p>
 * Author Jming.L
 * Date 2017/9/26 10:37
 */
public abstract class BaseDialog extends Dialog implements LayoutDialog {

    private Context mContext;
    private OnCloseListener mCloseListener;
    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    public BaseDialog(Context context) {
        this(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }


    @Override
    protected void onStart() {
        super.onStart();
        initWindow(getWindow());//对话框显示后再设置窗体才有效果
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCloseListener != null) {
            mCloseListener.onClose();
        }
    }


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(onCreateView(mContext));
    }

    protected View onCreateView(Context context) {
        final View view = View.inflate(context, setLayout(), null);
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver observer = view.getViewTreeObserver();
                observer.removeOnGlobalLayoutListener(this);
                Window window = getWindow();
                if (window != null) {
                    WindowManager.LayoutParams attributes = window.getAttributes();
                    if (mWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                        attributes.width = view.getWidth();
                    }
                    if (mHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                        attributes.height = view.getHeight();
                    }
                    window.setAttributes(attributes);
                }
            }
        });
        initView(view);
        initData();
        return view;
    }

    protected void initWindow(Window window) {
        if (window != null) {
            WindowUtils.hideStatusBar(window);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = mWidth;
            attributes.height = mHeight;
            window.setAttributes(attributes);
        }
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isOutOfBounds(event)) {
            onTouchOutside(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 点击窗体外部区域
     */
    public void onTouchOutside(MotionEvent event) {

    }

    private boolean isOutOfBounds(MotionEvent event) {
        //相对弹窗左上角的x坐标
        final int x = (int) event.getX();
        //相对弹窗左上角的y坐标
        final int y = (int) event.getY();
        //最小识别距离
        final int slop = ViewConfiguration.get(getContext()).getScaledWindowTouchSlop();
        Window window = getWindow();
        if (window != null) {
            View decorView = window.getDecorView();//弹窗的根View
            return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                    || (y > (decorView.getHeight() + slop));
        }
        return false;
    }

    @Override
    public Context getLayoutContext() {
        return mContext;
    }

    public void setOnCloseListener(OnCloseListener listener) {
        mCloseListener = listener;
    }

}
