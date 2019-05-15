package com.mmednet.test;

import android.content.Context;
import android.view.View;

import com.mmednet.library.view.dialog.BaseDialog;
import com.mmednet.library.view.dialog.FragmentDialog;


public class ABCDialog extends BaseDialog {
    public ABCDialog(Context context) {
        super(context);
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public int setLayout() {
        return R.layout.activity_voice;
    }
}
