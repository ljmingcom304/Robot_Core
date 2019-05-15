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
    private static Map<Class<?>, URL> registryMap;
    private SharedPreferences mShared;

    private String host;
    private String image;
    private Map<String, String> stringMap;

    protected URL() {
        Library library = Library.getInstance();
        Context context = library.getContext();
        String name = getClass().getName();
        mShared = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        stringMap = new HashMap<>();
    }

    public static synchronized <T extends URL> T getInstance(Class<T> clazz) {
        if (registryMap == null) {
            registryMap = new HashMap<>();
        }
        if (!registryMap.containsKey(clazz)) {
            try {
                T instance = clazz.newInstance();
                registryMap.put(clazz, instance);
                return instance;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //noinspection unchecked
        return (T) registryMap.get(clazz);
    }

    public static URL getInstance() {
        return getInstance(URL.class);
    }


    public void setHost(String host) {
        this.host = host;
        SharedPreferences.Editor editor = mShared.edit();
        editor.putString(URL_HOST, host);
        editor.apply();
    }

    public String getHost() {
        if (TextUtils.isEmpty(host)) {
            host = mShared.getString(URL_HOST, null);
        }
        return host;
    }

    public String getImage() {
        if (TextUtils.isEmpty(image)) {
            host = mShared.getString(URL_IMAGE, null);
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        SharedPreferences.Editor editor = mShared.edit();
        editor.putString(URL_IMAGE, host);
        editor.apply();
    }

    public static String url(String path) {
        URL url = URL.getInstance();
        return url.getHost() + path;
    }

    public static String image(String path) {
        URL url = URL.getInstance();
        return url.getImage() + path;
    }

    public void setValue(String key, String value) {
        stringMap.put(key, value);
        SharedPreferences.Editor editor = mShared.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getValue(String key) {
        String value = stringMap.get(key);
        if (value == null) {
            value = mShared.getString(key, null);
        }
        return value;
    }

    public static String value(String key, String path) {
        URL url = URL.getInstance();
        return url.getValue(key) + (path == null ? "" : path);
    }

}
