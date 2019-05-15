package com.mmednet.test;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mmednet.library.layout.CustomActivity;
import com.mmednet.library.view.dialog.BaseDialog;
import com.mmednet.library.view.dialog.FragmentDialog;


import java.util.ArrayList;


public class VoiceActivity extends CustomActivity {

    //https://blog.csdn.net/maligebazi/article/details/80304894
    private Button mBtnVoice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

    }
}
