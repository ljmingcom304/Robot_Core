package com.mmednet.view.picker;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mmednet.view.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Title:DatePicker
 * <p>
 * Description:日历控件
 * </p>
 * Author Jming.L
 * Date 2018/7/5 13:24
 */
public class DatePicker {

    enum SCROLL_TYPE {
        HOUR(1),
        MINUTE(2);

        public int value;

        SCROLL_TYPE(int value) {
            this.value = value;
        }
    }

    public interface Callback {
        void onBack(String result);
    }

    public static final int ALL = 1;
    public static final int Y_M = 2;
    public static final int M_D = 3;
    public static final int Y_M_D = 4;
    public static final int H_M = 5;

    private int type;
    private int scrollUnits;
    private Callback handler;
    private Context context;
    private boolean canAccess;
    private Dialog pickerDialog;
    private TimePicker year_pv;
    private TimePicker month_pv;
    private TimePicker day_pv;
    private TimePicker hour_pv;
    private TimePicker minute_pv;
    private static final int MAX_MINUTE = 59;
    private static final int MAX_HOUR = 23;
    private static final int MAX_MONTH = 12;
    private ArrayList<String> yearList;
    private ArrayList<String> monthList;
    private ArrayList<String> dayList;
    private ArrayList<String> hourList;
    private ArrayList<String> minuteList;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
    private boolean spanYear, spanMon, spanDay, spanHour, spanMin;
    private Calendar selectedCalender, startCalendar, endCalendar;

    private RelativeLayout mRlTitle;//顶部布局
    private LinearLayout mLlContent;//底部布局
    private TextView tvTitle;       //标题
    private TextView tvCancel;      //取消
    private TextView tvSelect;      //确定

    public DatePicker(Context context, Callback callback, String startDate, String endDate) {
        scrollUnits = DatePicker.SCROLL_TYPE.HOUR.value + DatePicker.SCROLL_TYPE.MINUTE.value;
        canAccess = false;
        if (isValidDate(startDate, "yyyy-MM-dd HH:mm") && isValidDate(endDate, "yyyy-MM-dd HH:mm")) {
            canAccess = true;
            this.context = context;
            this.handler = callback;
            selectedCalender = Calendar.getInstance();
            startCalendar = Calendar.getInstance();
            endCalendar = Calendar.getInstance();
            @SuppressLint( "SimpleDateFormat" )
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

            try {
                startCalendar.setTime(sdf.parse(startDate));
                endCalendar.setTime(sdf.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            initDialog();
            initView();
        }

    }

    private void initDialog() {
        if (pickerDialog == null) {
            pickerDialog = new Dialog(context, R.style.view_time_dialog);
            pickerDialog.setCancelable(true);
            pickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pickerDialog.setContentView(R.layout.view_picker_dialog);
            Window window = pickerDialog.getWindow();
            window.setGravity(80);
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            window.setAttributes(lp);
        }

    }

    public void setPickerColor(int mainColor, int otherColor) {
        year_pv.setColor(mainColor, otherColor);
        month_pv.setColor(mainColor, otherColor);
        day_pv.setColor(mainColor, otherColor);
        hour_pv.setColor(mainColor, otherColor);
        minute_pv.setColor(mainColor, otherColor);
    }

    public TextView getCancelView() {
        return tvCancel;
    }

    public TextView getCertainView() {
        return tvSelect;
    }

    public TextView getTitleView() {
        return tvTitle;
    }

    /**
     * 顶部布局
     */
    public ViewGroup getTitleGroup() {
        return mRlTitle;
    }

    /**
     * 底部布局
     */
    public ViewGroup getContentGroup() {
        return mLlContent;
    }

    private void initView() {
        mRlTitle = (RelativeLayout) pickerDialog.findViewById(R.id.rl_title);
        mLlContent = (LinearLayout) pickerDialog.findViewById(R.id.ll_content);
        tvTitle = (TextView) pickerDialog.findViewById(R.id.tv_title);
        year_pv = (TimePicker) pickerDialog.findViewById(R.id.year_pv);
        month_pv = (TimePicker) pickerDialog.findViewById(R.id.month_pv);
        day_pv = (TimePicker) pickerDialog.findViewById(R.id.day_pv);
        hour_pv = (TimePicker) pickerDialog.findViewById(R.id.hour_pv);
        minute_pv = (TimePicker) pickerDialog.findViewById(R.id.minute_pv);
        tvCancel = (TextView) pickerDialog.findViewById(R.id.tv_cancle);
        tvSelect = (TextView) pickerDialog.findViewById(R.id.tv_select);
        tvCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                pickerDialog.dismiss();
            }
        });
        tvSelect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                switch (type) {
                    case ALL:
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                        break;
                    case Y_M:
                        sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
                        break;
                    case M_D:
                        sdf = new SimpleDateFormat("MM-dd", Locale.CHINA);
                        break;
                    case Y_M_D:
                        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                        break;
                    case H_M:
                        sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
                    default:
                        break;
                }

                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                String date = sdf.format(selectedCalender.getTime());
                Log.i("DatePicker", (selectedCalender.get(Calendar.MONTH) + 1) + "月"
                        + (selectedCalender.get(Calendar.DAY_OF_MONTH)) + "日");
                handler.onBack(date);
                pickerDialog.dismiss();
            }
        });
    }

    private void initParameter() {
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        startMinute = startCalendar.get(Calendar.MINUTE);
        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        endMinute = endCalendar.get(Calendar.MINUTE);
        spanYear = startYear != endYear;
        spanMon = (!spanYear) && (startMonth != endMonth);
        spanDay = (!spanMon) && (startDay != endDay);
        spanHour = (!spanDay) && (startHour != endHour);
        spanMin = (!spanHour) && (startMinute != endMinute);
        selectedCalender.setTime(startCalendar.getTime());
    }

    private void initTimer() {
        initArrayList();
        if (spanYear) {
            for (int i = startYear; i <= endYear; ++i) {
                yearList.add(i + "年");
            }

            for (int i = startMonth; i <= MAX_MONTH; ++i) {
                monthList.add(formatTimeUnit(i) + "月");
            }

            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
                dayList.add(formatTimeUnit(i) + "日");
            }

            if ((scrollUnits & DatePicker.SCROLL_TYPE.HOUR.value) != DatePicker.SCROLL_TYPE.HOUR.value) {
                hourList.add(formatTimeUnit(startHour) + "时");
            } else {
                for (int i = startHour; i <= MAX_HOUR; ++i) {
                    hourList.add(formatTimeUnit(i) + "时");
                }
            }

            if ((scrollUnits & DatePicker.SCROLL_TYPE.MINUTE.value) != DatePicker.SCROLL_TYPE.MINUTE.value) {
                minuteList.add(formatTimeUnit(startMinute) + "分");
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; ++i) {
                    minuteList.add(formatTimeUnit(i) + "分");
                }
            }
        } else if (spanMon) {
            yearList.add(startYear + "年");

            for (int i = startMonth; i <= endMonth; ++i) {
                monthList.add(formatTimeUnit(i) + "月");
            }

            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
                dayList.add(formatTimeUnit(i) + "日");
            }

            if ((scrollUnits & DatePicker.SCROLL_TYPE.HOUR.value) != DatePicker.SCROLL_TYPE.HOUR.value) {
                hourList.add(formatTimeUnit(startHour) + "时");
            } else {
                for (int i = startHour; i <= MAX_HOUR; ++i) {
                    hourList.add(formatTimeUnit(i) + "时");
                }
            }

            if ((scrollUnits & DatePicker.SCROLL_TYPE.MINUTE.value) != DatePicker.SCROLL_TYPE.MINUTE.value) {
                minuteList.add(formatTimeUnit(startMinute) + "分");
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; ++i) {
                    minuteList.add(formatTimeUnit(i) + "分");
                }
            }
        } else if (spanDay) {
            yearList.add(startYear + "年");
            monthList.add(formatTimeUnit(startMonth) + "月");

            for (int i = startDay; i <= endDay; ++i) {
                dayList.add(formatTimeUnit(i) + "日");
            }

            if ((scrollUnits & DatePicker.SCROLL_TYPE.HOUR.value) != DatePicker.SCROLL_TYPE.HOUR.value) {
                hourList.add(formatTimeUnit(startHour) + "时");
            } else {
                for (int i = startHour; i <= MAX_HOUR; ++i) {
                    hourList.add(formatTimeUnit(i) + "时");
                }
            }

            if ((scrollUnits & DatePicker.SCROLL_TYPE.MINUTE.value) != DatePicker.SCROLL_TYPE.MINUTE.value) {
                minuteList.add(formatTimeUnit(startMinute) + "分");
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; ++i) {
                    minuteList.add(formatTimeUnit(i) + "分");
                }
            }
        } else if (spanHour) {
            yearList.add(startYear + "年");
            monthList.add(formatTimeUnit(startMonth) + "月");
            dayList.add(formatTimeUnit(startDay) + "日");
            if ((scrollUnits & DatePicker.SCROLL_TYPE.HOUR.value) != DatePicker.SCROLL_TYPE.HOUR.value) {
                hourList.add(formatTimeUnit(startHour) + "时");
            } else {
                for (int i = startHour; i <= endHour; ++i) {
                    hourList.add(formatTimeUnit(i) + "时");
                }
            }

            if ((scrollUnits & DatePicker.SCROLL_TYPE.MINUTE.value) != DatePicker.SCROLL_TYPE.MINUTE.value) {
                minuteList.add(formatTimeUnit(startMinute) + "分");
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; ++i) {
                    minuteList.add(formatTimeUnit(i) + "分");
                }
            }
        } else if (spanMin) {
            yearList.add(startYear + "年");
            monthList.add(formatTimeUnit(startMonth) + "月");
            dayList.add(formatTimeUnit(startDay) + "日");
            hourList.add(formatTimeUnit(startHour) + "时");
            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minuteList.add(formatTimeUnit(startMinute) + "分");
            } else {
                for (int i = startMinute; i <= endMinute; ++i) {
                    minuteList.add(formatTimeUnit(i) + "分");
                }
            }
        }

        loadComponent();
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void initArrayList() {
        if (yearList == null) {
            yearList = new ArrayList<>();
        }

        if (monthList == null) {
            monthList = new ArrayList<>();
        }

        if (dayList == null) {
            dayList = new ArrayList<>();
        }

        if (hourList == null) {
            hourList = new ArrayList<>();
        }

        if (minuteList == null) {
            minuteList = new ArrayList<>();
        }
        yearList.clear();
        monthList.clear();
        dayList.clear();
        hourList.clear();
        minuteList.clear();
    }

    private void loadComponent() {
        year_pv.setData(yearList);
        month_pv.setData(monthList);
        day_pv.setData(dayList);
        hour_pv.setData(hourList);
        minute_pv.setData(minuteList);
        year_pv.setSelected(0);
        month_pv.setSelected(0);
        day_pv.setSelected(0);
        hour_pv.setSelected(0);
        minute_pv.setSelected(0);
        executeScroll();
    }

    private void addListener() {
        year_pv.setOnSelectListener(new TimePicker.onSelectListener() {
            public void onSelect(String text) {
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(text.replace("年", "")));
                monthChange();
            }
        });
        month_pv.setOnSelectListener(new TimePicker.onSelectListener() {
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(text.replace("月", "")) - 1);
                dayChange();
            }
        });
        day_pv.setOnSelectListener(new TimePicker.onSelectListener() {
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text.replace("日", "")));
                hourChange();
            }
        });
        hour_pv.setOnSelectListener(new TimePicker.onSelectListener() {
            public void onSelect(String text) {
                selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text.replace("时", "")));
                minuteChange();
            }
        });
        minute_pv.setOnSelectListener(new TimePicker.onSelectListener() {
            public void onSelect(String text) {
                selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text.replace("分", "")));
            }
        });
    }

    private void monthChange() {
        monthList.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAX_MONTH; ++i) {
                monthList.add(formatTimeUnit(i) + "月");
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; ++i) {
                monthList.add(formatTimeUnit(i) + "月");
            }
        } else {
            for (int i = 1; i <= MAX_MONTH; ++i) {
                monthList.add(formatTimeUnit(i) + "月");
            }
        }

        selectedCalender.set(Calendar.MONTH, Integer.parseInt(((String) monthList.get(0)).replace("月", "")) - 1);
        month_pv.setData(monthList);
        month_pv.setSelected(0);
        executeAnimator(month_pv);
        month_pv.postDelayed(new Runnable() {
            public void run() {
                dayChange();
            }
        }, 100L);
    }

    private void dayChange() {
        dayList.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
                dayList.add(formatTimeUnit(i) + "日");
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; ++i) {
                dayList.add(formatTimeUnit(i) + "日");
            }
        } else {
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
                dayList.add(formatTimeUnit(i) + "日");
            }
        }

        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(((String) dayList.get(0)).replace("日", "")));
        day_pv.setData(dayList);
        day_pv.setSelected(0);
        executeAnimator(day_pv);
        day_pv.postDelayed(new Runnable() {
            public void run() {
                hourChange();
            }
        }, 100L);
    }

    private void hourChange() {
        if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
            hourList.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                for (int i = startHour; i <= MAX_HOUR; ++i) {
                    hourList.add(formatTimeUnit(i) + "时");
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                for (int i = 0; i <= endHour; ++i) {
                    hourList.add(formatTimeUnit(i) + "时");
                }
            } else {
                for (int i = 0; i <= MAX_HOUR; ++i) {
                    hourList.add(formatTimeUnit(i) + "时");
                }
            }

            selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(((String) hourList.get(0)).replace("时", "")));
            hour_pv.setData(hourList);
            hour_pv.setSelected(0);
            executeAnimator(hour_pv);
        }

        hour_pv.postDelayed(new Runnable() {
            public void run() {
                minuteChange();
            }
        }, 100L);
    }

    private void minuteChange() {
        if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
            minuteList.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                for (int i = startMinute; i <= MAX_MINUTE; ++i) {
                    minuteList.add(formatTimeUnit(i) + "分");
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                for (int i = 0; i <= endMinute; ++i) {
                    minuteList.add(formatTimeUnit(i) + "分");
                }
            } else {
                for (int i = 0; i <= MAX_MINUTE; ++i) {
                    minuteList.add(formatTimeUnit(i) + "分");
                }
            }

            selectedCalender.set(Calendar.MINUTE, Integer.parseInt(((String) minuteList.get(0)).replace("分", "")));
            minute_pv.setData(minuteList);
            minute_pv.setSelected(0);
            executeAnimator(minute_pv);
        }

        executeScroll();
    }

    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }

    private void executeScroll() {
        year_pv.setCanScroll(yearList.size() > 1);
        month_pv.setCanScroll(monthList.size() > 1);
        day_pv.setCanScroll(dayList.size() > 1);
        hour_pv.setCanScroll(hourList.size() > 1 && (scrollUnits & DatePicker.SCROLL_TYPE.HOUR.value) == DatePicker.SCROLL_TYPE.HOUR.value);
        minute_pv.setCanScroll(minuteList.size() > 1 && (scrollUnits & DatePicker.SCROLL_TYPE.MINUTE.value) == DatePicker.SCROLL_TYPE.MINUTE.value);
    }

    private int disScrollUnit(DatePicker.SCROLL_TYPE... scroll_types) {
        if (scroll_types != null && scroll_types.length != 0) {
            DatePicker.SCROLL_TYPE[] types = scroll_types;
            int count = scroll_types.length;

            for (int i = 0; i < count; i++) {
                DatePicker.SCROLL_TYPE scroll_type = types[i];
                scrollUnits ^= scroll_type.value;
            }
        } else {
            scrollUnits = DatePicker.SCROLL_TYPE.HOUR.value + DatePicker.SCROLL_TYPE.MINUTE.value;
        }

        return scrollUnits;
    }

    public void show(String time) {
        if (canAccess) {
            if (isValidDate(time, "yyyy-MM-dd")) {
                if (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()) {
                    canAccess = true;
                    initParameter();
                    initTimer();
                    addListener();
                    setSelectedTime(time);
                    pickerDialog.show();
                }
            } else {
                canAccess = false;
            }
        }

    }

    /**
     * 设置日期控件是否显示时和分
     */
    public void showSpecificTime(int show) {
        type = show;
        if (canAccess) {
            switch (show) {
                case DatePicker.ALL:
                    disScrollUnit();
                    year_pv.setVisibility(View.VISIBLE);
                    month_pv.setVisibility(View.VISIBLE);
                    day_pv.setVisibility(View.VISIBLE);
                    hour_pv.setVisibility(View.VISIBLE);
                    minute_pv.setVisibility(View.VISIBLE);
                    break;
                case DatePicker.Y_M:
                    disScrollUnit(DatePicker.SCROLL_TYPE.HOUR, DatePicker.SCROLL_TYPE.MINUTE);
                    year_pv.setVisibility(View.VISIBLE);
                    month_pv.setVisibility(View.VISIBLE);
                    day_pv.setVisibility(View.GONE);
                    hour_pv.setVisibility(View.GONE);
                    minute_pv.setVisibility(View.GONE);
                    break;
                case DatePicker.M_D:
                    disScrollUnit(DatePicker.SCROLL_TYPE.HOUR, DatePicker.SCROLL_TYPE.MINUTE);
                    year_pv.setVisibility(View.GONE);
                    month_pv.setVisibility(View.VISIBLE);
                    day_pv.setVisibility(View.VISIBLE);
                    hour_pv.setVisibility(View.GONE);
                    minute_pv.setVisibility(View.GONE);
                    break;
                case DatePicker.Y_M_D:
                    disScrollUnit(DatePicker.SCROLL_TYPE.HOUR, DatePicker.SCROLL_TYPE.MINUTE);
                    year_pv.setVisibility(View.VISIBLE);
                    month_pv.setVisibility(View.VISIBLE);
                    day_pv.setVisibility(View.VISIBLE);
                    hour_pv.setVisibility(View.GONE);
                    minute_pv.setVisibility(View.GONE);
                    break;
                case DatePicker.H_M:
                    disScrollUnit();
                    year_pv.setVisibility(View.GONE);
                    month_pv.setVisibility(View.GONE);
                    day_pv.setVisibility(View.GONE);
                    hour_pv.setVisibility(View.VISIBLE);
                    minute_pv.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    public void setIsLoop(boolean isLoop) {
        if (canAccess) {
            year_pv.setIsLoop(isLoop);
            month_pv.setIsLoop(isLoop);
            day_pv.setIsLoop(isLoop);
            hour_pv.setIsLoop(isLoop);
            minute_pv.setIsLoop(isLoop);
        }

    }

    /**
     * 设置日期控件默认选中的时间
     */
    public void setSelectedTime(String time) {
        if (canAccess) {
            String[] str = time.split(" ");
            String[] dateStr = str[0].split("-");
            year_pv.setSelected(dateStr[0] + "年");
            selectedCalender.set(Calendar.YEAR, Integer.parseInt(dateStr[0]));
            monthList.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth;
            if (selectedYear == startYear) {
                for (selectedMonth = startMonth; selectedMonth <= MAX_MONTH; ++selectedMonth) {
                    monthList.add(formatTimeUnit(selectedMonth) + "月");
                }
            } else if (selectedYear == endYear) {
                for (selectedMonth = 1; selectedMonth <= endMonth; ++selectedMonth) {
                    monthList.add(formatTimeUnit(selectedMonth) + "月");
                }
            } else {
                for (selectedMonth = 1; selectedMonth <= MAX_MONTH; ++selectedMonth) {
                    monthList.add(formatTimeUnit(selectedMonth) + "月");
                }
            }

            month_pv.setData(monthList);
            month_pv.setSelected(dateStr[1] + "月");
            selectedCalender.set(Calendar.MONTH, Integer.parseInt(dateStr[1]) - 1);
            executeAnimator(month_pv);
            dayList.clear();
            selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            if (selectedYear == startYear && selectedMonth == startMonth) {
                for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
                    dayList.add(formatTimeUnit(i) + "日");
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth) {
                for (int i = 1; i <= endDay; ++i) {
                    dayList.add(formatTimeUnit(i) + "日");
                }
            } else {
                for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
                    dayList.add(formatTimeUnit(i) + "日");
                }
            }

            day_pv.setData(dayList);
            day_pv.setSelected(dateStr[2] + "日");
            selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[2]));
            executeAnimator(day_pv);
            if (str.length == 2) {
                String[] timeStr = str[1].split(":");
                if ((scrollUnits & DatePicker.SCROLL_TYPE.HOUR.value) == DatePicker.SCROLL_TYPE.HOUR.value) {
                    hourList.clear();
                    int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
                    if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                        for (int i = startHour; i <= MAX_HOUR; ++i) {
                            hourList.add(formatTimeUnit(i) + "时");
                        }
                    } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                        for (int i = 0; i <= endHour; ++i) {
                            hourList.add(formatTimeUnit(i) + "时");
                        }
                    } else {
                        for (int i = 0; i <= MAX_HOUR; ++i) {
                            hourList.add(formatTimeUnit(i) + "时");
                        }
                    }

                    hour_pv.setData(hourList);
                    hour_pv.setSelected(timeStr[0] + "时");
                    selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr[0]));
                    executeAnimator(hour_pv);
                }

                if ((scrollUnits & DatePicker.SCROLL_TYPE.MINUTE.value) == DatePicker.SCROLL_TYPE.MINUTE.value) {
                    minuteList.clear();
                    int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
                    int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
                    if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                        for (int i = startMinute; i <= MAX_MINUTE; ++i) {
                            minuteList.add(formatTimeUnit(i) + "分");
                        }
                    } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                        for (int i = 0; i <= endMinute; ++i) {
                            minuteList.add(formatTimeUnit(i) + "分");
                        }
                    } else {
                        for (int i = 0; i <= MAX_MINUTE; ++i) {
                            minuteList.add(formatTimeUnit(i) + "分");
                        }
                    }

                    minute_pv.setData(minuteList);
                    minute_pv.setSelected(timeStr[1] + "分");
                    selectedCalender.set(Calendar.MINUTE, Integer.parseInt(timeStr[1]));
                    executeAnimator(minute_pv);
                }
            }

            executeScroll();
        }

    }

    /**
     * 验证字符串是否是一个合法的日期格式
     */
    private boolean isValidDate(String date, String template) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat(template);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        try {
            format.setLenient(false);
            format.parse(date);
        } catch (Exception var6) {
            convertSuccess = false;
        }

        return convertSuccess;
    }

}
