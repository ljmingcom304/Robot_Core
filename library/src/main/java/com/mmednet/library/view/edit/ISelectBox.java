package com.mmednet.library.view.edit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmednet.library.R;
import com.mmednet.library.common.Value;
import com.mmednet.library.util.UIUtils;
import com.mmednet.library.view.DrawableTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:SelectBox
 * <p>
 * Description:复选框
 * </p>
 * Author Jming.L
 * Date 2017/11/10 14:01
 */
public abstract class ISelectBox extends LinearLayout implements EditView {

    private boolean mEditable;
    private Context mContext;
    private Drawable mLeftDrawable;
    private Drawable mRightDrawable;
    protected List<TextView> mList;
    protected List<String> texts;

    protected OnItemEditListener mEditListener;
    private int mBackgroundResId;
    private int mTextColor;
    private int mTextSize;
    private int mHintColor;

    protected int mPadding = Value.VALUE_INT;

    public ISelectBox(Context context) {
        this(context, null);
    }

    public ISelectBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        if (mPadding == Value.VALUE_INT) {
            mPadding = UIUtils.getDimens(context, R.dimen.size_rect_padding);
        }

        initView();
    }

    private void initView() {
        mList = new ArrayList<>();
        texts = new ArrayList<>();
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
        this.setEditable(true);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    public boolean isEditable() {
        return mEditable;
    }

    @Override
    public void setEditable(boolean editable) {
        mEditable = editable;
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            textView.setEnabled(mEditable);
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
            textView.setTextColor(mTextColor);
        }
    }

    @Override
    public void setHintColor(int color) {
        this.mHintColor = color;
    }

    @Override
    public void addHint(String... hint) {
        if (hint != null) {
            for (int i = 0; i < hint.length; i++) {
                TextView textView = getHintTextView(hint[i]);
                this.addView(textView);
                mList.add(textView);
            }
        }
    }

    @Override
    public void setHint(String... hint) {
        if (hint != null) {
            removeAllViews();
            mList.clear();
            addHint(hint);
        }
    }

    private TextView getHintTextView(String hint) {
        final DrawableTextView textView = new DrawableTextView(mContext);
        textView.setAlignCenter(false);
        textView.setIncludeFontPadding(false);
        textView.setPadding(mPadding, mPadding, mPadding, mPadding);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setTextColor(mTextColor);
        textView.setCompoundDrawablePadding(mPadding * 2);
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(mBackgroundResId, 0, 0, 0);
        textView.setText(hint);
        textView.setSelected(false);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(v);
                if (mEditListener != null) {
                    mEditListener.onClick(textView.getText().toString(), textView.isSelected());
                }
            }
        });
        return textView;
    }

    @Override
    public List<String> getHints() {
        ArrayList<String> list = new ArrayList<>();
        for (TextView textView : mList) {
            list.add(textView.getText().toString());
        }
        return list;
    }

    /**
     * 触发点击事件后的单选或复选逻辑
     *
     * @param view 被点击的控件
     */
    protected abstract void selectItem(View view);

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
        //左侧背景图形
        if (left != null) {
            TextDrawable drawable = new TextDrawable(mContext);
            drawable.setTextColor(mHintColor);
            drawable.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
            drawable.setText(left);
            this.setLeftDrawable(drawable);
        }
    }

    @Override
    public void setLeftDrawable(Drawable left) {
        mLeftDrawable = left;
        for (TextView tv : mList) {
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(mLeftDrawable, null, mRightDrawable, null);
        }
    }

    @Override
    public void setRightDrawable(String right) {
        //左侧背景图形
        if (right != null) {
            TextDrawable drawable = new TextDrawable(mContext);
            drawable.setTextColor(mHintColor);
            drawable.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
            drawable.setText(right);
            this.setRightDrawable(drawable);
        }
    }

    @Override
    public void setRightDrawable(Drawable right) {
        mRightDrawable = right;
        for (TextView tv : mList) {
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(mLeftDrawable, null, mRightDrawable, null);
        }
    }

    @Override
    public void setEditInputType(int type) {
    }

}
