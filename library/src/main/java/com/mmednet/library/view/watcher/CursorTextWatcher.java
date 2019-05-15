package com.mmednet.library.view.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Title:CursorTextWatcher
 * <p>
 * Description:输入完成后光标锁定输入位置
 * </p>
 * Author Jming.L
 * Date 2018/6/20 12:00
 */
public class CursorTextWatcher implements TextWatcher {

    private EditText editText;
    private int position;

    public CursorTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        position = start + count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.setSelection(position);
    }
}
