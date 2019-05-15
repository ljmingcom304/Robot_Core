package com.mmednet.library.robot.manage;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mmednet.baidu.asr.AsrListener;
import com.mmednet.baidu.asr.AsrManager;
import com.mmednet.baidu.tts.TtsListener;
import com.mmednet.baidu.tts.TtsManager;
import com.mmednet.baidu.wakeup.Wakeup;
import com.mmednet.baidu.wakeup.WakeupListener;


/**
 * Title:BaiduManager
 * <p>
 * Description:百度
 * </p>
 * Author Jming.L
 * Date 2018/12/20 11:12
 */
public class BaiduManager implements Manager {

    private Wakeup mWakeup;
    private AsrManager mAsrManager;
    private TtsManager mTtsManager;

    private Callback mTtsCallback;
    private Callback mAsrCallback;
    private int mCount;

    private static final int STATUS_WAKEUP = 0;     //执行唤醒后的操作
    private static final int STATUS_REWAKEUP = 1;   //再次唤醒
    private static final int STATUS_ASREND = 2;     //表示第一次语音识别结束后的操作
    private static final int STATUS_REASR = 3;      //表示再次开启语音识别
    private static final String TAG = BaiduManager.class.getSimpleName();

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //唤醒成功后1秒开始识别，唤醒失败后5秒继续唤醒
            if (msg.what == STATUS_WAKEUP) {
                wakeUp(true);
            }
            if (msg.what == STATUS_REWAKEUP) {
                mWakeup.start();
            }
            if (msg.what == STATUS_REASR) {
                mAsrManager.start();
                mHandler.sendEmptyMessageDelayed(STATUS_ASREND, 5000);
            }
            if (msg.what == STATUS_ASREND) {
                mAsrManager.stop();
                //当重复3次没有接收到任何语音时或已经接收到语音时，退出语音识别重试
                if (mCount < 5) {
                    Log.w(TAG, "语音识别重连" + mCount + "次");
                    //延迟发送解决引擎忙的问题
                    mHandler.sendEmptyMessageDelayed(STATUS_REASR, 1000);
                    mCount++;
                } else {
                    Log.w(TAG, "语音识别停止重连");
                }
            }
        }
    };

    private WakeupListener mWakeupListener = new WakeupListener() {
        @Override
        public void onResult(int status, String word) {
            if (status == WakeupListener.SUCCESS) {
                mHandler.sendEmptyMessageDelayed(STATUS_WAKEUP, 1000);
                if (mAsrCallback != null) {
                    mAsrCallback.onResult(Callback.WAKEUP, word);
                }
            } else if (status == WakeupListener.FAILURE) {
                mHandler.sendEmptyMessageDelayed(STATUS_REWAKEUP, 5000);
                if (mAsrCallback != null) {
                    mAsrCallback.onResult(Callback.FAILURE, word);
                }
            }
        }
    };

    private AsrListener mAsrListener = new AsrListener() {
        @Override
        public void onResult(int status, String word) {
            if (status == WakeupListener.SUCCESS) {
                //当有返回成功的语音结果后开启下一轮语音识别
                wakeUp(true);
                if (mAsrCallback != null) {
                    mAsrCallback.onResult(Callback.SUCCESS, word);
                }
            } else if (status == WakeupListener.PROGRESS) {
                //当有结果返回时不再重试
                mCount = 0;
                mHandler.removeMessages(STATUS_ASREND);
                mHandler.removeMessages(STATUS_REASR);
                if (mAsrCallback != null) {
                    mAsrCallback.onResult(Callback.PROGRESS, word);
                }
            } else if (status == WakeupListener.FAILURE) {
                if (mAsrCallback != null) {
                    mAsrCallback.onResult(Callback.FAILURE, word);
                }
            }
        }
    };

    private TtsListener mTtsListener = new TtsListener() {
        @Override
        public void onResult(int status, String word) {
            if (status == WakeupListener.SUCCESS) {
                if (mTtsCallback != null) {
                    mTtsCallback.onResult(Callback.SUCCESS, word);
                }
            } else if (status == WakeupListener.FAILURE) {
                if (mTtsCallback != null) {
                    mTtsCallback.onResult(Callback.FAILURE, word);
                }
            }
        }
    };

    @Override
    public void init(Context context) {
        mWakeup = Wakeup.init(context);
        mWakeup.start();
        mWakeup.setWakeupListener(mWakeupListener);

        mAsrManager = AsrManager.init(context);
        mAsrManager.setAsrListener(mAsrListener);

        mTtsManager = TtsManager.init(context);
        mTtsManager.setTtsListener(mTtsListener);
    }

    @Override
    public void release(Context context) {
        mWakeup.release();
        mAsrManager.release();
        mTtsManager.release();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void text2Speech(String text, Callback callback) {
        this.mTtsCallback = callback;
        mTtsManager.start(text);
    }

    @Override
    public void speech2Text(Callback callback) {
        this.mAsrCallback = callback;
    }


    @Override
    public synchronized void wakeUp(boolean wakeup) {
        mCount = 0;
        mHandler.removeMessages(STATUS_ASREND);
        mHandler.removeMessages(STATUS_REASR);
        if (wakeup) {
            //5秒后不说话百度语音会崩溃，所以要及时关闭
            mAsrManager.start();
            mHandler.sendEmptyMessageDelayed(STATUS_ASREND, 5000);
        } else {
            mAsrManager.stop();
        }
    }

}
