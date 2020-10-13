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

    private Button mBtnVoice;
    private EditLayout mElSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        mElSpinner = (EditLayout) findViewById(R.id.el_spinner);
    }
}
