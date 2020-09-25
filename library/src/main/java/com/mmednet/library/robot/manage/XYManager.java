package com.mmednet.library.robot.manage;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.mmednet.library.layout.CustomApplication;
import com.mmednet.library.util.FileUtils;
import com.mmednet.xyzj.XYCallback;
import com.mmednet.xyzj.XYRobot;

import org.jetbrains.annotations.Nullable;

/**
 * Title:XYManager
 * <p>
 * Description:小鱼在家
 * </p>
 * Author Jming.L
 * Date 2017/9/6 9:51
 */
public class XYManager implements Manager {

    private XYRobot mRobot = XYRobot.getInstance();
    private CustomApplication mApp = CustomApplication.getInstance();

    private Callback mTtsCallback;
    private Callback mAsrCallback;
    private Callback mFaceCallback;
    private Callback mPicCallback;

    private XYCallback mXYTtsCallback = new XYCallback() {
        @Override
        public void onBack(String message) {
            if (mTtsCallback != null) {
                mTtsCallback.onBack(Status.SUCCESS, message);
            }
        }
    };
    private XYCallback mXYSttCallback = new XYCallback() {
        @Override
        public void onBack(String message) {
            Toast.makeText(mApp, message, Toast.LENGTH_SHORT).show();
            if (mAsrCallback != null) {
                mAsrCallback.onBack(Status.SUCCESS, message);
            }
        }
    };
    private XYCallback mXYFaceCallback = new XYCallback() {
        @Override
        public void onBack(String message) {
            if (!TextUtils.isEmpty(message)) {
                //人脸图片=路径+用户ID+文件后缀，这里提取用户ID
                message = message.substring(
                        message.lastIndexOf("/") + 1,
                        message.lastIndexOf("."));
                if (mFaceCallback != null) {
                    mFaceCallback.onBack(Status.SUCCESS, message);
                }
            } else {
                if (mFaceCallback != null) {
                    mFaceCallback.onBack(Status.FAILURE, "error");
                }
            }
        }
    };
    private XYCallback mXYPicCallback = new XYCallback() {
        @Override
        public void onBack(String message) {
            if (mPicCallback != null) {
                mPicCallback.onBack(Status.SUCCESS, message);
            }
        }
    };

    @Override
    public void init(Context context) {
        mRobot.init(context);
    }

    @Override
    public void release(Context context) {
        mRobot.close(context);
    }

    @Override
    public void text2Speech(String text, Callback callback) {
        this.mTtsCallback = callback;
        mRobot.text2Speech(text, mXYTtsCallback);
    }

    @Override
    public void speech2Text(Callback callback) {
        this.mAsrCallback = callback;
        mRobot.speech2Text(mXYSttCallback);
    }

    @Override
    public void wakeUp(boolean wakeup) {
        if (wakeup) {
            mRobot.wakeUp();
            //手动唤醒没有唤醒词
            if (mAsrCallback != null) {
                mAsrCallback.onBack(Status.WAKEUP, null);
            }
        }
    }

    public void startPhone(String name, Callback callback) {
        mRobot.startPhone(name);
    }

    public void takePicture(Callback callback) {
        mPicCallback = callback;
        mRobot.takePicture(mXYPicCallback);
    }

    /**
     * 人脸识别
     *
     * @param callback 回调识别结果
     * @param dir      照片比对目录
     */
    public void openFaceRecognition(Callback callback, @Nullable String dir) {
        this.mFaceCallback = callback;
        final String path = FileUtils.getFilesPath(dir);
        Activity activity = mApp.getCurActivity();
        mRobot.startRecognitionOfFace(activity, path, mXYFaceCallback);
    }

}
