package com.mmednet.library.http.parse;

import java.util.HashMap;

/**
 * Title:HttpHeader
 * <p>
 * Description:请求头
 * </p>
 * Author Jming.L
 * Date 2018/11/27 16:48
 */
public class HttpHeader extends HashMap<String, String> {

    private static final long serialVersionUID = -9054376924008468815L;

    public static final String KEY = "x-meridian-cdtype";

    static {
        System.loadLibrary("result");
    }

    public native static String format(String res, String method);

    public HttpHeader() {
        this.put(KEY, "123");
    }

}
