package com.mmednet.face.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mmednet.face.R;


/**
 * Title: Robot_Library
 * <p>
 * Description:
 * </p>
 *
 * @author Zsy
 * @date 2019/4/1 10:05
 */

public class ScanView extends View {
    private final int STATE_DEF = 0, STATE_SCAN = 1, STATE_VERIFY = 2;
    private int mState = STATE_DEF;
    private int mWidth, mHeight;//控件宽高

    private float border = 0.00f;
    private int right;
    private int left;
    private int bottom;
    private int top;
    private Paint mClpaint;
    private Paint mLinepaint;
    private Paint mRectpaint;//已扫描区域paint
    private float[] floats;
    private float mCL = 36f;
    private String TAG = "ScanView";
    private int mCurrent;//记录当前动画进度值

    public ValueAnimator getValueAnimator() {
        return valueAnimator;
    }

    private ValueAnimator valueAnimator;//动画
    private AnimationDrawable mFrameAnim;//验证帧动画

    private RectF mScanRect = new RectF();//已扫描矩形范围(淡蓝色)
    private RectF mFaceRect;//面部识别捕获矩形范围

    public ScanView(Context context) {
        super(context);
        init();
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object value = animation.getAnimatedValue();
                mCurrent = (int) value;
                invalidate();
            }
        });
        mClpaint = new Paint();
        mClpaint.setColor(0xff0c6cfe);
        mClpaint.setStrokeWidth(5);

        int duration = 1500 / 30;//总时长1500毫秒
        mFrameAnim = new AnimationDrawable();
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_00), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_01), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_02), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_03), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_04), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_05), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_06), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_07), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_08), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_09), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_10), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_11), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_12), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_13), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_14), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_15), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_16), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_17), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_18), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_19), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_20), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_21), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_22), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_23), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_24), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_25), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_26), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_27), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_28), duration);
        mFrameAnim.addFrame(getResources().getDrawable(R.drawable.pic_29), duration);
        // 设置为循环播放
        mFrameAnim.setOneShot(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (floats == null) {
            left = (int) (border * mWidth);
            right = (int) ((1 - border) * mWidth);
            top = (int) (border * mHeight);
            bottom = (int) ((1 - border) * mHeight);

            floats = new float[]{left, top, left + mCL, top,//左上角上
                    left, top, left, mCL + top,//左上角左
                    right, top, right - mCL, top,//右上角上
                    right, top, right, top + mCL,//右上角右
                    left, bottom, left, bottom - mCL,//左下角左
                    left, bottom, left + mCL, bottom,//左下角下
                    right, bottom, right - mCL, bottom,//右下角下
                    right, bottom, right, bottom - mCL//右下角右
            };
        }
        //扫描线
        if (mLinepaint == null) {
            mLinepaint = new Paint();
            LinearGradient linearGradient = new LinearGradient(0, 0, mWidth / 2, 0,
                    0x000c6cfe, 0x330c6cfe,
                    Shader.TileMode.MIRROR);
            mLinepaint.setShader(linearGradient);
            mLinepaint.setStrokeWidth(15);
        }
        if (mRectpaint == null) {
            mRectpaint = new Paint();
            LinearGradient rectGradient = new LinearGradient(0, 0, 0, mHeight,
                    0x000c6cfe, 0x330c6cfe,
                    Shader.TileMode.CLAMP);
            mRectpaint.setShader(rectGradient);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float canvasWidth = right - left;//画布宽度
        float canvasHeight = bottom - top;//画布高度
        float x = canvasWidth / 2;
        float y = (canvasHeight / 2);
        float r = (Math.min(canvasWidth, canvasHeight) / 2) - 20;
        if (mFaceRect == null) {
            mFaceRect = new RectF(
                    (int) (x - r),//左
                    (int) (y - r),//上
                    (int) (x + r),//右
                    (int) (y + r));//下
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == STATE_SCAN) {
            // 绘制边角
            canvas.drawLines(floats, mClpaint);
            //当前扫面高度
            int mScanHeight = mCurrent * mHeight / 100;
            //绘制扫描线
            canvas.drawLine(0, mScanHeight, mWidth, mScanHeight, mLinepaint);
            //绘制已扫描区域
            mScanRect.set(0, 0, mWidth, mScanHeight);
            canvas.drawRoundRect(mScanRect, 0, 0, mRectpaint);
        }
        if (mState == STATE_VERIFY) {
            RectF rectF = new RectF();
            rectF.set(20, 20, mWidth-20, mHeight-20);
            Paint paint = new Paint();
            paint.setColor(0x99000000);
            canvas.drawRoundRect(rectF, 0, 0, paint);
            paint.setColor(0xffffffff);
            paint.setTextSize(40);
            String s = "识别中，请耐心等待...";
            float v = paint.measureText(s);
            canvas.drawText(s, (mWidth - v) / 2, (float) (mHeight * 0.8), paint);
        }

    }


    public RectF getFaceRoundRect() {
        if (mFaceRect != null) {
            Log.e(TAG, mFaceRect.toString());
        }
        return mFaceRect;
    }

    /**
     * 开启扫描动画
     */
    public void start() {
        post(new Runnable() {
            @Override
            public void run() {
                if (mFrameAnim.isRunning()) {
                    mFrameAnim.stop();
                    setBackgroundDrawable(null);
                }
                mState = STATE_SCAN;
                setBackgroundResource(R.drawable.pic_face_frame);
                valueAnimator.start();
            }
        });

    }

    /**
     * 停止动画
     */
    public void stop() {
        post(new Runnable() {
            @Override
            public void run() {
                if (valueAnimator.isRunning()) {
                    valueAnimator.cancel();
                }
                if (mFrameAnim.isRunning()) {
                    mFrameAnim.stop();
                    setBackgroundDrawable(null);
                }
                setBackgroundResource(R.drawable.pic_face_frame);
                mState = STATE_DEF;
                mCurrent = 0;
                invalidate();
            }
        });

    }

    /**
     * 显示验证动画
     */
    public void verify() {
        post(new Runnable() {
            @Override
            public void run() {
                if (valueAnimator.isRunning()) {
                    valueAnimator.cancel();
                }
                if (mFrameAnim.isRunning()) {
                    mFrameAnim.stop();
                    setBackgroundDrawable(null);
                }
                setBackgroundDrawable(mFrameAnim);
                mState = STATE_VERIFY;
                mFrameAnim.start();
            }
        });

    }

    /**
     * 验证失败显示空白页
     */
    public void fail() {
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        if (mFrameAnim.isRunning()) {
            mFrameAnim.stop();
        }
        setBackgroundDrawable(null);
        mState = STATE_DEF;
        invalidate();
    }

}
