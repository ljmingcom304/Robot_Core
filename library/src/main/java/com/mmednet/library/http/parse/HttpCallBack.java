package com.mmednet.library.http.parse;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mmednet.library.Library;
import com.mmednet.library.http.okhttp.ProgressUIListener;
import com.mmednet.library.log.Logger;
import com.mmednet.library.util.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class HttpCallBack<T> extends ProgressUIListener {

    private String TAG = "HttpCallBack";

    public final void onSuccessResult(HttpCode httpCode, String message, Serializable result) {
        if (onPreExecute(httpCode, message, null)) {
            return;
        }
        this.onSuccess(result);
        this.onPostExecute();
    }

    /**
     * 处理网络请求失败
     */
    public final void onFailureResult(HttpCode httpCode, String message, String result) {
        if (onPreExecute(httpCode, message, result)) {
            return;
        }

        String description = message;
        if (TextUtils.isEmpty(message)) {
            description = httpCode.getDescription();
        }
        String logger = "[Code:" + httpCode.getCode() + "][Msg:" + description + "]";
        Log.e(TAG, logger);

        if (httpCode == HttpCode.NO_DATA) {
            this.onEmpty(description);
        } else {
            this.onFailure(description);
        }

        this.onPostExecute();
    }

    protected boolean onPreExecute(HttpCode httpcode) {
        return false;
    }

    protected boolean onPreExecute(HttpCode httpcode, String message) {
        return onPreExecute(httpcode);
    }

    protected boolean onPreExecute(HttpCode httpCode, String message, String result) {
        return onPreExecute(httpCode, message);
    }

    /**
     * 执行后处理
     */
    protected void onPostExecute() {

    }

    /**
     * 网络请求成功
     */
    protected abstract void onSuccess(Serializable s);

    /**
     * 网络请求失败
     */
    protected void onFailure(String message) {
        UIUtils.showToast(Library.getInstance().getContext(), message);
    }

    /**
     * 网络请求为空
     */
    protected void onEmpty(String message) {
        Logger.e(TAG, message);
    }

    @Override
    public void onStart(long totalBytes) {
        Logger.i(TAG, "============= onStart ===============");
    }

    @Override
    public void onChanged(long numBytes, long totalBytes, float percent, float speed) {
        Logger.i(TAG, "[NumBytes:" + numBytes + "][TotalBytes:" + totalBytes + "][Percent:" + percent + "][Speed:" + speed + "]");
    }

    @Override
    public void onFinish() {
        Logger.i(TAG, "============= onFinish ===============");
    }

}
