package com.mmednet.library.http.okhttp;

import android.util.Log;

import com.mmednet.library.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Title:OkCookie
 * <p>
 * Description:Cookie
 * </p>
 * Author Jming.L
 * Date 2018/4/28 15:14
 */
public class OkCookie implements CookieJar {

    private Map<String, List<Cookie>> cookieMap = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieMap.put(url.host(), cookies);
    }

    //不能返回null，否则会报NULLException
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieMap.get(url.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }
}
