package com.mmednet.baidu.tts;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.baidu.tts.aop.tts.TtsError;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.mmednet.baidu.utils.AppUtils;


/**
 * Title:TtsManager
 * <p>
 * Description:语音合成
 * </p>
 * Author Jming.L
 * Date 2018/8/1 16:34
 */
public class TtsManager {

    private String TAG = TtsManager.class.getSimpleName();

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private TtsMode ttsMode = TtsMode.MIX;
    private SpeechSynthesizer mSpeechSynthesizer;

    private TtsManager() {

    }

    public static TtsManager init(Context context) {
        TtsManager ttsManager = new TtsManager();
        LoggerProxy.printable(true); // 日志打印在logcat中

        ttsManager.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        ttsManager.mSpeechSynthesizer.setContext(context);
        ttsManager.mSpeechSynthesizer.setAppId(AppUtils.getAppId(context));
        ttsManager.mSpeechSynthesizer.setApiKey(AppUtils.getApiKey(context), AppUtils.getSecretKey(context));

        //离在线混合模式需要设置离线模型
        if (ttsManager.ttsMode.equals(TtsMode.MIX)) {
            OfflineResource offlineResource = new OfflineResource(context, OfflineResource.VOICE_FEMALE);
            if (ttsManager.checkOfflineAuth()) {
                // 文本模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
                ttsManager.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
                // 声学模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
                ttsManager.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());
            } else {
                ttsManager.ttsMode = TtsMode.ONLINE;
            }
        }

        //选填语音参数，不填写默认值生效
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        ttsManager.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        ttsManager.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        // 设置合成的语速，0-9 ，默认 5
        ttsManager.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        ttsManager.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        ttsManager.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        ttsManager.mSpeechSynthesizer.setAudioStreamType(AudioManager.MODE_IN_CALL);

        //语音合成初始化
        ttsManager.mSpeechSynthesizer.initTts(ttsManager.ttsMode);
        return ttsManager;
    }

    public void setTtsListener(TtsListener listener) {
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);
    }


    /**
     * 开始播放
     */
    public int start(String text) {
        return mSpeechSynthesizer.speak(text);
    }

    /**
     * 停止播放
     */
    public int stop() {
        return mSpeechSynthesizer.stop();
    }

    /**
     * 恢复播放
     */
    public int resume() {
        return mSpeechSynthesizer.resume();
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
            mSpeechSynthesizer.release();
            mSpeechSynthesizer = null;
        }
    }

    //检查离线授权
    private boolean checkOfflineAuth() {
        AuthInfo authInfo = mSpeechSynthesizer.auth(ttsMode);
        if (!authInfo.isSuccess()) {
            TtsError ttsError = authInfo.getTtsError();
            String errorMsg = ttsError.getDetailMessage();
            Log.e(TAG, "离线鉴权失败：" + errorMsg);
            return false;
        }
        return true;
    }

}
