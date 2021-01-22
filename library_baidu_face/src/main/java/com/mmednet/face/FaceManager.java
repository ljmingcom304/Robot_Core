package com.mmednet.face;

import android.content.Context;
import android.util.Log;

import com.baidu.aip.FaceEnvironment;
import com.baidu.aip.FaceSDKManager;
import com.baidu.idl.facesdk.FaceTracker;
import com.mmednet.face.bean.AccessToken;
import com.mmednet.face.exception.FaceError;
import com.mmednet.face.model.RegResult;
import com.mmednet.face.net.APIService;
import com.mmednet.face.utils.Md5;
import com.mmednet.face.utils.OnResultListener;

import java.io.File;

/**
 * Title: Robot_Library
 * <p>
 * Description:人脸识别管理器(初始化及接口调用)
 * </p>
 *
 * @author Zsy
 * @date 2019/3/27 16:18
 */
public class FaceManager {

    /**
     * @param context         上下文
     * @param licenseID       账号的
     * @param licenseFileName 在assets文件下license文件名
     * @param apiKey          账号的apiKey
     * @param secretKey       账号的secretKey
     */
    public static void init(final Context context, String groupID, String licenseID, String licenseFileName, String apiKey, String secretKey) {
        //传入参数信息初始化SDK
        FaceSDKManager.getInstance().init(context, licenseID, licenseFileName);
        //设置配置参数
        setFaceConfig(context);
        //初始化网络工具类
        APIService.getInstance().init(context);
        //人脸库ID
        APIService.getInstance().setGroupId(groupID);
        // 用ak，sk获取token, 调用在线api，如：注册、识别等。为了ak、sk安全，建议放您的服务器，
        OnResultListener<AccessToken> listener = new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                Log.i("faceidentify", "AccessToken->" + result.getAccessToken());
            }

            @Override
            public void onError(FaceError error) {
                Log.e("faceidentify", "AccessTokenError:" + error);
                error.printStackTrace();
            }
        };
        //联网获取AccessToken
        APIService.getInstance().initAccessTokenWithAkSk(listener, context, apiKey, secretKey);
    }

    /**
     * 面部识别注册
     *
     * @param userName 用户名
     * @param faceFile 头像文件
     */
    public static void register(String userName, String uid, File faceFile, OnResultListener<RegResult> onResultListener) {
        APIService.getInstance().reg(onResultListener, faceFile, uid, userName);
    }

    /**
     * 面部识别验证
     *
     * @param userName 用户名
     * @param faceFile 头像文件
     */
    public static void verify(String userName, File faceFile, int minScroe, OnResultListener<RegResult> listener) {
        String uid = Md5.MD5(userName, "utf-8");
        APIService.getInstance().verify(listener, faceFile, uid);
    }

    /**
     * 面部识别快速登录
     *
     * @param resultListener
     * @param faceFile       头像文件
     */
    public static void login(File faceFile, OnResultListener<RegResult> resultListener) {
        APIService.getInstance().identify(resultListener, faceFile);
    }


    /**
     * 配置参数信息
     */
    private static void setFaceConfig(Context context) {
        FaceTracker tracker = FaceSDKManager.getInstance().getFaceTracker(context);  //.getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整

        // 模糊度范围 (0-1) 推荐小于0.7
        tracker.set_blur_thr(FaceEnvironment.VALUE_BLURNESS);
        // 光照范围 (0-255) 推荐大于40
        tracker.set_illum_thr(FaceEnvironment.VALUE_BRIGHTNESS);
        // 裁剪人脸大小
        tracker.set_cropFaceSize(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        // 人脸yaw,pitch,row 角度，范围（-45，45），推荐-15-15
        tracker.set_eulur_angle_thr(45, 45, 45);
        // 最小检测人脸（在图片人脸能够被检测到最小值）80-200， 越小越耗性能，推荐120-200
        tracker.set_min_face_size(120);//FaceEnvironment.VALUE_MIN_FACE_SIZE (100)
        //极限
        tracker.set_notFace_thr(0.6f);//FaceEnvironment.VALUE_NOT_FACE_THRESHOLD(0.6)
        // 人脸遮挡范围 （0-1） 推荐小于0.5
        tracker.set_occlu_thr(0.5f);//FaceEnvironment.VALUE_OCCLUSION
        // 是否进行质量检测
        tracker.set_isCheckQuality(true);
        // 是否进行活体校验
        tracker.set_isVerifyLive(true);

        tracker.set_detect_in_video_interval(500);
    }

}
