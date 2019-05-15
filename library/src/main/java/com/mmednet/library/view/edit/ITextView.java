package com.mmednet.library.view.edit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Title:ITextView
 * <p>
 * Description:文本框
 * </p>
 * Author Jming.L
 * Date 2017/9/25 10:23
 */
public class ITextView extends IEditText {

    private OnItemEditListener mListener;

    public ITextView(Context context) {
        this(context, null);
    }

    public ITextView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(ITextView.this.getText().toString(), true);
                }
            }
        });
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
    }

    @Override
    public void setOnItemEditListener(OnItemEditListener listener) {
        this.mListener = listener;
    }
}
