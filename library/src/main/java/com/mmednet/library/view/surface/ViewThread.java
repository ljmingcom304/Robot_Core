package com.mmednet.library.view.surface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ViewThread {

    private SurfaceHolder mHolder;
    private boolean isRun;
    private ExecutorService service;
    private OnRunListener mListener;
    private Runnable run = new Runnable() {

        @Override
        public void run() {
            while (isRun) {
                Canvas canvas = null;
                try {
                    synchronized (this) {
                        canvas = mHolder.lockCanvas();// 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                        canvas.drawColor(Color.TRANSPARENT,
                                android.graphics.PorterDuff.Mode.CLEAR);// 清屏
                        mListener.onRun(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        mHolder.unlockCanvasAndPost(canvas);// 结束锁定画图，并提交改变。
                    }
                }
                try {
                    Thread.sleep(100);// 控制帧率
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    interface OnRunListener {
        void onRun(Canvas canvas);
    }

    public ViewThread() {
        this(null);
    }

    public ViewThread(SurfaceHolder holder) {
        this.mHolder = holder;
        isRun = false;
        service = Executors.newSingleThreadExecutor();
    }

    public boolean isRun() {
        return isRun;
    }

    /**
     * 开启线程
     */
    public void startThread() {
        isRun = true;
        if (!service.isShutdown()) {
            service.execute(run);
        }
    }

    /**
     * 终止线程
     */
    public void stopThread() {
        isRun = false;
    }

    public void setOnRunListener(OnRunListener listener) {
        this.mListener = listener;
    }

    public SurfaceHolder getHolder() {
        return mHolder;
    }

    public void setHolder(SurfaceHolder mHolder) {
        this.mHolder = mHolder;
    }

}
