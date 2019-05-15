package com.mmednet.library.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.mmednet.library.robot.voice.VoiceBinder;

/**
 * Title:CustomFragment
 * <p>
 * Description:
 * </p>
 * Author Jming.L
 * Date 2017/12/18 14:05
 */
public abstract class CustomFragment extends Fragment implements Layout {

    protected String TAG = this.getClass().getSimpleName();

    private OnLayoutListener mLayoutListener;
    private OnActivityResultListener mOnActivityResultListener;

    {
        VoiceBinder.init(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mLayoutListener != null)
            mLayoutListener.onCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLayoutListener != null)
            mLayoutListener.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLayoutListener != null)
            mLayoutListener.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLayoutListener != null)
            mLayoutListener.onDestroy();
    }

    @Override
    public final void setOnLayoutListener(OnLayoutListener layoutListener) {
        mLayoutListener = layoutListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mOnActivityResultListener != null) {
            mOnActivityResultListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setOnActivityResultListener(OnActivityResultListener listener) {
        mOnActivityResultListener = listener;
    }

}
