package com.mmednet.xyzj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static android.R.attr.action;


/**
 * Title:NemoReceiver
 * <p>
 * Description:
 * </p>
 * Author Jming.L
 * Date 2017/8/21 17:16
 */
public class NemoReceiver extends BroadcastReceiver {

    private XYRobot mRobot = XYRobot.getInstance();
    private static final String TAG = "NemoReceiver";
    //机器拍照
    private static final String ACTION_PICTURE_TAKE = "nemo.intent.action.TAKE_PICTURE";
    //取消拍照
    private static final String ACTION_PICTURE_CANCEL = "nemo.intent.action.CANCEL_TAKE_PICTURE";
    //语音识别
    private static final String ACTION_SST_TAKE = "nemo.intent.action.ASR";
    //人脸识别
    private static final String ACTION_FACE_TAKE = "nemo.intent.action.FACEDETECT";

    //拍照路径
    private static final String INTENT_PICTURE_PATH = "intent_key_pic_path";
    //语音结果
    private static final String INTENT_SST_RESULT = "voice_recognition_str";
    //人脸结果
    private static final String INTENT_FACE_RESULT = "face_detect_result";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        XYCallback picCallback = mRobot.getPicCallback();
        //拍照存储
        if (ACTION_PICTURE_TAKE.equals(action)) {
            mRobot.setUsable(true);
            String picPath = intent.getStringExtra(INTENT_PICTURE_PATH);
            Log.i(TAG, "拍照结果：" + picPath);
            if (picCallback != null) {
                picCallback.onBack(picPath);
            }
        } else if (ACTION_PICTURE_CANCEL.equals(action)) {
            mRobot.setUsable(true);
            Log.i(TAG, "拍照取消");
            if (picCallback != null) {
                picCallback.onBack(null);
            }
        }

        //语音识别
        if (ACTION_SST_TAKE.equals(action)) {
            String result = intent.getStringExtra(INTENT_SST_RESULT);
            Log.i(TAG, "语音识别：" + result);
            XYCallback sstCallback = mRobot.getSpeech2TextCallback();
            if (sstCallback != null) {
                sstCallback.onBack(result);
            }
        }

        //人脸识别
        if (ACTION_FACE_TAKE.equals(action)) {
            mRobot.setUsable(true);
            String result = intent.getStringExtra(INTENT_FACE_RESULT);
            Log.i(TAG, "人脸识别：" + result);
            XYCallback callback = mRobot.getRecognitionOfFaceCallback();
            if (callback != null) {
                callback.onBack(result);
            }
        }
    }
}
