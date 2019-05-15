package com.mmednet.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

//https://blog.csdn.net/gesanri/article/details/48968803
public class GridLayout extends LinearLayout {

    private List<int[]> children;

    public GridLayout(Context context) {
        this(context, null, 0);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        children = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        int left = 0;
        int top = 0;
        int totalHeight = 0;
        int totalWidth = 0;
        int measuredWidth = getMeasuredWidth();
        int share = 4;                              //多少份
        int unit = measuredWidth / share;           //每份多宽

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();

            if (i == 0) {
                totalHeight = params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            }

            if (left + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > measuredWidth) {
                left = 0;
                top += params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
                totalHeight += params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            }

            int[] rect = {left + params.leftMargin, top + params.topMargin,
                    left + params.leftMargin + child.getMeasuredWidth(),
                    top + params.topMargin + child.getMeasuredHeight()};
            children.add(rect);

            int childWidth = params.leftMargin + child.getMeasuredWidth() + params.rightMargin;

            //父布局N等分1.00-0.75-0.50-0.25
            for (int j = share; j > 0; j--) {
                if (childWidth > unit * (j - 1)) {
                    childWidth = unit * j;
                    break;
                }
            }

            left += childWidth;

            if (left > totalWidth) {
                totalWidth = left;
            }
        }

        int height = 0;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = totalHeight;
        }

        int width = 0;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = totalWidth;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            int[] position = children.get(i);
            child.layout(position[0], position[1], position[2], position[3]);
        }
    }

}
