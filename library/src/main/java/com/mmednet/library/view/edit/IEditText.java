package com.mmednet.library.view.edit;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmednet.library.R;
import com.mmednet.library.common.Value;
import com.mmednet.library.util.DensityUtils;
import com.mmednet.library.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:IEditText
 * <p>
 * Description:编辑框
 * </p>
 * Author Jming.L
 * Date 2017/9/22 15:46
 */
@SuppressLint( "AppCompatCustomView" )
public class IEditText extends EditText implements EditView {

    private Context mContext;
    private List<String> texts;
    protected String mDefault = "- - - - -";
    protected String mText;                 //内容
    protected String mHint;
    protected boolean mEditable;
    protected Drawable mRDrawable;          //右侧图形
    protected Drawable mLDrawable;          //左侧图形

    private int mPadding = Value.VALUE_INT;
    private int mBackgroundResId;

    protected int mTextColor;
    protected int mTextSize;
    protected int mHintColor;

    private int paddingT = Value.VALUE_INT;
    private int paddingB = Value.VALUE_INT;

    private LinearLayout.LayoutParams mParams01;
    private LinearLayout.LayoutParams mParams02;

    public IEditText(Context context) {
        this(context, null);
    }

    public IEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mParams01 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams02 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        if (mPadding == Value.VALUE_INT) {
            mPadding = UIUtils.getDimens(context, R.dimen.size_title_padding);
        }

        if (paddingT == Value.VALUE_INT) {
            paddingT = UIUtils.getDimens(context, R.dimen.core_size_10);
        }
        if (paddingB == Value.VALUE_INT) {
            paddingB = UIUtils.getDimens(context, R.dimen.core_size_7);
        }

        initView();
    }

    private void initView() {
        texts = new ArrayList<>();
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setSingleLine(true);
        this.setEllipsize(TextUtils.TruncateAt.END);
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        this.setTextColor(mTextColor);
        this.setIncludeFontPadding(false);
        this.setPadding(0, paddingT, 0, paddingB);
        this.setCompoundDrawablePadding(mPadding);
        this.addTextChangedListener(new ITextWatcher(this));
        this.setEditable(true);
    }

    @Override
    public boolean isEditable() {
        return mEditable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.mEditable = editable;
        //获取输入框中内容
        String text = "";
        List<String> texts = getTexts();
        if (texts.size() > 0) {
            text = texts.get(0);
        }
        if (editable) {// 可编辑
            this.setLayoutParams(mParams01);
            this.setText(text);
            this.setHintTextColor(mHintColor);
            this.setHint(mHint);
            this.setEnabled(true);
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            this.clearFocus();//不获取光标
            this.setBackgroundResource(mBackgroundResId);
        } else {// 不可编辑
            this.setLayoutParams(mParams02);
            if (TextUtils.isEmpty(text)) {
                text = mDefault;
            }
            this.setText(text);
            this.setEnabled(false);
            this.setFocusable(false);
            this.setFocusableInTouchMode(false);
            this.clearFocus();
            this.setBackground(null);
        }

        Drawable lDrawable = null;
        Drawable rDrawable = null;
        if (mLDrawable instanceof TextDrawable) {
            ((TextDrawable) mLDrawable).setEnabled(mEditable);
            lDrawable = mLDrawable;
        } else if (mEditable) {
            lDrawable = mLDrawable;
        }
        if (mRDrawable instanceof TextDrawable) {
            ((TextDrawable) mRDrawable).setEnabled(mEditable);
            rDrawable = mRDrawable;
        } else if (mEditable) {
            rDrawable = mRDrawable;
        }
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(lDrawable, null, rDrawable, null);
    }

    @Override
    public void setTxtSize(int size) {
        mTextSize = size;
        if (mLDrawable instanceof TextDrawable) {
            ((TextDrawable) mLDrawable).setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
        if (mRDrawable instanceof TextDrawable) {
            ((TextDrawable) mRDrawable).setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

    @Override
    public void setHintColor(int color) {
        mHintColor = color;
        if (mLDrawable instanceof TextDrawable) {
            ((TextDrawable) mLDrawable).setTextColor(mHintColor);
        }
        if (mRDrawable instanceof TextDrawable) {
            ((TextDrawable) mRDrawable).setTextColor(mHintColor);
        }

        if (mEditable) {
            this.setHintTextColor(mHintColor);
        }
    }

    @Override
    public void setTxtColor(int color) {
        mTextColor = color;
        this.setTextColor(mTextColor);
    }

    private ColorStateList getColorStateList() {
        int[] colors = new int[]{mHintColor, mTextColor};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_enabled};
        states[1] = new int[]{};
        return new ColorStateList(states, colors);
    }

    @Override
    public void addHint(String... hint) {
        this.setHint(hint);
    }

    @Override
    public void setHint(String... hint) {
        if (hint != null) {
            for (String h : hint) {
                if (h != null) {
                    this.setHint(h);
                    mHint = h;
                    break;
                }
            }
        }
    }

    @Override
    public List<String> getHints() {
        ArrayList<String> hints = new ArrayList<>();
        hints.add(mHint);
        return hints;
    }

    @Override
    public void setTexts(String... text) {
        if (text != null) {
            for (String t : text) {
                if (t != null) {
                    this.setText(t);
                    mText = t;
                    break;
                }
            }
        } else {
            this.setText("");
        }
    }

    @Override
    public void setLeftDrawable(String left) {
        //左侧背景图形
        if (left != null) {
            TextDrawable drawable = new TextDrawable(mContext);
            drawable.setTextColor(getColorStateList());
            drawable.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            drawable.setText(left);
            mLDrawable = drawable;
        }
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(mLDrawable, null, mRDrawable, null);
    }

    @Override
    public void setLeftDrawable(Drawable left) {
        mLDrawable = left;
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(mLDrawable, null, mRDrawable, null);
    }

    @Override
    public void setRightDrawable(String right) {
        //右侧背景图形
        if (right != null) {
            TextDrawable drawable = new TextDrawable(mContext);
            drawable.setTextColor(getColorStateList());
            drawable.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            drawable.setText(right);
            mRDrawable = drawable;
        }
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(mLDrawable, null, mRDrawable, null);
    }

    @Override
    public void setRightDrawable(Drawable right) {
        mRDrawable = right;
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(mLDrawable, null, mRDrawable, null);
    }

    @Override
    public List<String> getTexts() {
        texts.clear();
        String text = getText().toString();
        if (!TextUtils.isEmpty(text)) {
            text = text.replace(mDefault, "").trim();
            texts.add(text);
        }
        return texts;
    }

    @Override
    public void setBackgroundView(int resId) {
        this.mBackgroundResId = resId;
    }

    @Override
    public void setEditInputType(int type) {
        this.setInputType(type);
    }

    @Override
    public void setOnItemEditListener(OnItemEditListener listener) {

    }

}
