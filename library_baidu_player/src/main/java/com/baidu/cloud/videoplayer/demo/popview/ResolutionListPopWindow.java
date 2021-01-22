package com.baidu.cloud.videoplayer.demo.popview;

import com.baidu.cloud.videoplayer.demo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

public class ResolutionListPopWindow extends PopupWindow {
    private View contentView;
    private DisplayMetrics dm = new DisplayMetrics();

    private ResolutionListPopWindow(final Activity context, View ctView,
                                   String[] resolution, final OnItemClickListener listener) {
        super(ctView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        contentView = ctView;
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 设置SelectPicPopupWindow的View
//        this.setContentView(contentView);
        // // 设置SelectPicPopupWindow弹出窗体的宽
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

        ListView mResolutionSelector = (ListView) contentView.findViewById(R.id.resolution_selector);

        mResolutionSelector
                .setAdapter(new ArrayAdapter<String>(context, R.layout.item_of_list_resolution_item, resolution));
        mResolutionSelector.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(parent, view, position, id);
                dismiss();
            }
        });
    }

    public static ResolutionListPopWindow createResolutionListPopWindow(final Activity context,
                                                                        String[] resolution,
                                                                        final OnItemClickListener listener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ctView = inflater.inflate(R.layout.pop_window_resolutionlist, null);
        ResolutionListPopWindow rtPopWindow = new ResolutionListPopWindow(context, ctView, resolution, listener);
        return rtPopWindow;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 显示popupWindow
     * 
     * @param parent
     */
    public void showPopupWindow(View view) {
        if (!this.isShowing()) {
            // dm.widthPixels - (int)(dm.density * 165)
            // 以下拉方式显示popupwindow
            this.showAtLocation(view, Gravity.BOTTOM, (int) (dm.widthPixels * 2 / 7), (int) (dm.density * 54));
        } else {
            this.dismiss();
        }
    }
}
