package com.mmednet.library.view.surface;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

abstract class ICallback implements SurfaceHolder.Callback {

    private static final String TAG = ICallback.class.getSimpleName();
    private ViewThread mThread;
    private int width;
    private int height;

    public ICallback(SurfaceHolder holder) {
        mThread = new ViewThread(holder);
        mThread.setOnRunListener(new ViewThread.OnRunListener() {

            @Override
            public void onRun(Canvas canvas) {
                draw(canvas);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        start();
    }

    public abstract void draw(Canvas canvas);

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.i(TAG, "surfaceChanged");
        this.width = width;
        this.height = height;
    }

    @Override
    public synchronized void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        cancel();
    }

    public void start() {
        if (mThread != null && !mThread.isRun()) {
            mThread.startThread();
        }
    }

    public void cancel() {
        if (mThread != null) {
            mThread.stopThread();
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
