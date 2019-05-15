package com.mmednet.library.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mmednet.library.R;
import com.mmednet.library.util.WindowUtils;

/**
 * Title:FragmentDialog
 * <p>
 * Description:Fragment弹窗（避免声明为成员变量）
 * </p>
 * Author Jming.L
 * Date 2017/10/19 16:02
 */
public abstract class FragmentDialog extends DialogFragment implements LayoutDialog {

    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private OnCloseListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return onCreateView(inflater, container);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        final View view = inflater.inflate(setLayout(), container, false);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            initWindow(dialog.getWindow());
        }
    }

    protected void initWindow(Window window) {
        if (window != null) {
            WindowUtils.hideStatusBar(window);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = mWidth;
            attributes.height = mHeight;
            window.setAttributes(attributes);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onClose();
        }
    }

    public Window getWindow() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            return dialog.getWindow();
        }
        return null;
    }

    public Context getLayoutContext() {
        return getActivity();
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public void show(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(this);
        transaction.commitAllowingStateLoss();
        //TODO  Can not perform this action after onSaveInstanceState
        super.show(manager, this.getTag());
    }

    @Override
    public void setOnCloseListener(OnCloseListener listener) {
        mListener = listener;
    }

    @Override
    public void dismiss() {
        //解决Activity状态无法保存崩溃的异常
        super.dismissAllowingStateLoss();
    }
}
