package com.mmednet.ocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.mmednet.ocr.utils.FileUtils;

import java.io.File;

import androidx.fragment.app.Fragment;

/**
 * Title:OcrManager
 * <p>
 * Description:文字识别
 * </p >
 * Author Jming.L
 * Date 2021/1/22 11:20
 */
public class OcrManager {

    private static final int REQUEST_CODE_CAMERA = 102;
    private Context mContext;
    private Activity mActivity;
    private Fragment mFragment;
    private OnResultListener<IDCardResult> mListener;

    public OcrManager(Activity activity) {
        mActivity = activity;
        mContext = activity.getBaseContext();
    }

    public OcrManager(Fragment fragment) {
        mFragment = fragment;
        mContext = fragment.getContext();
    }

    public static void init(final Context context, String ak, String sk, OnResultListener<AccessToken> listener) {
        OCR.getInstance(context).initAccessTokenWithAkSk(listener, context.getApplicationContext(), ak, sk);
    }

    /**
     * 初始化质量控制模型
     */
    public void initCamera() {
        //  初始化本地质量控制模型,释放代码在onDestory中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(mContext, OCR.getInstance(mContext).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        Log.e(OcrManager.class.getSimpleName(), "本地质量控制初始化错误，错误原因： " + msg);
                    }
                });
    }

    /**
     * 释放质量控制模型
     */
    public void releaseCamera() {
        // 释放本地质量控制模型
        CameraNativeHelper.release();
    }

    /**
     * 打开身份证正面识别
     */
    public void openIdCardFront() {
        Intent intent = new Intent(mContext, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtils.getSaveFile(mContext.getApplicationContext()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        if (mFragment == null) {
            mActivity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } else {
            mFragment.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 处理身份证识别结果
     */
    public void handleIdCardResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtils.getSaveFile(mContext.getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }
    }

    public void setOnResultListener(OnResultListener<IDCardResult> listener) {
        mListener = listener;
    }

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);
        OCR.getInstance(mContext).recognizeIDCard(param, mListener);
    }

}
