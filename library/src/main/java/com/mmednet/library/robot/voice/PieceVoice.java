package com.mmednet.library.robot.voice;

import android.util.Log;

import com.mmednet.library.layout.Layout;
import com.mmednet.library.layout.OnLayoutListener;
import com.mmednet.library.robot.Robot;
import com.mmednet.library.robot.engine.Engine;
import com.mmednet.library.robot.engine.OnListener;
import com.mmednet.library.robot.manage.Manager;

/**
 * Title:PieceVoice
 * <p>
 * Description:局部语音动作，通过在Activity或Fragment上添加BindVoice注解的方式动态绑定
 * </p>
 * Author Jming.L
 * Date 2018/3/21 18:01
 */
public abstract class PieceVoice<T extends Layout> implements OnLayoutListener {

    private T t;
    private Engine mEngine = Robot.getEngine();
    private static final String TAG = PieceVoice.class.getSimpleName();
    private OnListener mListener = new OnListener() {
        @Override
        public boolean onResult(String result) {
            return PieceVoice.this.onResult(t, result);
        }

        @Override
        public boolean onProgress(String result) {
            return PieceVoice.this.onProgress(result);
        }

        @Override
        public boolean onWakeup(String result) {
            return PieceVoice.this.onWakeup(result);
        }
    };

    final void bind(T t) {
        this.t = t;
        this.t.setOnLayoutListener(this);
    }

    public T getLayout() {
        return t;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {
        try {
            mEngine.attachOnListener(mListener);
            Log.i(TAG, "attach " + t.getClass().getName());
        } catch (Exception e) {
            Log.e(TAG, "Robot is not initialized!", e);
        }
    }

    @Override
    public void onPause() {
        try {
            mEngine.detachOnListener(mListener);
            Log.i(TAG, "detach " + t.getClass().getName());
        } catch (Exception e) {
            Log.e(TAG, "Robot is not initialized!", e);
        }
    }

    @Override
    public void onDestroy() {
        //页面销毁时停止语音识别
        mEngine.recognizeVoice(false);
    }

    protected abstract boolean onResult(T t, String result);

    protected boolean onProgress(String result) {
        return false;
    }

    protected boolean onWakeup(String result) {
        return false;
    }

    /**
     * 获取语音引擎
     */
    public Engine getEngine() {
        return mEngine;
    }

}
