package com.mmednet.library.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Title:SignUtils
 * <p>
 * Description:签名
 * </p>
 * Author Jming.L
 * Date 2017/10/19 18:46
 */
public class SignUtils {

    public static String generateSign(Map<String, String> params)
            throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            temp.append(URLEncoder.encode(valueString, "UTF-8"));
        }
        return MD5Utils.encrypt(new String(temp)).toUpperCase();
    }
}