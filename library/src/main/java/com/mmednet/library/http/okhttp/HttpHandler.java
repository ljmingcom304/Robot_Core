package com.mmednet.library.http.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mmednet.library.http.parse.HttpCallBack;
import com.mmednet.library.http.parse.HttpCode;
import com.mmednet.library.http.parse.Resolver;

import java.io.Serializable;

/**
 * Title:HttpHandler
 * <p>
 * Description:主线程中处理消息
 * </p>
 * Author Jming.L
 * Date 2017/11/1 11:13
 */
public class HttpHandler extends Handler {

    public HttpHandler() {
        super(Looper.getMainLooper());
    }

    @Override
    public void handleMessage(Message msg) {
        HttpMessage httpMessage = (HttpMessage) msg.obj;
        HttpCallBack callBack = httpMessage.getCallBack();
        HttpCode httpCode = httpMessage.getHttpCode();
        Serializable serializable = httpMessage.getSerializable();
        String message = httpMessage.getMessage();
        if (httpCode == HttpCode.SUCCESS) {
            callBack.onSuccessResult(httpCode, message, serializable);
        } else {
            callBack.onFailureResult(httpCode, message, (String) serializable);
        }
    }

}


