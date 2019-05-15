package com.mmednet.library.view.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


public class ITextWatcher implements TextWatcher {

    private EditText editText;

    public ITextWatcher(EditText editText){
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.setSelection(s.length());
    }
}
