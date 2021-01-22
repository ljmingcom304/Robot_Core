package com.baidu.cloud.videoplayer.demo.popview;

import com.baidu.cloud.videoplayer.demo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomAlertWindow {
    // public static final int WHITE_THEME = ;
    // public static final int BLACK_THEME = ;
    public static void showAlertWindow(Activity activity, String msgString, String yesString, String noString,
            final View.OnClickListener yesListener, final View.OnClickListener noListener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.show();
        Window win = alertDialog.getWindow();
        win.setContentView(R.layout.alert_window_black_theme);
        TextView tvMsg = (TextView) win.findViewById(R.id.tvMsg);
        tvMsg.setText(msgString);
        Button btOk = (Button) win.findViewById(R.id.btOk);
        Button btCancel = (Button) win.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (noListener != null) {
                    noListener.onClick(view);
                }
                alertDialog.dismiss();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (yesListener != null) {
                    yesListener.onClick(view);
                }
                alertDialog.dismiss();
            }
        });
    }
}
