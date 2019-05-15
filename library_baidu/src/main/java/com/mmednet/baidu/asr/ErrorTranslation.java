package com.mmednet.baidu.asr;

import android.speech.SpeechRecognizer;

/**
 * Title:ErrorTranslation
 * <p>
 * Description:错误
 * </p>
 * Author Jming.L
 * Date 2018/8/1 15:59
 */
public class ErrorTranslation {

    public static String recogError(int errorCode) {
        if (errorCode == SpeechRecognizer.ERROR_AUDIO) {
            return "音频问题";
        }
        if (errorCode == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
            return "没有语音输入";
        }

        if (errorCode == SpeechRecognizer.ERROR_CLIENT) {
            return "其它客户端错误";
        }
        if (errorCode == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
            return "权限不足";
        }
        if (errorCode == SpeechRecognizer.ERROR_NETWORK) {
            return "网络问题";
        }
        if (errorCode == SpeechRecognizer.ERROR_NO_MATCH) {
            return "没有匹配的识别结果";
        }
        if (errorCode == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
            return "引擎忙";
        }
        if (errorCode == SpeechRecognizer.ERROR_SERVER) {
            return "服务端错误";
        }
        if (errorCode == SpeechRecognizer.ERROR_NETWORK_TIMEOUT) {
            return "连接超时";
        }
        return "未知错误:" + errorCode;
    }

    public static String wakeupError(int errorCode) {
        if (errorCode == 1) {
            return "参数错误";
        }
        if (errorCode == 2) {
            return "网络请求发生错误";
        }
        if (errorCode == 3) {
            return "服务器数据解析错误";
        }
        if (errorCode == 4) {
            return "网络不可用";
        }
        return "未知错误:" + errorCode;
    }
}
