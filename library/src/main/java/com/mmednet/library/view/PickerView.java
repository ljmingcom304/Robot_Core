package com.mmednet.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:ScrollerView
 * <p>
 * Description:滚动器
 * </p>
 * Author Jming.L
 * Date 2019/10/8 9:56
 */
public class PickerView extends View {

    private int mWidth;
    private int mHeight;
    private int mCenterColor;            //中间选中颜色
    private int mOtherColor;            //其他位置颜色
    private int mLineColor;             //分割线的颜色
    private int mRow;                   //显示的行数
    private int mSpace;                 //每行的间距
    private Paint mLinePaint;
    private Paint mTextPaint;
    private int mTextSize;              //文字大小
    private int mOffset;                //总的滑动偏移值

    private int mDownY;                 //按下时Y的值
    private int mStartY;                //按下时Y的值
    private int mDirection;             //-1表示向上滑动；1表示向下滑动
    private int mMaxVelocity;           //最大滑动速度
    private int mMinVelocity;           //最小滑动速度
    private Scroller mScroller;         //滑动器
    private VelocityTracker mTracker;   //速度计算器

    private OnScrollListener mListener;
    private String mCurrentItem;
    private List<String> mItems;

    public interface OnScrollListener {
        void onScroll(String item);
    }

    public PickerView(Context context) {
        this(context, null, 0);
    }

    public PickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRow = 3;
        mItems = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            mItems.add(String.valueOf(i));
        }
        mCurrentItem = mItems.get(0);
        mLinePaint = new Paint();
        mTextPaint = new Paint();

        mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
        ViewConfiguration configuration = ViewConfiguration.get(context);
        //执行手势动作的最小速度值，当手势速度低于该速度时不进行滑动
        mMinVelocity = configuration.getScaledMinimumFlingVelocity();
        //执行手势动作的最大速度值，当手势速度大于该速度时以最大值进行滑动
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        this.setRow(3);
        this.mTextSize = (int) (mSpace * 0.382f);
        this.mCenterColor = Color.BLACK;
        this.mOtherColor = Color.parseColor("#99000000");
        this.mLineColor = Color.parseColor("#0da5ec");
        this.mOffset = -Math.max(mItems.indexOf(mCurrentItem), 0) * mSpace;
        mTextPaint.setColor(mOtherColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);

        mLinePaint.setColor(mLineColor);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth * 0.5f, mHeight * 0.5f);
        for (int i = 0; i < mItems.size(); i++) {
            String item = mItems.get(i);
            float dY = i * mSpace + mOffset + getTextOffset(mTextPaint);
            mTextPaint.setColor(Math.abs(dY) < mSpace / 2 ? mCenterColor : mOtherColor);
            canvas.drawText(item, 0, dY, mTextPaint);
        }
        canvas.restore();
        canvas.drawLine(0, (mHeight - mSpace) * 0.5f, mWidth, (mHeight - mSpace) * 0.5f, mLinePaint);
        canvas.drawLine(0, (mHeight + mSpace) * 0.5f, mWidth, (mHeight + mSpace) * 0.5f, mLinePaint);
    }

    private float getTextOffset(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.abs(Math.ceil((double) (fm.descent + fm.ascent)) * 0.5D);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }

    /**
     * 设置文字大小
     *
     * @param textSize 文字大小
     */
    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    /**
     * 设置数据
     *
     * @param items 数据集合
     */
    public void setItems(List<String> items) {
        if (items == null) {
            items = new ArrayList<>();
        }
        mItems = items;
        mCurrentItem = mItems.size() > 0 ? mItems.get(0) : "";
        mOffset = 0;
        invalidate();
    }

    /**
     * 设置当前条目
     *
     * @param item 条目名称
     */
    public void setItem(String item) {
        if (!TextUtils.isEmpty(item) && mItems.contains(item)) {
            mCurrentItem = item;
            mOffset = -Math.max(mItems.indexOf(mCurrentItem), 0) * mSpace;
            invalidate();
        }
    }

    /**
     * 获取当前选中条目
     *
     * @return 当前条目
     */
    public String getItem() {
        return mCurrentItem;
    }

    /**
     * 设置显示的奇数列数，默认3列
     *
     * @param row
     */
    public void setRow(int row) {
        row = Math.max(row, 0);
        if (row % 2 == 0) {
            row++;
        }
        this.mRow = row;
        this.mSpace = mHeight / mRow;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        if (action == MotionEvent.ACTION_DOWN) {
            mDownY = (int) event.getY();
            mStartY = (int) event.getY();
            //当按下时滑动强制停止
            mScroller.forceFinished(true);
        }
        if (action == MotionEvent.ACTION_MOVE) {
            //进行手势触摸滑动
            int moveY = (int) event.getY();
            int dY = moveY - mStartY;
            mOffset += dY;
            mStartY = moveY;
            invalidate();
        }
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            int upY = (int) event.getY();
            //计算上下滑动方向
            mDirection = mDownY == upY ? 0 : (upY - mDownY) / Math.abs(upY - mDownY);
            mTracker.computeCurrentVelocity(1000, mMaxVelocity);
            float yVelocity = mTracker.getYVelocity();
            //如果当前的手势速度满足可以滑动的最小手势速度则进行滑动
            if (Math.abs(yVelocity) > mMinVelocity) {
                mScroller.fling(0, mStartY, 0, (int) yVelocity,
                        0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                //计算滑动器将会滑动的距离
                int finalY = mScroller.getFinalY();
                int startX = mScroller.getStartY();
                int scrollerY = finalY - startX;
                //计算滑动停止后最终的偏移距离
                int finalOffset = mOffset + scrollerY;
                //计算下一个索引
                int nextNum = Math.round(finalOffset * 1.0f / mSpace + mDirection * 0.5f);
                //当上下越界时回到最近值
                nextNum = Math.min(nextNum, 0);
                nextNum = Math.max(nextNum, 1 - mItems.size());
                //计算到下一个索引所应偏移的距离
                int unitOffset = nextNum * mSpace - finalOffset;
                //重新矫正滑动器的终点
                finalOffset = finalOffset + unitOffset;
                scrollerY = finalOffset - mOffset;
                finalY = scrollerY + startX;
                mScroller.setFinalY(finalY);
            } else {
                //计算下一个索引
                int nextNum = Math.round(mOffset * 1.0f / mSpace + mDirection * 0.5f);
                //当上下越界时回到最近值
                nextNum = Math.min(nextNum, 0);
                nextNum = Math.max(nextNum, 1 - mItems.size());
                //计算滑动到下一个索引所需要偏移的距离
                int unitOffset = nextNum * mSpace - mOffset;
                mScroller.startScroll(0, mStartY, 0, unitOffset, 1000);
            }
            //computeScroll依赖于该方法的调用
            invalidate();
        }
        return true;
    }

    @Override
    public void computeScroll() {
        //判断滑动是否已经完成
        if (mScroller.computeScrollOffset()) {
            //当前滑动的位置是否等于最终的位置
            if (mScroller.getCurrY() != mScroller.getFinalY()) {
                //当抬起时通过滚动器继续进行滑动
                int moveY = mScroller.getCurrY();
                int dY = moveY - mStartY;
                mOffset += dY;
                mStartY = moveY;
                //computeScroll依赖于该方法的调用
                invalidate();
            } else {
                int index = Math.abs(Math.round(mOffset * 1.0f / mSpace));
                mCurrentItem = mItems.get(index);
                if (mListener != null) {
                    mListener.onScroll(mCurrentItem);
                }
            }
        }
    }

}
