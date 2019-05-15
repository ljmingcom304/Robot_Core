/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mmednet.zxing.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.google.zxing.Result;
import com.mmednet.zxing.camera.CameraManager;
import com.mmednet.zxing.common.Constant;
import com.mmednet.zxing.decode.DecodeThread;
import com.mmednet.zxing.view.ViewfinderCallback;
import com.mmednet.zxing.view.ViewfinderView;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture. 该类用于处理有关拍摄状态的所有信息
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureHandler extends Handler {

    private final CaptureManager captureManager;
    private final FragmentActivity activity;
    private final DecodeThread decodeThread;
    private final CameraManager cameraManager;
    private final ViewfinderView viewfinderView;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureHandler(CaptureManager captureManager, FragmentActivity activity, ViewfinderView viewfinderView) {
        this.captureManager = captureManager;
        this.activity = activity;
        this.viewfinderView = viewfinderView;
        decodeThread = new DecodeThread(this, true,
                new ViewfinderCallback(viewfinderView));
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        // 开始拍摄预览和解码
        this.cameraManager = captureManager.getCameraManager();
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case Constant.RESTART_PREVIEW:
                // 重新预览
                restartPreviewAndDecode();
                break;
            case Constant.DECODE_SUCCEEDED:
                // 解码成功
                state = State.SUCCESS;
                captureManager.handleDecode((Result) message.obj);
                break;
            case Constant.DECODE_FAILED:
                // 尽可能快的解码，以便可以在解码失败时，开始另一次解码
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(),
                        Constant.DECODE_START);
                break;
            case Constant.RETURN_SCAN_RESULT:
                activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.finish();
                break;
        }
    }

    /**
     * 完全退出
     */
    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), Constant.DECODE_QUIT);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause()
            // will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        //确保不会发送任何队列消息
        removeMessages(Constant.DECODE_SUCCEEDED);
        removeMessages(Constant.DECODE_FAILED);
    }

    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(),
                    Constant.DECODE_START);
            viewfinderView.drawViewfinder();
        }
    }

}
