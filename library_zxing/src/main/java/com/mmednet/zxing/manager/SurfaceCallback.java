package com.mmednet.zxing.manager;

import android.view.SurfaceHolder;


class SurfaceCallback implements SurfaceHolder.Callback {

    private boolean surface;
    private CaptureManager manager;

    public SurfaceCallback(CaptureManager manager) {
        this.manager = manager;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!surface) {
            surface = true;
            manager.initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surface = false;
    }

    public boolean isSurface() {
        return surface;
    }

    public void setSurface(boolean surface) {
        this.surface = surface;
    }
}
