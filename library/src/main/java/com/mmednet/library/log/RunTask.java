package com.mmednet.library.log;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Title:RunTask
 * <p>
 * Description:可中断执行的任务
 * </p>
 * Author Jming.L
 * Date 2017/8/29 11:56
 */
abstract class RunTask<U, R> implements Runnable{
    public Object[] objs;

    protected RunTask(Object... objs) {
        this.objs = objs;
    }

    private static final int TASK_BEFORE_UI = 0X001;
    private static final int TASK_UPDATE_UI = 0x002;
    private static final int TASK_RESULT = 0x003;

    private boolean isCanceled;

    protected Handler rHandler = new MyHandler(Looper.getMainLooper(), this);

    private class MyHandler extends Handler {
        WeakReference<RunTask<U, R>> runtaskWeakReference;

        public MyHandler(Looper looper, RunTask<U, R> runtask) {
            super(looper);
            this.runtaskWeakReference = new WeakReference<>(runtask);
        }

        @SuppressWarnings("unchecked")
		@Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RunTask<U, R> runtask = runtaskWeakReference.get();
            if (null == runtask) {
                return;
            }
            switch (msg.arg1) {
                case TASK_BEFORE_UI:
                    runtask.onBefore();
                    break;
                case TASK_UPDATE_UI:
                    runtask.onUpdateUICallBack((U) msg.obj);
                    break;
                case TASK_RESULT:
                    runtask.onResult((R) msg.obj);
                    break;
            }

        }
    }

    @Override
    public void run() {
        if (isCanceled) {
            return;
        }

        Message msg = rHandler.obtainMessage();
        msg.arg1 = TASK_BEFORE_UI;
        rHandler.sendMessage(msg);

        if (isCanceled) {
            return;
        }
        R result = runInBackground();
        if (isCanceled) {
            return;
        }

        msg = rHandler.obtainMessage();
        msg.arg1 = TASK_RESULT;
        msg.obj = result;
        rHandler.sendMessage(msg);
    }

    public abstract R runInBackground();

    public void updateUI(U obj) {
        Message msg = rHandler.obtainMessage();
        msg.arg1 = TASK_UPDATE_UI;
        msg.obj = obj;
        rHandler.sendMessage(msg);
    }

    protected void onBefore() {
    }

    protected void onUpdateUICallBack(U obj) {
    }

    protected void onResult(R result) {
    }

    /**终止任务*/
    public void cancel() {
        if (!isCanceled) {
            isCanceled = true;
        }
    }

    /**终止运行*/
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (mayInterruptIfRunning) {
            Thread.currentThread().interrupt();
        }
        cancel();
        return true;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

}
