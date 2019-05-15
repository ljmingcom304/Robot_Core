package com.mmednet.library.view.watcher;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title:LimitTextWatcher
 * <p>
 * Description:限制编辑框的输入位数
 * </p>
 * Author Jming.L
 * Date 2018/6/20 12:01
 */
public class LimitTextWatcher implements TextWatcher {

    private EditText editText;
    private int limit;
    private float minValue;
    private float maxValue;
    private int position;
    private int start;
    private String temp;

    public LimitTextWatcher(EditText editText, int limit, float minValue, float maxValue) {
        this.editText = editText;
        this.limit = limit;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.temp = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.start = start;
        this.position = start + count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editText.isEnabled()) {
            String text = s.toString();
            if (TextUtils.equals(temp, text)) {
                return;
            }
            if (!TextUtils.isEmpty(text)) {
                text = text.trim();
                if (limit >= 0) {//位数有小数做限制输入，无效时不做限制输入
                    Pattern pattern = Pattern.compile("^[0-9]+.?[0-9]*$");//是否小数或整数
                    Matcher macher = pattern.matcher(text);
                    if (macher.find()) {//是否小数或整数
                        float value = Float.parseFloat(text);
                        if (value >= minValue && value <= maxValue) {
                            int num = text.contains(".") ? (text.length() - text.lastIndexOf(".") - 1) : 0;
                            if (num > limit) {//超过位数时返回
                                editText.setText(text.substring(0, text.indexOf(".") + limit + 1));
                            } else {
                                editText.setSelection(position);
                            }
                        } else {
                            editText.removeTextChangedListener(this);
                            editText.setText(temp);
                            editText.setSelection(start);
                            editText.addTextChangedListener(this);
                        }
                    } else {
                        editText.removeTextChangedListener(this);
                        editText.setText(temp);
                        editText.setSelection(start);
                        editText.addTextChangedListener(this);
                    }
                }
            }
        }
    }

}
