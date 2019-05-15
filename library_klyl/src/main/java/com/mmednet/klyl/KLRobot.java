package com.mmednet.klyl;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mmednet.klyl.bean.FileMsgBean;
import com.mmednet.klyl.bean.MapBean;
import com.mmednet.klyl.callback.ModeCallback;
import com.mmednet.klyl.callback.OnListener;
import com.mmednet.klyl.constants.MsgType;
import com.mmednet.klyl.util.HttpUtil;
import com.mmednet.klyl.util.ImageUpload;
import com.mmednet.klyl.util.MsgSendUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KLRobot extends RobotManager {

    private static KLRobot mManager;
    private HandleMessage mMessage;
    private static String TAG = KLRobot.class.getSimpleName();
    private MsgSendUtils msgSendUtils;
    private Gson gson = new Gson();

    private KLRobot() {
        mMessage = HandleMessage.getInstance();
        msgSendUtils = MsgSendUtils.getInstance();
    }

    public static KLRobot getInstance() {
        if (mManager == null) {
            mManager = new KLRobot();
        }
        return mManager;
    }

    @Override
    public void init(Context context) {
        setSpeech2TextCode("健康服务", null);
    }

    /**
     * 进入聊天模式
     */
    @Override
    public void startChat() {
        getSystemMode(new ModeCallback() {

            @Override
            public void onWake() {
                Log.e(TAG, "KLRobot=>startChat wake");
            }

            @Override
            public void onSleep() {
                Log.e(TAG, "KLRobot=>startChat sleep");
            }
        });
        msgSendUtils.sendStringMsg(MsgType.CHAT_START_SEND, "ok");
    }

    @Override
    public void stopChat() {
        Log.i(TAG, "KLRobot=>stopChat");
    }

    /**
     * 文本转语音 ，当参数二为true时表示开启语音识别模式
     */
    @Override
    public void text2Speech(String text, final KLCallback callback) {
        boolean start = true;
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.TTS_RECEIVE));
        if (!TextUtils.isEmpty(text)) {
            if (start) {
                msgSendUtils.sendStringMsg(
                        MsgType.TTS_START_RECOGNITION_SEND, text);
            } else {
                msgSendUtils.sendStringMsg(MsgType.TTS_SEND, text);
            }
        }
    }

    /**
     * 设置音量
     */
    public void setVolume(int volume) {
        volume = Math.max(volume, 0);
        volume = Math.min(volume, 15);
        msgSendUtils.sendStringMsg(MsgType.VOICE_SEND, volume + "");
    }

    /**
     * 开启语音识别
     */
    @Override
    public void speech2Text(final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.STT_RECEIVE));
        msgSendUtils.sendStringMsg(MsgType.STT_START_SEND, "open");
    }

    /**
     * 关闭语音识别
     */
    public void speech2TextClose() {
        msgSendUtils.sendStringMsg(MsgType.STT_CLOSE_SEND, "close");
    }

    /**
     * 设置进入语音识别模式的口令
     */
    @Deprecated
    public void setSpeech2TextCode(String code, final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.STT_PASSWORD_RECEIVE));
        msgSendUtils.sendStringMsg(
                MsgType.STT_PASSWORD_SEND, code);
    }

    /**
     * 设置聊天模式的失败次数，达到次数后返回休眠模式
     */
    @Deprecated
    public void setTalkModeMaxCount(int count) {
        msgSendUtils.sendStringMsg(MsgType.MODE_SWITCH_COUNT,
                String.valueOf(count));
    }

    /**
     * 播放音频
     */
    @Deprecated
    public void playAudio(String name) {
        msgSendUtils.sendStringMsg(MsgType.AUDIO_PLAY_SEND, name);
    }

    /**
     * 获取所有音频
     */
    @Deprecated
    public void getAudios(final KLCallback callback) {
        msgSendUtils.sendStringMsg(MsgType.AUDIO_LIST_SEND, "ok");
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.AUDIO_LIST_RECEIVE));
    }

    /**
     * 开始拍照
     */
    @Deprecated
    public void takePicture(final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.PHOTO_START_RECEIVE));
        msgSendUtils.sendFaceMsg(MsgType.PHOTO_START_SEND, "Photo");
    }

    /**
     * 开启拍照预览
     */
    @Deprecated
    public void openPicture() {
        msgSendUtils.sendFaceMsg(MsgType.SEND_SCAN_START, "Preview");
    }

    /**
     * 结束拍照预览
     */
    @Deprecated
    public void closePicture() {
        msgSendUtils.sendFaceMsg(MsgType.SEND_SCAN_CLOSE, "Preview");
    }

    /**
     * 人脸注册
     */
    @Deprecated
    public void registerFaceInfo(final Bitmap bitmap, final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.FACE_REGISTER_RECEIVE));
        HttpUtil.go("http://192.168.12.1:7755/dbaction",
                new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onCallBack(String result) {
                        int id = Integer.parseInt(result);
                        id++;
                        Log.i(TAG, "人脸注册ID为:" + id);
                        uploadFaceInfo(bitmap, "andy", id + "");
                    }
                });

    }

    /**
     * 注册人脸信息
     */
    @Deprecated
    private void uploadFaceInfo(Bitmap bitmap, String name, String id) {
        if (bitmap != null) {
            String PHOTO_PATH = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/msc/";
            new ImageUpload(PHOTO_PATH + "fr.jpg").start();
            File dir = new File(PHOTO_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(PHOTO_PATH + "fr.jpg");// 将bitmap存成图片文件
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!file.exists()) {
                Log.i("FaceRegRegisterActivity", "图片不正确");
                return;
            }
            FileMsgBean bean = new FileMsgBean(file.getName(), file.length(),
                    MsgType.FACE_REGISTER_SEND, name, id);
            String sendStr = gson.toJson(bean);
            msgSendUtils.sendFaceMsg(MsgType.FACE_FILE_TYPE, sendStr);
        }
    }

    /**
     * 开启人脸识别
     */
    public void openFaceRecognition(final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.FACE_RECOGNITION_START_RECEIVE));
        msgSendUtils.sendFaceMsg(MsgType.FACE_RECOGNITION_START_SEND, "Face");
    }

    /**
     * 结束人脸识别
     */
    public void closeFaceRecognition() {
        msgSendUtils.sendFaceMsg(MsgType.FACE_RECOGNITION_CLOSE_SEND, "Face");
    }

    /**
     * 设置机器人连接外网WIFI
     */
    public void setSystemWifi(String ssid, String password) {
        if (!TextUtils.isEmpty(ssid) && !TextUtils.isEmpty(password)) {
            msgSendUtils.sendStringMsg(MsgType.WIFI_SEND, ssid + "&&"
                    + password);
        }
    }

    /**
     * 获取系统信息
     */
    public void getSystemInfo(final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.SYSTEM_INFO_RECEIVE));
        msgSendUtils.sendStringMsg(MsgType.SYSTEM_INFO_SEND, "ok");
    }

    /**
     * 获取当前的模式
     */
    private void getSystemMode(final ModeCallback callback) {
        msgSendUtils.sendStringMsg(MsgType.SYSTEM_INFO_SEND, "ok");
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(null, tag, MsgType.SYSTEM_INFO_SEND) {
            @Override
            public void onListen(String message, int msgType) {
                if (callback != null) {
                    if (msgType == MsgType.MODE_WAKEUP_RECEIVE) {// 唤醒
                        callback.onWake();
                    } else if (msgType == MsgType.MODE_SLEEP_RECEIVE) {// 休眠
                        callback.onSleep();
                    }
                }
            }
        });
    }

    /**
     * 机器人执行指定做动作
     */
    @Deprecated
    public void doAction(String action) {
        msgSendUtils.sendStringMsg(MsgType.ACTION_SEND, action);
    }

    /**
     * 让机器人去指定的位置
     */
    @Deprecated
    public void reachAddress(String position, KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.ACTION_ADDRESS_REACH_RECEIVE));
        msgSendUtils.sendStringMsg(MsgType.ACTION_ADDRESS_REACH_SEND, position);
    }

    /**
     * 获取机器人可以去的位置
     */
    @Deprecated
    public ArrayList<String> getAddress(String fileName) {
        ArrayList<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append("Slam");
        sb.append(File.separator);
        sb.append(fileName + "_json.json");
        Log.e("TAG", "地图文件路径为" + sb.toString());
        StringBuffer sb1 = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(sb.toString()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb1.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 字符串转JSON
        List<MapBean> mList = gson.fromJson(sb1.toString(), new TypeToken<List<MapBean>>() {
        }.getType());
        for (MapBean m : mList) {
            Log.e("TAG", "可以去的地方为：" + m.getName());
            list.add(m.getName());
        }
        return list;
    }

    /**
     * 播放指定表情
     */
    @Deprecated
    public void playEmoji(String name) {
        msgSendUtils.sendStringMsg(MsgType.EMOJI_PLAY_SEND, name);
    }

    /**
     * 获取表情列表
     */
    @Deprecated
    public void getEmojis(final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.EMOJI_LIST_RECEIVE));
        msgSendUtils.sendStringMsg(MsgType.EMOJI_LIST_SEND, "emoji");
    }

    /**
     * 拨打电话
     */
    @Override
    public void startPhone(String name, final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.PHONE_START_RECEIVE));
        msgSendUtils.sendPhoneMsg(MsgType.PHONE_START_SEND, name);
    }

    /**
     * 取消拨打
     */
    @Override
    public void stopPhone() {
        msgSendUtils.sendPhoneMsg(MsgType.PHONE_CANCEL_SEND, "cancle");
    }

    /**
     * 获取联系人列表
     */
    public void getContants(final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.PHONE_CONTANT_LIST_RECEIVE));
        msgSendUtils.sendPhoneMsg(MsgType.PHONE_CONTANT_LIST_SEND, "contact");
    }

    /**
     * 来电提醒
     */
    @Override
    public void receivePhone(final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.PHONE_REMIND_SEND));
    }

    /**
     * 接听电话
     */
    @Override
    public void acceptPhone() {
        msgSendUtils.sendPhoneMsg(MsgType.PHONE_ACCEPT_SEND, "accept");
    }

    /**
     * 拒接电话
     */
    @Override
    public void refusePhone() {
        msgSendUtils.sendPhoneMsg(MsgType.PHONE_ACCEPT_SEND, "refuse");
    }

    /**
     * 挂断电话
     */
    @Override
    public void hangupPhoneBySelf() {
        msgSendUtils.sendPhoneMsg(MsgType.PHONE_HANGUP_SELF_SEND, "cancle");
    }

    /**
     * 被挂断电话
     */
    @Override
    public void hangupPhoneByOther(final KLCallback callback) {
        String tag = Thread.currentThread().getStackTrace()[1].getMethodName();
        mMessage.setOnListener(new OnListener(callback, tag, MsgType.PHONE_HANGUP_OTHER_RECEIVE));
    }

}
