package com.mmednet.zxing.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.Result;
import com.mmednet.zxing.camera.CameraManager;
import com.mmednet.zxing.common.Constant;
import com.mmednet.zxing.decode.DecodeImgCallback;
import com.mmednet.zxing.decode.DecodeImgThread;
import com.mmednet.zxing.decode.ImageUtil;
import com.mmednet.zxing.encode.CodeCreator;
import com.mmednet.zxing.view.ViewfinderView;

import java.io.IOException;

/**
 * Title:CaptureManager
 * <p>
 * Description:扫码处理框
 * </p>
 * Author Jming.L
 * Date 2018/12/13 16:43
 */
public class CaptureManager {

    /**
     * 二维码扫描结果
     */
    public static final String CODED_CONTENT = "codedContent";
    /**
     * 图片解析结果
     */
    public static final String CODED_BITMAP = "codedBitmap";

    private Application application;
    private FragmentActivity activity;

    private ViewfinderView viewfinderView;          //扫描取景
    private SurfaceView surfaceView;                //扫描显景
    private SurfaceCallback surfaceCallback;
    private SurfaceHolder surfaceHolder;

    private ResultLauncher launcher;
    private InactivityTimer inactivityTimer;
    private CameraManager cameraManager;            //相机管理
    private BeepManager beepManager;                //振动管理
    private CaptureHandler captureHandler;          //解码处理

    public CaptureManager(FragmentActivity activity, SurfaceView surfaceView, ViewfinderView viewfinderView) {
        this.activity = activity;
        this.application = activity.getApplication();
        this.surfaceView = surfaceView;
        this.viewfinderView = viewfinderView;
        this.inactivityTimer = new InactivityTimer(activity);
        this.beepManager = new BeepManager(activity);
        this.launcher = ResultLauncher.init(activity);

        // 保持Activity处于唤醒状态
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        application.registerActivityLifecycleCallbacks(new ActivityCallback());
    }


    /**
     * @param rawResult 返回的扫描结果
     */
    public void handleDecode(Result rawResult) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        Intent intent = activity.getIntent();
        intent.putExtra(CODED_CONTENT, rawResult.getText());
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * 解析相册中二维码
     */
    public void decodePhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcher.startActivityForResult(intent, new ResultLauncher.Callback() {

            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    String path = ImageUtil.getImageAbsolutePath(activity, data.getData());
                    new DecodeImgThread(path, new DecodeImgCallback() {
                        @Override
                        public void onImageDecodeSuccess(Result result) {
                            handleDecode(result);
                        }

                        @Override
                        public void onImageDecodeFailed() {
                            Toast.makeText(activity, "图片解析失败.", Toast.LENGTH_SHORT).show();
                        }
                    }).run();
                }
            }
        });
    }

    /**
     * 根据内容生成二维码
     *
     * @param content 内容
     * @param width   宽度
     * @param height  高度
     * @param logo    LOGO
     * @return
     */
    public static Bitmap createCode(String content, int width, int height, Bitmap logo) {
        return CodeCreator.createQRCode(content, width, height, logo);
    }

    /**
     * 切换闪光灯
     */
    public boolean switchFlashLight() {
        return cameraManager.switchFlashLight();
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }


    void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (captureHandler == null) {
                captureHandler = new CaptureHandler(this, activity, viewfinderView);
            }
        } catch (IOException e) {
            Toast.makeText(activity, "相机被占用或没有相机权限，请检查", Toast.LENGTH_LONG).show();
        }
    }

    class ActivityCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (CaptureManager.this.activity == activity) {
                surfaceCallback.setSurface(false);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (CaptureManager.this.activity == activity) {
                cameraManager = new CameraManager(application, true);
                viewfinderView.setCameraManager(cameraManager);
                captureHandler = null;
                surfaceHolder = surfaceView.getHolder();
                if (surfaceCallback != null && surfaceCallback.isSurface()) {
                    initCamera(surfaceHolder);
                } else {
                    // 重置callback，等待surfaceCreated()来初始化camera
                    surfaceCallback = new SurfaceCallback(CaptureManager.this);
                    surfaceHolder.addCallback(surfaceCallback);
                }

                beepManager.updatePrefs();
                inactivityTimer.onResume();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (CaptureManager.this.activity == activity) {
                if (captureHandler != null) {
                    captureHandler.quitSynchronously();
                    captureHandler = null;
                }
                cameraManager.closeDriver();
                beepManager.close();
                inactivityTimer.onPause();
                if (!surfaceCallback.isSurface()) {
                    surfaceHolder.removeCallback(surfaceCallback);
                }
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (CaptureManager.this.activity == activity) {
                inactivityTimer.shutdown();
                application.unregisterActivityLifecycleCallbacks(this);
            }
        }

    }


}
