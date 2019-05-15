package com.mmednet.xyzj;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import vulture.service.api.ICallback;
import vulture.service.api.INemoAidlInterface;

/**
 * Title:XYRobot
 * <p>
 * Description:小鱼在家
 * </p>
 * Author Jming.L
 * Date 2017/8/30 15:14
 */
public class XYRobot {

    private String TAG = "XYRobot";
    private XYCallback sstCallback;                     // 语音转文本
    private XYCallback ttsCallback;                     // 文本转语音
    private XYCallback faceCallback;                    // 人脸识别
    private XYCallback picCallback;                     // 拍照
    private static XYRobot mManager = new XYRobot();
    private INemoAidlInterface mNemoAidlInterface;
    private boolean isBind = false;                     // 服务是否绑定
    private boolean isUsable = true;                    // 唤醒是否可用
    private long timeMillis;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mNemoAidlInterface = INemoAidlInterface.Stub.asInterface(service);
            // 返回播报状态，需要注册
            if (mNemoAidlInterface != null) {
                try {
                    mNemoAidlInterface.registerListener(mCallback);
                    //setKeepTime(60 * 1000);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "XYRobot=>INemoAidlInterface is null");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mNemoAidlInterface = null;
        }
    };
    // TTS播报完成后回调
    private ICallback.Stub mCallback = new ICallback.Stub() {
        @Override
        public void onSpeakStatusListener(final boolean isCompleted)
                throws RemoteException {
            Log.e(TAG, "TTS播报状态" + isCompleted);
            if (ttsCallback != null) {
                ttsCallback.onBack(isCompleted + "");
            } else {
                Log.e(TAG, "XYRobot=>ttsCallback is null!");
            }
        }
    };

    private XYRobot() {
    }

    public static XYRobot getInstance() {
        return mManager;
    }

    public void init(Context context) {
        Intent intent = new Intent();
        intent.setAction("nemo.intent.action.NEMO_REMOTE_SERVICE");
        intent.setPackage("vulture.app.home");
        isBind = context.bindService(intent, mServiceConnection,
                Context.BIND_AUTO_CREATE);
        Log.i(TAG, TAG + "=>bindService is " + isBind);
    }

    public void close(Context context) {
        try {
            if (mNemoAidlInterface != null) {
                mNemoAidlInterface.unregisterListener(mCallback);
                Log.e(TAG, "XYRobot=>NemoAidlInterface unregister listener");
            } else {
                Log.e(TAG, "XYRobot=>NemoAidlInterface is null");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (isBind && mServiceConnection != null) {
            context.unbindService(mServiceConnection);
            isBind = false;
        }
    }

    public void text2Speech(String text, XYCallback callback) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        try {
            if (mNemoAidlInterface != null) {// 解决文字过长无法播报的问题
                mNemoAidlInterface.interruptTts(true);
                mNemoAidlInterface.textToSpeech(text);// tts播报，参数二表示打断前一次TTS
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRecognitionOfFace(Activity activity, String path,
                                       XYCallback callback) {
        setUsable(false);
        faceCallback = callback;
        Bundle bundle = new Bundle();
        Log.i("XYRobot", path);
        bundle.putString("face_path", path);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClassName("vulture.app.home",
                "vulture.app.face.NemoFaceDetectActivity");
        activity.startActivity(intent);
    }

    public void speech2Text(XYCallback callback) {
        sstCallback = callback;
    }

    /**
     * 语音转文本回调
     */
    final XYCallback getSpeech2TextCallback() {
        return sstCallback;
    }

    /**
     * 人脸识别回调
     */
    final XYCallback getRecognitionOfFaceCallback() {
        return faceCallback;
    }

    /**
     * 拍照回调
     */
    final XYCallback getPicCallback() {
        return picCallback;
    }

    @Deprecated
    public void startChat() {
        try {
            if (mNemoAidlInterface != null) {
                mNemoAidlInterface.enableSpeech(true);// 是否开启机器人聊天唤醒
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void stopChat() {
        try {
            if (mNemoAidlInterface != null) {
                mNemoAidlInterface.enableSpeech(false);// 是否开启机器人聊天唤醒
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拨打视频
     *
     * @param name 手机号码
     */
    public void startPhone(String name) {
        try {
            if (mNemoAidlInterface != null)
                mNemoAidlInterface.makeNemoTelephoneCall(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置小鱼助手的持续时间（逻辑复杂，该功能暂未实现）
     */
    public void setKeepTime(long time) {
        try {
            if (mNemoAidlInterface != null) {
                mNemoAidlInterface.setNemoHelperOutOfTime(time);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 唤醒小鱼
     */
    public void wakeUp() {
        if (isUsable()) {
            try {
                if (mNemoAidlInterface != null) {
                    mNemoAidlInterface.wakeupNemoHelper();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拍照功能
     */
    public void takePicture(XYCallback callback) {
        setUsable(false);
        picCallback = callback;
        try {
            if (mNemoAidlInterface != null)
                mNemoAidlInterface.takePicture();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean isUsable() {
        long time = System.currentTimeMillis() - timeMillis;
        return time > 10000 || isUsable;
    }

    void setUsable(boolean usable) {
        isUsable = usable;
        timeMillis = System.currentTimeMillis();
    }
}
