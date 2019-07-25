package com.mmednet.library.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Title:SimpleTextWatcher
 * <p>
 * Description:文本变化监听
 * </p>
 * Author Jming.L
 * Date 2019/7/18 13:39
 */
public class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
