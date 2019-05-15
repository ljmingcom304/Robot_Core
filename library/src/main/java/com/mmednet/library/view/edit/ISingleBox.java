package com.mmednet.library.view.edit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Title:ISingleBox
 * <p>
 * Description:单选框
 * </p>
 * Author Jming.L
 * Date 2017/11/10 15:36
 */
public class ISingleBox extends ISelectBox {

    public ISingleBox(Context context) {
        this(context,null);
    }

    public ISingleBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void selectItem(View view) {
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            if (textView == view) {
                view.setSelected(!view.isSelected());
            } else {
                textView.setSelected(false);
            }
        }
    }

    @Override
    public void setTexts(String... text) {
        boolean none = true;    //控制单选
        T:
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            String tView = textView.getText().toString();
            if (none && text != null) {
                for (String t : text) {
                    if (TextUtils.equals(t, tView)) {
                        textView.setSelected(true);
                        textView.setVisibility(View.VISIBLE);
                        none = false;
                        continue T;
                    }
                }
            }
            textView.setSelected(false);
        }
    }

    @Override
    public List<String> getTexts() {
        texts.clear();
        for (TextView textView : mList) {
            if (textView.isSelected()) {
                String str = textView.getText().toString();
                texts.add(str);
                break;
            }
        }
        return texts;
    }
}
