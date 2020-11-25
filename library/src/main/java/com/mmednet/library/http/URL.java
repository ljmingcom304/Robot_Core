package com.mmednet.library.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mmednet.library.Library;
import com.mmednet.library.util.ACache;

import java.util.HashMap;
import java.util.Map;

/**
 * Title:URL
 * <p>
 * Description:所有地址常量均继承此类
 * </p>
 * Author Jming.L
 * Date 2018/6/1 14:04
 */
public class URL {

    private static final String URL_HOST = URL.class.getName() + "HOST";
    private static final String URL_IMAGE = URL.class.getName() + "IMAGE";

    protected URL() {
    }

    private static SharedPreferences getShared() {
        Context context = Library.getInstance().getContext();
        return context.getSharedPreferences(URL.class.getName(), Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor() {
        return getShared().edit();
    }


    public static void setHost(String host) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(URL_HOST, host);
        editor.apply();
    }

    public static String getHost() {
        return getShared().getString(URL_HOST, null);
    }

    public static String getImage() {
        return getShared().getString(URL_IMAGE, null);
    }

    public static void setImage(String image) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(URL_IMAGE, image);
        editor.apply();
    }

    public static void setValue(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getValue(String key) {
        return getShared().getString(key, null);
    }

    public static String url(String path) {
        return getHost() + path;
    }

    public static String image(String path) {
        return getImage() + path;
    }

    public static String value(String key, String path) {
        return getValue(key) + (path == null ? "" : path);
    }

}
