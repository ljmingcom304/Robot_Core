package com.mmednet.library.http.okhttp;

import android.os.Message;

import com.mmednet.library.http.parse.HttpCallBack;
import com.mmednet.library.http.parse.HttpCode;

import java.io.Serializable;

import okhttp3.Response;

/**
 * Title:HttpMessage
 * <p>
 * Description:子线程向主线程传递消息
 * </p>
 * Author Jming.L
 * Date 2017/11/1 11:04
 */
public class HttpMessage<T> {

    private HttpCode httpCode;
    private Serializable serializable;
    private String message;
    private HttpCallBack<T> callBack;

    public HttpCode getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(HttpCode httpCode) {
        this.httpCode = httpCode;
    }

    public Serializable getSerializable() {
        return serializable;
    }

    public void setSerializable(Serializable serializable) {
        this.serializable = serializable;
    }

    public HttpCallBack<T> getCallBack() {
        return callBack;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCallBack(HttpCallBack<T> callBack) {
        this.callBack = callBack;
    }

    public Message build() {
        Message msg = new Message();
        msg.obj = this;
        return msg;
    }
}
