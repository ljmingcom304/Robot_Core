package com.mmednet.library.view.edit;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mmednet.library.R;

import java.util.List;

/**
 * Title:ICheckBox
 * <p>
 * Description:复选框
 * </p>
 * Author Jming.L
 * Date 2017/9/22 15:45
 */
public class ICheckBox extends IEditBox implements EditView {

    public ICheckBox(Context context) {
        this(context, null);
    }

    public ICheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void selectItem(TextView view) {
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
