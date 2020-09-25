package com.mmednet.baidu.wakeup;

import android.content.Context;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.baidu.tts.client.SpeechSynthesizer;
import com.mmednet.baidu.utils.AppUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Title:Wakeup
 * <p>
 * Description:语音唤醒
 * </p>
 * Author Jming.L
 * Date 2018/8/1 16:03
 */
public class Wakeup {

    private Context mContext;
    private EventManager mManager;
    private EventListener mListener;

    private Wakeup() {

    }

    public static Wakeup init(Context context) {
        Wakeup wakeup = new Wakeup();
        wakeup.mContext = context;
        wakeup.mManager = EventManagerFactory.create(context, "wp");
        return wakeup;
    }

    public void setWakeupListener(WakeupListener listener) {
        this.mListener = listener;
        mManager.registerListener(listener);
    }

    public void start() {
        Map<String, Object> params = new HashMap<>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets://WakeUp.bin");
        params.put(SpeechConstant.APP_ID, AppUtils.getAppId(mContext));
        String json = new JSONObject(params).toString();
        //加载唤醒词文件
        mManager.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    public void stop() {
        mManager.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
    }

    public void release() {
        this.stop();
        if (mListener != null) {
            mManager.unregisterListener(mListener);
        }
        mManager = null;
    }
}
