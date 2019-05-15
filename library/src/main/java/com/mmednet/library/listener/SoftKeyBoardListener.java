package com.mmednet.library.listener;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Title:SoftKeyBoardListener
 * <p>
 * Description:软键盘监听器
 * </p>
 * Author Jming.L
 * Date 2019/4/3 17:07
 */
public class SoftKeyBoardListener {

    private View mRootView;//activity的根视图
    private int mVisibleHeight;//纪录根视图的显示高度
    private OnSoftKeyBoardChangeListener mChangeListener;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutListener;

    private SoftKeyBoardListener(Activity activity) {
        //获取activity的根视图
        mRootView = activity.getWindow().getDecorView();
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                mRootView.getWindowVisibleDisplayFrame(rect);
                int visibleHeight = rect.height();
                if (mVisibleHeight == 0) {
                    mVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (mVisibleHeight == visibleHeight) {
                    return;
                }
                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (mVisibleHeight - visibleHeight > 200) {
                    if (mChangeListener != null) {
                        mChangeListener.keyBoardShow(mVisibleHeight - visibleHeight);
                    }
                    mVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - mVisibleHeight > 200) {
                    if (mChangeListener != null) {
                        mChangeListener.keyBoardHide(visibleHeight - mVisibleHeight);
                    }
                    mVisibleHeight = visibleHeight;
                }
            }
        };
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);
    }

    private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.mChangeListener = onSoftKeyBoardChangeListener;
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }

    public static void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(activity);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }

}
