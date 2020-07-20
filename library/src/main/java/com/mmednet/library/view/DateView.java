package com.mmednet.library.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.mmednet.library.util.DensityUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *  * Title:DateView
 *  * <p>
 *  * Description:日历控制器
 *  * </p >
 *  * Author Jming.L
 *  * Date 2020/7/17 17:39
 *  
 */
public class DateView extends LinearLayout {

    private Calendar mCalendar;
    private PickerView mYearView;
    private PickerView mMonthView;
    private PickerView mDayView;
    private PickerView mHourView;
    private PickerView mMinView;
    private PickerView mSecView;

    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setGravity(Gravity.CENTER);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.mCalendar = Calendar.getInstance();
        initView(context);
    }

    private void initView(Context context) {
        mYearView = new PickerView(context);
        mMonthView = new PickerView(context);
        mDayView = new PickerView(context);
        mHourView = new PickerView(context);
        mMinView = new PickerView(context);
        mSecView = new PickerView(context);

        mYearView.setItems(initList(new ArrayList<String>(), 1900, 2100));
        mMonthView.setItems(initList(new ArrayList<String>(), 1, 12));
        mDayView.setItems(initList(new ArrayList<String>(), 1, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)));
        mHourView.setItems(initList(new ArrayList<String>(), 0, 23));
        mMinView.setItems(initList(new ArrayList<String>(), 0, 59));
        mSecView.setItems(initList(new ArrayList<String>(), 0, 59));

        mMonthView.setOnScrollListener(new PickerView.OnScrollListener() {
            private List<String> temp = new ArrayList<>();

            @Override
            public void onScroll(String item) {
                mCalendar.set(Calendar.YEAR, Integer.valueOf(mYearView.getItem()));
                mCalendar.set(Calendar.MONTH, Integer.valueOf(item) - 1);
                int maxItem = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                mDayView.setItems(initList(temp, 1, maxItem));
                int dayItem = Integer.valueOf(mDayView.getItem());
                if (dayItem > maxItem) {
                    dayItem = maxItem;
                }
                mDayView.setItem(String.valueOf(dayItem));
            }
        });

        int size = (int) DensityUtils.px2dp(context, 5);
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 2);
        params.leftMargin = size;
        params.rightMargin = size;
        mYearView.setLayoutParams(params);
        mMonthView.setLayoutParams(params);
        mDayView.setLayoutParams(params);
        mHourView.setLayoutParams(params);
        mMinView.setLayoutParams(params);
        mSecView.setLayoutParams(params);

        this.addView(mYearView);
        this.addView(mMonthView);
        this.addView(mDayView);
        this.addView(mHourView);
        this.addView(mMinView);
        this.addView(mSecView);
        this.setDateType(true, true, true, true, true, false);
    }

    public void setDateType(boolean year, boolean month, boolean day) {
        setDateType(year, month, day, false, false, false);
    }

    public void setDateType(boolean year, boolean month, boolean day, boolean hour, boolean minute, boolean second) {
        mYearView.setVisibility(year ? View.VISIBLE : View.GONE);
        mMonthView.setVisibility(month ? View.VISIBLE : View.GONE);
        mDayView.setVisibility(day ? View.VISIBLE : View.GONE);
        mHourView.setVisibility(hour ? View.VISIBLE : View.GONE);
        mMinView.setVisibility(minute ? View.VISIBLE : View.GONE);
        mSecView.setVisibility(second ? View.VISIBLE : View.GONE);
    }

    public void setCurrentTime(Date date) {
        mCalendar.setTime(date);

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        int second = mCalendar.get(Calendar.SECOND);

        mYearView.setItem(String.valueOf(year));
        mMonthView.setItem(String.valueOf(month));
        mDayView.setItem(String.valueOf(day));
        mHourView.setItem(String.valueOf(hour));
        mMinView.setItem(String.valueOf(minute));
        mSecView.setItem(String.valueOf(second));
    }

    public Date getCurrentTime() {
        int year = Integer.valueOf(mYearView.getItem());
        int month = Integer.valueOf(mMonthView.getItem());
        int day = Integer.valueOf(mDayView.getItem());
        int hour = Integer.valueOf(mHourView.getItem());
        int minute = Integer.valueOf(mMinView.getItem());
        int second = Integer.valueOf(mSecView.getItem());

        mCalendar.set(year, month - 1, day, hour, minute, second);

        return mCalendar.getTime();
    }

    private List<String> initList(List<String> list, int start, int end) {
        list.clear();
        for (int i = start; i <= end; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }
}
