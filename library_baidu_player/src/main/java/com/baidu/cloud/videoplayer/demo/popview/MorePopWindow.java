package com.baidu.cloud.videoplayer.demo.popview;

import com.baidu.cloud.videoplayer.demo.PlayerListActivity;
import com.baidu.cloud.videoplayer.demo.PlayerMainActivity;
import com.baidu.cloud.videoplayer.demo.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class MorePopWindow extends PopupWindow {

    private View contentView;
    private PlayerMainActivity activity;
    private DisplayMetrics dm = new DisplayMetrics();

    public MorePopWindow(final PlayerMainActivity context, View ctView) {
        super(ctView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        contentView = ctView;
        activity = context;
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 设置SelectPicPopupWindow的View
//        this.setContentView(contentView);
//        // // 设置SelectPicPopupWindow弹出窗体的宽
//        this.setWidth(LayoutParams.WRAP_CONTENT);
//        // // 设置SelectPicPopupWindow弹出窗体的高
//        this.setHeight(LayoutParams.WRAP_CONTENT);
//        // 设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(android.R.style.Animation_Dialog);

        contentView.findViewById(R.id.ll_more_settings).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MorePopWindow.this.dismiss();
                // display another popup window
                SettingsPopWindow settingPop = SettingsPopWindow.createSettingsPopWindow(activity);
                settingPop.showPopupWindow();
            }
        });
        contentView.findViewById(R.id.ll_more_addnew).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MorePopWindow.this.dismiss();
                // display another popup window
                AddPopWindow addPop = AddPopWindow.createAddPopWindow(activity);
                addPop.showPopupWindow();
            }
        });
        contentView.findViewById(R.id.ll_more_cachelist).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MorePopWindow.this.dismiss();
                // jump to PlayerListActivity
                Intent intent = new Intent(activity, PlayerListActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    public static MorePopWindow createMorePopWindow(final PlayerMainActivity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ctView = inflater.inflate(R.layout.pop_window_more, null);
        MorePopWindow rtPopWindow = new MorePopWindow(context, ctView);
        return rtPopWindow;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 显示popupWindow
     * 
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, dm.widthPixels - (int) (dm.density * 165), 3);
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 0.3f;
            activity.getWindow().setAttributes(lp);
        } else {
            this.dismiss();
        }
    }
}
