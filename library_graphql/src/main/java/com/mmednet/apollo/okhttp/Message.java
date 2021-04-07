package com.mmednet.apollo.okhttp;

import java.io.Serializable;

/**
 * Title:Message
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2021/2/3 10:54
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 9173159938715375145L;

    private int statusCode;         //错误码
    private String error;           //错误
    private String message;         //错误描述

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
