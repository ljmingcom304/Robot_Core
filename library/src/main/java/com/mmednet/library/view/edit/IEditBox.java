package com.mmednet.library.view.edit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mmednet.library.R;
import com.mmednet.library.common.Value;
import com.mmednet.library.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:IEditBox
 * <p>
 * Description:单选框和复选框
 * </p>
 * Author Jming.L
 * Date 2017/9/27 14:43
 */
public abstract class IEditBox extends TextViewLayout implements EditView {

    protected boolean mEditable;
    private Context mContext;
    protected List<TextView> mList;
    protected List<String> texts;
    protected OnItemEditListener mEditListener;

    private int mHintColor;
    private int mTextColor;
    private int mTextSize;

    private int mBackgroundResId;
    private int mMinWidth;

    private int mPadding;

    public IEditBox(Context context) {
        this(context, null);
    }

    public IEditBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mMinWidth = UIUtils.getDimens(context, R.dimen.core_size_80);
        mPadding = UIUtils.getDimens(context, R.dimen.size_rect_padding);
        initView();
    }

    private void initView() {
        mList = new ArrayList<>();
        texts = new ArrayList<>();
        this.setEditable(true);
    }

    @Override
    public boolean isEditable() {
        return mEditable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.mEditable = editable;
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            textView.setEnabled(mEditable);
            textView.setAlpha(mEditable ? 1.0f : 0.5f);
        }
    }

    @Override
    public void setOnItemEditListener(OnItemEditListener listener) {
        mEditListener = listener;
    }

    @Override
    public void setTxtSize(int size) {
        mTextSize = size;
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
    }

    @Override
    public void setTxtColor(int color) {
        mTextColor = color;
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            textView.setTextColor(getStateList(mHintColor, mTextColor));
        }
    }

    @Override
    public void setHintColor(int color) {
        mHintColor = color;
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            textView.setTextColor(getStateList(mHintColor, mTextColor));
        }
    }

    @Override
    public void addHint(String... hint) {
        if (hint != null) {
            for (String h : hint) {
                TextView textView = getHintTextView(h);
                this.addView(textView);
                mList.add(textView);
            }
        }
    }

    @Override
    public void setHint(String... hint) {
        removeAllViews();
        mList.clear();
        if (hint != null) {
            for (String h : hint) {
                TextView textView = getHintTextView(h);
                this.addView(textView);
                mList.add(textView);
            }
        }
    }

    @Override
    public List<String> getHints() {
        ArrayList<String> list = new ArrayList<>();
        for (TextView textView : mList) {
            list.add(textView.getText().toString());
        }
        return list;
    }

    private TextView getHintTextView(String hint) {
        final TextView textView = new TextView(mContext);
        textView.setIncludeFontPadding(false);
        textView.setPadding(mPadding * 2, mPadding, mPadding * 2, mPadding);
        textView.setGravity(Gravity.CENTER);
        textView.setMinWidth(mMinWidth);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setTextColor(getStateList(mHintColor, mTextColor));
        textView.setBackgroundResource(mBackgroundResId);
        textView.setText(hint);
        textView.setSelected(false);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem((TextView) v);
                if (mEditListener != null) {
                    mEditListener.onClick(textView.getText().toString(), textView.isSelected());
                }
            }
        });
        return textView;
    }

    private ColorStateList getStateList(int normal, int selected) {
        int[] colors = new int[]{selected, normal};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        return new ColorStateList(states, colors);
    }

    /**
     * 触发点击事件后的单选或复选逻辑
     *
     * @param view 被点击的控件
     */
    protected abstract void selectItem(TextView view);

    @Override
    public abstract void setTexts(String... text);

    @Override
    public abstract List<String> getTexts();

    @Override
    public void setBackgroundView(int resId) {
        this.mBackgroundResId = resId;
    }

    @Override
    public void setLeftDrawable(String left) {
    }

    @Override
    public void setLeftDrawable(Drawable left) {
    }

    @Override
    public void setRightDrawable(String right) {

    }

    @Override
    public void setRightDrawable(Drawable right) {

    }

    @Override
    public void setEditInputType(int type) {
    }
}
