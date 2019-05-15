package com.mmednet.library.view.loader;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.mmednet.library.R;
import com.mmednet.library.log.Logger;

import java.util.concurrent.Executors;


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
public abstract class Loader extends FrameLayout {

    private static final String TAG = Loader.class.getSimpleName();
    private Context mContext;
    private View mLoadingView;// 加载的view
    private View mErrorView;// 错误的view
    private View mEmptyView;// 空的view
    private View mSucceedView;// 成功的view
    private LoadState mLoadState;// 加载结果
    private OnReloadListener mRListener;
    private OnStateListener mStateListener;
    private int mMaxCount = 4;
    private int mState;// 默认的状态

    public interface OnReloadListener {
        /**
         * 重新加载网络数据
         */
        void onReload();
    }

    public interface OnStateListener {
        /**
         * 状态切换，当View为空时不会切换到该状态
         */
        void onState(LoadState loadState);
    }

    public Loader(Context context) {
        this(context, null);
    }

    public Loader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Loader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initState(attrs, defStyle);
        initView();
    }

    private void initState(AttributeSet attrs, int defStyle) {
        Resources.Theme theme = mContext.getTheme();
        TypedArray array = theme.obtainStyledAttributes(attrs, R.styleable.Loader, defStyle, 0);
        if (array != null) {
            mState = array.getInt(R.styleable.Loader_state, LoadState.UNLOADED.value());
        } else {
            mState = LoadState.UNLOADED.value();
        }
        for (LoadState loadState : LoadState.values()) {
            if (mState == loadState.value()) {
                this.mLoadState = loadState;
                break;
            }
        }
    }

    private void initView() {
        mSucceedView = createSuccessView();
        if (null != mSucceedView) {
            if (getChildCount() == 0) {
                this.addView(mSucceedView, new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
            }
        }

        mLoadingView = createLoadingView();
        if (null != mLoadingView) {
            this.addView(mLoadingView, new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        } else {
            mMaxCount--;
        }

        mErrorView = createErrorView();
        if (null != mErrorView) {
            this.addView(mErrorView, new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        } else {
            mMaxCount--;
        }

        mEmptyView = createEmptyView();
        if (null != mEmptyView) {
            this.addView(mEmptyView, new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        } else {
            mMaxCount--;
        }

        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                showSafePagerView();
            }
        });
    }

    // 主线程中显示界面
    private void showSafePagerView() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            showPagerView();
        } else {
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0)
                        showPagerView();
                }
            };
            handler.sendEmptyMessage(0);
        }
    }

    private void showPagerView() {
        if (null != mLoadingView) {
            mLoadingView.setVisibility((mState == LoadState.UNLOADED.value()
                    || mState == LoadState.LOADING.value())
                    ? View.VISIBLE : View.GONE);
        } else {
            Logger.w(TAG, "LoadingView is null!");
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(mState == LoadState.ERROR.value()
                    ? View.VISIBLE : View.GONE);
            if (mStateListener != null) {
                mStateListener.onState(LoadState.ERROR);
            }
        } else {
            Logger.w(TAG, "ErrorView is null!");
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(mState == LoadState.EMPTY.value()
                    ? View.VISIBLE : View.GONE);
            if (mStateListener != null) {
                mStateListener.onState(LoadState.EMPTY);
            }
        } else {
            Logger.w(TAG, "EmptyView is null!");
        }

        if (null == mSucceedView) {
            mSucceedView = createSuccessView();
        }

        if (null != mSucceedView) {
            mSucceedView.setVisibility(mState == LoadState.SUCCESS.value()
                    ? View.VISIBLE : View.GONE);
            if (mStateListener != null) {
                mStateListener.onState(LoadState.SUCCESS);
            }
        } else {
            Logger.w(TAG, "SuccessView is null!");
        }

        if (mStateListener != null) {
            if (mLoadState == LoadState.UNLOADED) {
                mLoadState = LoadState.LOADING;
            }
            mStateListener.onState(mLoadState);
        }
    }

    /**
     * 加载成功界面
     */
    protected View createSuccessView() {
        int childCount = getChildCount();
        if (childCount > mMaxCount) {
            throw new RuntimeException(getClass().getSimpleName() + " can host only one direct child");
        }
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view != getLoadingView() && view != getEmptyView() && view != getErrorView()) {
                return view;
            }
        }
        return null;
    }

    /**
     * 正在加载界面
     */
    protected abstract View createLoadingView();


    /**
     * 没有数据界面
     */
    protected abstract View createEmptyView();

    /**
     * 网络异常界面
     */
    protected abstract View createErrorView();

    /**
     * 重载页面
     */
    public final void reload() {
        if (mRListener != null) {
            mState = LoadState.UNLOADED.value();
            showSafePagerView();
            mRListener.onReload();
        } else {
            Log.e(getClass().getSimpleName(), "OnReloadListener is null.");
        }
    }

    /**
     * 加载状态切换时触发
     */
    public final void setOnStateListener(OnStateListener listener) {
        this.mStateListener = listener;
    }

    /**
     * 网络请求失败，重新加载时触发
     */
    public final void setOnReloadListener(OnReloadListener listener) {
        this.mRListener = listener;
    }

    /**
     * 设置加载结果
     */
    public final Loader setLoadState(LoadState loadState) {
        this.mLoadState = loadState;

        if (mState == LoadState.ERROR.value()
                || mState == LoadState.EMPTY.value()
                || mState == LoadState.LOADING.value()) {
            mState = LoadState.UNLOADED.value();
        }

        if (mState == LoadState.UNLOADED.value()) {
            mState = LoadState.LOADING.value();
            TaskRunnable task = new TaskRunnable();
            int count = Runtime.getRuntime().availableProcessors();
            Executors.newFixedThreadPool(count).execute(task);
        } else {
            mState = mLoadState.value();
            showSafePagerView();
        }

        return this;
    }

    private class TaskRunnable implements Runnable {
        @Override
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        mState = mLoadState.value();
                        showPagerView();
                    }
                }
            };
            handler.sendEmptyMessage(1);
        }

    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

}
