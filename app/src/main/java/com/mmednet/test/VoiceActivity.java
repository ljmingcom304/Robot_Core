package com.mmednet.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.mmednet.library.layout.CustomActivity;
import com.mmednet.library.view.EditLayout;
import com.mmednet.library.view.dialog.BaseDialog;


public class VoiceActivity extends CustomActivity {

    //https://blog.csdn.net/maligebazi/article/details/80304894
    private Button mBtnVoice;
    private EditLayout mElSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        mBtnVoice = (Button) findViewById(R.id.btn_voice);
        mBtnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDialog fragmentDialog = new ABCDialog(VoiceActivity.this);
                fragmentDialog.show();
            }
        });
        mElSpinner = (EditLayout) findViewById(R.id.el_spinner);
        Spinner spinner = (Spinner) mElSpinner.getEditView();
        Log.e("TAG", spinner.getMeasuredHeight()+"========================" + spinner.getDropDownVerticalOffset());
    }
}
