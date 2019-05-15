package com.mmednet.baidu.asr;

import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;
import com.mmednet.baidu.Message;

/**
 * Title:AsrListener
 * <p>
 * Description:语音识别监听
 * </p>
 * Author Jming.L
 * Date 2018/8/1 16:52
 */
public abstract class AsrListener implements EventListener, Message {

    private static final String TAG = AsrListener.class.getSimpleName();
    private boolean enable;

    /**
     * 语音识别是否启动
     */
    public boolean isEnable() {
        return enable;
    }

    @Override
    public final void onEvent(String name, String params, byte[] data, int offset, int length) {
        Log.i(TAG, "名称标识:" + name + "; 返回数据:" + params);

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
            Log.i(TAG, "准备就绪可以说话");//1
            enable = true;
        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN)) {
            this.onResult(STARTED, null);
            Log.i(TAG, "用户开始说话");//2
        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            AsrResult recogResult = AsrResult.parseJson(params);
            // 临时识别结果, 长语音模式需要从此消息中取出结果
            String[] results = recogResult.getResultsRecognition();

            if (recogResult.isPartialResult()) {
                //可能返回多个结果，请取第一个结果
                Log.i(TAG, "临时识别结果：" + results[0]);//3
                this.onResult(PROGRESS, results[0]);
            }

            if (recogResult.isFinalResult()) {
                //可能返回多个结果，请取第一个结果
                Log.i(TAG, "最终识别结果：" + results[0]);//5
                this.onResult(SUCCESS, results[0]);
            }

            if (recogResult.isNluResult()) {
                Log.i("临时识别结果", new String(data, offset, length));
            }

        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_END)) {
            this.onResult(STOPED, null);
            Log.i(TAG, "用户停止说话");//4
            enable = false;
        }


        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            // 识别结束， 最终识别结果或可能的错误
            AsrResult recogResult = AsrResult.parseJson(params);
            if (recogResult.hasError()) {
                int errorCode = recogResult.getError();
                int subErrorCode = recogResult.getSubError();
                Log.e(TAG, "识别错误:" + ErrorTranslation.recogError(errorCode));
                this.onResult(FAILURE, ErrorTranslation.recogError(errorCode));
            } else {
                Log.i(TAG, "识别一段话结束。如果是长语音的情况会继续识别下段话。");//6
            }
        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_EXIT)) {
            Log.i(TAG, "语音识别退出");//7
        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_LOADED)) {
            Log.i(TAG, "离线资源加载成功");
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_UNLOADED)) {
            Log.i(TAG, "离线资源卸载成功");
        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH)) {
            Log.i(TAG, "长语音结束");
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_VOLUME)) {
            Log.i(TAG, "音量数据=" + params);
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_AUDIO)) {
            Log.i(TAG, "声音回调");
        }
    }

}
