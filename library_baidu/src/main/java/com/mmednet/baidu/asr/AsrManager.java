package com.mmednet.baidu.asr;

import android.content.Context;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Title:AsrManager
 * <p>
 * Description:语音识别
 * </p>
 * Author Jming.L
 * Date 2018/8/1 16:03
 */
public class AsrManager {

    private static final String TAG = AsrManager.class.getSimpleName();
    private boolean isOfflineEngineLoaded = false;
    private EventManager mManager;
    private AsrListener mListener;

    private AsrManager() {
    }


    public static AsrManager init(Context context) {
        AsrManager recognize = new AsrManager();
        recognize.mManager = EventManagerFactory.create(context, "asr");

        return recognize;
    }

    public void setAsrListener(AsrListener listener) {
        this.mListener = listener;
        mManager.registerListener(listener);
    }

    public void start() {
        //如果语音识别已经开启则不再另外开启
        if (mListener != null && mListener.isEnable()) {
            return;
        }
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        //是否回调当前音量数据
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 1000);
        String json = new JSONObject(params).toString();
        mManager.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    /**
     * 提前结束录音等待识别结果。
     */
    public void stop() {
        //如果语音识别已经退出则不另外退出
        if (mListener != null && !mListener.isEnable()) {
            return;
        }
        mManager.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
    }

    /**
     * 取消本次识别，取消后将立即停止不会返回识别结果。
     * cancel 与stop的区别是 cancel在stop的基础上，完全停止整个识别流程，
     */
    public void cancel() {
        if (mManager != null) {
            mManager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        }
    }

    public void release() {
        if (mManager == null) {
            return;
        }
        this.cancel();
        if (isOfflineEngineLoaded) {
            mManager.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
            isOfflineEngineLoaded = false;
        }
        mManager.unregisterListener(mListener);
        mManager = null;
    }

    /**
     * 加载离线引擎
     */
    private void loadOfflineEngine(Map<String, Object> params) {
        String json = new JSONObject(params).toString();
        Log.i(TAG + ".Debug", "loadOfflineEngine params:" + json);
        mManager.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, json, null, 0, 0);
        isOfflineEngineLoaded = true;
        // 没有ASR_KWS_LOAD_ENGINE这个回调表试失败，如缺少第一次联网时下载的正式授权文件。
    }

}
