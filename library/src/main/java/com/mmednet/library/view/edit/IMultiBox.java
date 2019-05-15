package com.mmednet.library.view.edit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Title:IMultiBox
 * <p>
 * Description:多选框
 * </p>
 * Author Jming.L
 * Date 2017/11/10 17:17
 */
public class IMultiBox extends ISelectBox {


    public IMultiBox(Context context) {
        this(context, null);
    }

    public IMultiBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void selectItem(View view) {
        view.setSelected(!view.isSelected());
    }

    @Override
    public void setTexts(String... text) {
        T:
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            String tView = textView.getText().toString();
            if (text != null) {
                for (String t : text) {
                    if (TextUtils.equals(t, tView)) {
                        textView.setSelected(true);
                        textView.setVisibility(View.VISIBLE);
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
            }
        }
        return texts;
    }
}
