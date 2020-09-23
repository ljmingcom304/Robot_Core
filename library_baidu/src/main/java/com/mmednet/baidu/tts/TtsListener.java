package com.mmednet.baidu.tts;

import android.util.Log;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.mmednet.baidu.Message;

/**
 * Title:TtsListener
 * <p>
 * Description:TTS回调
 * </p>
 * Author Jming.L
 * Date 2018/8/1 16:30
 */
public abstract class TtsListener implements SpeechSynthesizerListener, Message {

    private static final String TAG = TtsListener.class.getSimpleName();

    @Override
    public final void onSynthesizeStart(String utteranceId) {
        Log.i(TAG, "准备开始合成,序列号:" + utteranceId);
    }

    /**
     * 语音流 16K采样率 16bits编码 单声道 。
     *
     * @param utteranceId
     * @param bytes       二进制语音 ，注意可能有空data的情况，可以忽略
     * @param progress    如合成“百度语音问题”这6个字， progress肯定是从0开始，到6结束。 但progress无法和合成到第几个字对应。
     *                    engineType 下版本提供。1:音频数据由离线引擎合成； 0：音频数据由在线引擎（百度服务器）合成。
     */
    public void onSynthesizeDataArrived(String utteranceId, byte[] bytes, int progress) {
        Log.i(TAG, "合成进度回调, progress：" + progress + ";序列号:" + utteranceId);
        // + ";" + (engineType == 1? "离线合成":"在线合成"));
    }

    @Override
    // engineType 下版本提供。1:音频数据由离线引擎合成； 0：音频数据由在线引擎（百度服务器）合成。
    public void onSynthesizeDataArrived(String utteranceId, byte[] bytes, int progress, int engineType) {
        onSynthesizeDataArrived(utteranceId, bytes, progress);
    }


    //每句合成正常结束都会回调，如果过程中出错，则回调onError，不再回调此接口
    @Override
    public final void onSynthesizeFinish(String utteranceId) {
        Log.i(TAG, "合成结束回调,序列号:" + utteranceId);
        this.onResult(STARTED, utteranceId);
    }

    @Override
    public final void onSpeechStart(String utteranceId) {
        Log.i(TAG, "播放开始回调,序列号:" + utteranceId);
        this.onResult(SUCCESS, utteranceId);
    }

    @Override
    public final void onSpeechProgressChanged(String utteranceId, int progress) {
        Log.i(TAG, "播放进度回调, 进度：" + progress + ";序列号:" + utteranceId);
    }

    @Override
    public final void onSpeechFinish(String utteranceId) {
        Log.i(TAG, "播放结束回调, 序列号:" + utteranceId);
        this.onResult(STOPED, utteranceId);
    }

    @Override
    public final void onError(String utteranceId, SpeechError speechError) {
        Log.e(TAG, "错误发生：" + speechError.description + "，错误编码：" + speechError.code + "，序列号:" + utteranceId);
        this.onResult(FAILURE, utteranceId);
    }


}
