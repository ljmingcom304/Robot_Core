package com.mmednet.baidu.wakeup;

import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;
import com.mmednet.baidu.Message;
import com.mmednet.baidu.asr.ErrorTranslation;

/**
 * Title:WakeupListener
 * <p>
 * Description:唤醒监听
 * </p>
 * Author Jming.L
 * Date 2018/8/1 16:11
 */
public abstract class WakeupListener implements EventListener, Message {

    private static final String TAG = WakeupListener.class.getSimpleName();

    @Override
    public final void onEvent(String name, String params, byte[] data, int offset, int length) {
        Log.i("TAG", "名称标识:" + name + "; 返回数据:" + params);
        if (SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS.equals(name)) { // 识别唤醒词成功
            WakeupResult result = WakeupResult.parseJson(name, params);
            int errorCode = result.getErrorCode();
            if (result.hasError()) { // error不为0依旧有可能是异常情况
                Log.e(TAG, ErrorTranslation.wakeupError(errorCode));
                this.onResult(FAILURE, ErrorTranslation.wakeupError(errorCode));
            } else {
                String word = result.getWord();
                Log.i(TAG, word);
                this.onResult(SUCCESS, word);
            }
        }

        if (SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR.equals(name)) { // 识别唤醒词报错
            WakeupResult result = WakeupResult.parseJson(name, params);
            int errorCode = result.getErrorCode();
            if (result.hasError()) {
                Log.e(TAG, ErrorTranslation.wakeupError(errorCode));
                this.onResult(FAILURE, ErrorTranslation.wakeupError(errorCode));
            }
        }

        if (SpeechConstant.CALLBACK_EVENT_WAKEUP_STOPED.equals(name)) { // 关闭唤醒词
            Log.i(TAG, "唤醒停止");
        }

        if (SpeechConstant.CALLBACK_EVENT_WAKEUP_AUDIO.equals(name)) { // 音频回调
            Log.i(TAG, "音频回调（基本不用）");
        }
    }

}
