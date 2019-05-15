package com.mmednet.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mmednet.library.R;

/**
 * Title:DrawableTextView
 * <p>
 * Description:图片可控大小和位置的TextView
 * </p>
 * Author Jming.L
 * Date 2018/3/24 10:30
 */
public class DrawableTextView extends TextView {

    private int drawableLeftWidth;
    private int drawableTopWidth;
    private int drawableRightWidth;
    private int drawableBottomWidth;

    private int drawableLeftHeight;
    private int drawableTopHeight;
    private int drawableRightHeight;
    private int drawableBottomHeight;

    private boolean isAlignCenter;//是否居中对齐

    private int mWidth;
    private int mHeight;

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        drawableLeftWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableLeftWidth, 0);
        drawableTopWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableTopWidth, 0);
        drawableRightWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableRightWidth, 0);
        drawableBottomWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableBottomWidth, 0);
        drawableLeftHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableLeftHeight, 0);
        drawableTopHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableTopHeight, 0);
        drawableRightHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableRightHeight, 0);
        drawableBottomHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableBottomHeight, 0);
        isAlignCenter = typedArray.getBoolean(R.styleable.DrawableTextView_isAlignCenter, false);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        Drawable drawableTop = drawables[1];
        Drawable drawableRight = drawables[2];
        Drawable drawableBottom = drawables[3];
        if (drawableLeft != null) {
            setDrawable(drawableLeft, 0, drawableLeftWidth, drawableLeftHeight);
        }
        if (drawableTop != null) {
            setDrawable(drawableTop, 1, drawableTopWidth, drawableTopHeight);
        }
        if (drawableRight != null) {
            setDrawable(drawableRight, 2, drawableRightWidth, drawableRightHeight);
        }
        if (drawableBottom != null) {
            setDrawable(drawableBottom, 3, drawableBottomWidth, drawableBottomHeight);
        }
        this.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    private void setDrawable(Drawable drawable, int tag, int drawableWidth, int drawableHeight) {
        //获取图片实际长宽
        int width = drawableWidth == 0 ? drawable.getIntrinsicWidth() : drawableWidth;
        int height = drawableHeight == 0 ? drawable.getIntrinsicHeight() : drawableHeight;
        int left = 0, top = 0, right = 0, bottom = 0;
        switch (tag) {
            case 0:
            case 2:
                left = 0;
                top = isAlignCenter ? 0 : -getLineCount() * getLineHeight() / 2 + getLineHeight() / 2;
                right = width;
                bottom = top + height;
                break;
            case 1:
                left = isAlignCenter ? 0 : -mWidth / 2 + width / 2;
                top = 0;
                right = left + width;
                bottom = top + height;
                break;
        }
        drawable.setBounds(left, top, right, bottom);
    }

    public void setDrawableLeftWidth(int drawableLeftWidth) {
        this.drawableLeftWidth = drawableLeftWidth;
    }

    public void setDrawableTopWidth(int drawableTopWidth) {
        this.drawableTopWidth = drawableTopWidth;
    }

    public void setDrawableRightWidth(int drawableRightWidth) {
        this.drawableRightWidth = drawableRightWidth;
    }

    public void setDrawableBottomWidth(int drawableBottomWidth) {
        this.drawableBottomWidth = drawableBottomWidth;
    }

    public void setDrawableLeftHeight(int drawableLeftHeight) {
        this.drawableLeftHeight = drawableLeftHeight;
    }

    public void setDrawableTopHeight(int drawableTopHeight) {
        this.drawableTopHeight = drawableTopHeight;
    }

    public void setDrawableRightHeight(int drawableRightHeight) {
        this.drawableRightHeight = drawableRightHeight;
    }

    public void setDrawableBottomHeight(int drawableBottomHeight) {
        this.drawableBottomHeight = drawableBottomHeight;
    }

    public void setAlignCenter(boolean alignCenter) {
        isAlignCenter = alignCenter;
    }

}