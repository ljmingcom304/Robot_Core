package com.mmednet.library.view.loader;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mmednet.library.R;
import com.mmednet.library.util.UIUtils;


/**
 * <p>
 * Title:LoadPage
 * </p>
 * <p>
 * Description:网络请求加载页,在请求完毕后通过{@link #setLoadState}设置请求结果
 * </p>
 *
 * @author Jming.L
 * @date 2016年3月18日 下午3:52:01
 */
public class LoaderLayout extends Loader {

    private TextView mEmptyTitle;//空数据标题
    private TextView mEmptyDes;//空数据描述
    private TextView mErrorTitle;//错误数据标题
    private TextView mErrorDes;//错误数据描述

    public LoaderLayout(Context context) {
        super(context);
    }

    public LoaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 正在加载界面
     */
    protected View createLoadingView() {
        int padding = UIUtils.getDimens(getContext(), R.dimen.core_size_30);
        LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        LinearLayout lLayout = new LinearLayout(getContext());
        lLayout.setBackgroundColor(Color.WHITE);
        lLayout.setGravity(Gravity.CENTER);
        lLayout.setLayoutParams(lParams);
        lLayout.setPadding(padding, padding, padding, padding);
        lLayout.setOrientation(LinearLayout.VERTICAL);

        int size = UIUtils.getDimens(getContext(), R.dimen.core_size_8);
        ProgressBar pb = new ProgressBar(getContext());
        pb.setPadding(size, size, size, size);
        lLayout.addView(pb);

        TextView tv = new TextView(getContext());
        tv.setText("正在加载，请稍后...");
        tv.setPadding(size, size, size, size);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(Color.parseColor("#666666"));
        lLayout.addView(tv);

        return lLayout;
    }

    /**
     * 失败数据界面的内容
     */
    public void setErrorContent(String title, String description) {
        if (!TextUtils.isEmpty(title)) {
            mErrorTitle.setText(title);
        }

        if (!TextUtils.isEmpty(description)) {
            mErrorDes.setText(description);
        }
    }

    /**
     * 没有数据界面的内容
     */
    public void setEmptyContent(String title, String description) {
        if (!TextUtils.isEmpty(title)) {
            mEmptyTitle.setText(title);
        }

        if (!TextUtils.isEmpty(description)) {
            mEmptyDes.setText(description);
        }
    }

    /**
     * 没有数据界面
     */
    protected View createEmptyView() {
        int padding = UIUtils.getDimens(getContext(), R.dimen.core_size_30);
        LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        LinearLayout lLayout = new LinearLayout(getContext());
        lLayout.setBackgroundColor(Color.WHITE);
        lLayout.setGravity(Gravity.CENTER);
        lLayout.setOrientation(LinearLayout.VERTICAL);
        lLayout.setLayoutParams(lParams);
        lLayout.setPadding(padding, padding, padding, padding);

        int size = UIUtils.getDimens(getContext(), R.dimen.core_size_8);
        //图标
        ImageView icon = new ImageView(getContext());
        icon.setPadding(size, size, size, size);
        //icon.setImageResource(R.drawable.icon_loader_empty);

        //标题
        mEmptyTitle = new TextView(getContext());
        mEmptyTitle.setGravity(Gravity.CENTER);
        mEmptyTitle.setPadding(size, size, size, size);
        mEmptyTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mEmptyTitle.setText("返回数据为空");
        mEmptyTitle.setTextColor(Color.parseColor("#666666"));

        //描述
        mEmptyDes = new TextView(getContext());
        mEmptyDes.setGravity(Gravity.CENTER);
        mEmptyDes.setText("网络请求成功，没有数据记录");
        mEmptyDes.setLineSpacing(dip2px(getContext(), 5), 1f);
        mEmptyDes.setPadding(size, size, size, size);
        mEmptyDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mEmptyDes.setTextColor(Color.parseColor("#999999"));

        lLayout.addView(icon);
        lLayout.addView(mEmptyTitle);
        lLayout.addView(mEmptyDes);
        return lLayout;
    }

    /**
     * 网络异常界面
     */
    protected View createErrorView() {
        int padding = UIUtils.getDimens(getContext(), R.dimen.core_size_30);
        LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        LinearLayout lLayout = new LinearLayout(getContext());
        lLayout.setBackgroundColor(Color.WHITE);
        lLayout.setGravity(Gravity.CENTER);
        lLayout.setOrientation(LinearLayout.VERTICAL);
        lLayout.setLayoutParams(lParams);
        lLayout.setPadding(padding, padding, padding, padding);

        int size = UIUtils.getDimens(getContext(), R.dimen.core_size_8);
        //图标
        ImageView icon = new ImageView(getContext());
        icon.setPadding(size, size, size, size);
        // icon.setImageResource(R.drawable.icon_loader_error);

        //标题
        mErrorTitle = new TextView(getContext());
        mErrorTitle.setGravity(Gravity.CENTER);
        mErrorTitle.setPadding(size, size, size, size);
        mErrorTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mErrorTitle.setText("网络不给力");
        mErrorTitle.setTextColor(Color.parseColor("#666666"));

        //描述
        mErrorDes = new TextView(getContext());
        mErrorDes.setGravity(Gravity.CENTER);
        mErrorDes.setText("网络连接失败，请您点击重试");
        mErrorDes.setPadding(size, size, size, size);
        mErrorDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mErrorDes.setTextColor(Color.parseColor("#999999"));

        //重试
        lLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        lLayout.addView(icon);
        lLayout.addView(mErrorTitle);
        lLayout.addView(mErrorDes);

        return lLayout;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
