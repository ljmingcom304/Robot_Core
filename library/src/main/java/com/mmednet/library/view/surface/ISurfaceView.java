package com.mmednet.library.view.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class ISurfaceView extends SurfaceView {

    private SurfaceHolder mHolder;
    private ICallback mCallback;

    public ISurfaceView(Context context) {
        this(context, null);
    }

    public ISurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ISurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    protected final void initView() {
        this.setZOrderOnTop(true);
        mHolder = this.getHolder();
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mHolder.setKeepScreenOn(true);
        mCallback = new ICallback(mHolder) {

            @Override
            public void draw(Canvas canvas) {
                drawView(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                onViewChange(width, height);
            }

        };
    }

    @Override
    protected final void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == 0) {// 窗口显示
            mCallback.start();
            mHolder.addCallback(mCallback);
        } else {
            mCallback.cancel();
            mHolder.removeCallback(mCallback);
        }
    }

    @Override
    protected final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected abstract void onViewChange(float width, float height);

    protected abstract void drawView(Canvas canvas);

}
