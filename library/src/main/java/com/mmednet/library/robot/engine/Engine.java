package com.mmednet.library.robot.engine;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mmednet.library.log.Logger;
import com.mmednet.library.robot.correct.Standard;
import com.mmednet.library.robot.engine.action.Action;
import com.mmednet.library.robot.manage.Callback;
import com.mmednet.library.robot.manage.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:Engine
 * <p>
 * Description:语音交互引擎
 * </p>
 * Author Jming.L
 * Date 2017/9/4 13:37
 */
public class Engine {

    private static final String TAG = "Engine";
    private Action action;
    private Manager manager;
    private Standard standard;
    private String result;
    private OnListener listener;
    private List<OnListener> listeners;
    private Callback callback = new Callback() {
        @Override
        public void onResult(int status, String result) {
            //处理语音回调的消息
            if (status == Callback.SUCCESS) {
                dispatchVoice(result);
            }
            if (status == Callback.WAKEUP) {
                wakeupVoice(result);
            }
            if (status == Callback.PROGRESS) {
                printVoice(result);
            }
            if (status == Callback.FAILURE) {
                Log.e(TAG, result);
            }
        }
    };
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String message = (String) msg.obj;
            //最新添加的优先级最高，以前添加的优先级最低
            if (!handleVoice(message)) {
                if (!action.execute(message)) {
                    result = message;
                }
            }
        }
    };

    public Engine(Manager manager) {
        this(null, manager);
    }

    public Engine(WholeVoice voice, Manager manager) {
        this.standard = new Standard();
        this.listeners = new ArrayList<>();
        this.manager = manager;
        this.manager.speech2Text(callback);
        if (voice != null) {
            action = new Action(voice);
        } else {
            Logger.e(TAG, "WholeVoice is null!");
        }
    }

    /**
     * 消息调度
     */
    private void dispatchVoice(String voice) {
        Log.i(TAG, "result=" + voice);
        voice = standard.correct(voice.trim());

        if (action != null) {
            Message msg = handler.obtainMessage();
            msg.obj = voice;
            handler.sendMessage(msg);
        } else {
            handleVoice(voice);
        }
    }

    /**
     * 语音识别
     */
    public void recognizeVoice(boolean recognize) {
        manager.wakeUp(recognize);
    }

    /**
     * 文本转语音
     */
    public void playVoice(String voice) {
        manager.text2Speech(voice, null);
    }

    private void wakeupVoice(String voice) {
        boolean isListener = false;
        if (listener != null) {
            isListener = listener.onWakeup(voice);
        }
        //新挂载的数据优先级最高
        if (!isListener) {
            for (int i = listeners.size() - 1; i >= 0; i--) {
                OnListener listener = listeners.get(i);
                if (listener.onWakeup(voice)) break;
            }
        }
    }

    /**
     * 处理临时消息
     */
    private void printVoice(String voice) {
        boolean isListener = false;
        if (listener != null) {
            isListener = listener.onProgress(voice);
        }
        //新挂载的数据优先级最高
        if (!isListener) {
            for (int i = listeners.size() - 1; i >= 0; i--) {
                OnListener listener = listeners.get(i);
                if (listener.onProgress(voice)) break;
            }
        }
    }

    /**
     * 处理最终消息
     */
    private boolean handleVoice(String voice) {
        boolean isListener = false;
        if (listener != null) {
            isListener = listener.onResult(voice);
        }
        //新挂载的数据优先级最高
        if (!isListener) {
            for (int i = listeners.size() - 1; i >= 0; i--) {
                OnListener listener = listeners.get(i);
                isListener = listener.onResult(voice);
                if (isListener) break;
            }
        }
        return isListener;
    }

    /**
     * 设置语音交互监听
     *
     * @param listener 监听器
     */
    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    /**
     * 挂载语音交互监听
     *
     * @param listener 监听器
     */
    public void attachOnListener(OnListener listener) {
        listeners.add(listener);
        //如果结果始终没有被消费，则交由新挂载的监听去消费
        if (result != null) {
            listener.onResult(result);
            result = null;
        }
    }

    /**
     * 卸载语音交互监听
     *
     * @param listener 监听器
     */
    public void detachOnListener(OnListener listener) {
        listeners.remove(listener);
    }

}
