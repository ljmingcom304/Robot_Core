package com.mmednet.router.view.dialog;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmednet.library.view.dialog.FragmentDialog;
import com.mmednet.router.R;

/**
 * Title:NetworkDialog
 * <p>
 * Description:提交保存用的Dialog
 * </p>
 * Author Jming.L
 * Date 2018/2/7 17:35
 */
public class NetworkDialog extends FragmentDialog {

    public static final int LOADIND = 0;
    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;
    private int state = LOADIND;

    private RelativeLayout mRlLoading;
    private RelativeLayout mRlSuccess;
    private RelativeLayout mRlFailure;
    private Button mBtnReload;
    private TextView mTvLoading;
    private TextView mTvSuccess;
    private TextView mTvFailure;
    private String loading;
    private String success;
    private String failure;
    private String reload;

    private OnReloadClickListener listener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            state = LOADIND;
            if (isVisible())
                dismiss();
        }
    };

    //重载监听
    public interface OnReloadClickListener {
        void onReload();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        state = LOADIND;
    }

    @Override
    public void initView(View view) {
        mRlLoading = (RelativeLayout) view.findViewById(R.id.rl_loading);
        mRlSuccess = (RelativeLayout) view.findViewById(R.id.rl_success);
        mRlFailure = (RelativeLayout) view.findViewById(R.id.rl_failure);
        mBtnReload = (Button) view.findViewById(R.id.btn_reload);
        mTvLoading = (TextView) view.findViewById(R.id.tv_loading);
        mTvSuccess = (TextView) view.findViewById(R.id.tv_success);
        mTvFailure = (TextView) view.findViewById(R.id.tv_failure);
    }

    @Override
    public void initData() {
        setState(state);
        mBtnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onReload();
                }
            }
        });
        if (loading != null) {
            mTvLoading.setText(loading);
        }
        if (success != null) {
            mTvSuccess.setText(success);
        }
        if (failure != null) {
            mTvFailure.setText(failure);
        }
        if (reload != null) {
            mBtnReload.setText(reload);
        }
    }

    public void setLoadingText(String loading) {
        this.loading = loading;
        if (mTvLoading != null) {
            mTvLoading.setText(loading);
        }
    }

    public void setSuccessText(String success) {
        this.success = success;
        if (mTvSuccess != null) {
            mTvSuccess.setText(success);
        }
    }

    public void setFailureText(String failure) {
        this.failure = failure;
        if (mTvFailure != null) {
            mTvFailure.setText(failure);
        }
    }

    public void setReloadText(String reload) {
        this.reload = reload;
        if (mBtnReload != null) {
            mBtnReload.setText(reload);
        }
    }

    /**
     * 设置当前状态
     *
     * @param state 正在保存、保存成功、保存失败
     */
    public void setState(int state) {
        this.state = state;
        if (mRlLoading != null) {
            mRlLoading.setVisibility(state == LOADIND ? View.VISIBLE : View.GONE);
        }
        if (mRlSuccess != null) {
            mRlSuccess.setVisibility(state == SUCCESS ? View.VISIBLE : View.GONE);
        }
        if (mRlFailure != null) {
            mRlFailure.setVisibility(state == FAILURE ? View.VISIBLE : View.GONE);
        }
        if (state == SUCCESS) {
            mHandler.postDelayed(mRunnable, 1000);
        }

    }

    @Override
    public int setLayout() {
        return R.layout.router_dialog_network;
    }

    public void setOnReloadClickListener(OnReloadClickListener listener) {
        this.listener = listener;
    }

    public void setCancelable(boolean Cancelable) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(Cancelable);
        }
    }

}
