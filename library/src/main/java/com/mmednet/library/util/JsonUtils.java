package com.mmednet.library.util;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    public static final int OBJECT = 1;
    public static final int ERROR = 0;
    public static final int ARRAY = 2;

    /**
     * 获取Json字符串的类型
     */
    public static int getJsonType(String json) {
        if (TextUtils.isEmpty(json)) {
            return ERROR;
        }

        final char[] strChar = json.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

        if (firstChar == '{') {
            return OBJECT;
        } else if (firstChar == '[') {
            return ARRAY;
        } else {
            return ERROR;
        }
    }

    /**
     * JSON数据是否为空
     */
    public static boolean isJsonEmpty(JsonElement element) {
        if (element == null) {
            return true;
        }
        if (element.isJsonNull()) {
            return true;
        }
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            if (jsonObject == null) {
                return true;
            }
            Set<String> set = jsonObject.keySet();
            if (set == null || set.size() == 0) {
                return true;
            }
        }
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            if (jsonArray == null || jsonArray.size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤double
     */
    @Deprecated
    public static String filterJson(String json, double value) {
        json = json.replaceAll("(,\"\\w+\":" + value + ")|(\"\\w+\":" + value + ",)", "");
        return json;
    }

    /**
     * 过滤float
     */
    @Deprecated
    public static String filterJson(String json, float value) {
        json = json.replaceAll("(,\"\\w+\":" + value + ")|(\"\\w+\":" + value + ",)", "");
        return json;
    }

    /**
     * 过滤整数
     */
    @Deprecated
    public static String filterJson(String json, int value) {
        json = json.replaceAll("(,\"\\w+\":" + value + ")|(\"\\w+\":" + value + ",)", "");
        return json;
    }

    /**
     * 过滤字符串
     */
    @Deprecated
    public static String filterJson(String json, String value) {
        json = json.replaceAll("(,\"\\w+\":\"" + value + "\")|(\"\\w+\":\"" + value + "\",)", "");
        return json;
    }

    /**
     * 过滤""、null、[]
     */
    @Deprecated
    public static String filterEmpty(String json) {
        String reg = "(,{0,1}\"\\w+\":\"\\s*\")|(\"\\w+\":\"\\s*\",)"//校验：字符:""
                + "|(,{0,1}\"\\w+\":null)|(\"\\w+\":null,)"//校验：字符:null
                + "|(\"\\w+\":\\[\\s*\\],)|(\"\\w+\":\\{\\s*\\},)"//校验：字符:[],、字符:{},
                + "|(,{0,1}\"\\w+\":\\[\\s*\\])|(,{0,1}\"\\w+\":\\{\\s*\\})"//校验：字符:[]、字符:{}、,字符:[]、,字符:{}
                + "|(,\\[\\s*\\])|(\\[\\s*\\],{0,1})"//校验：[]、[],
                + "|(,\\{\\s*\\})|(\\{\\s*\\},{0,1})";//校验：{}、{},
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(json);
        while (m.find()) {
            json = json.replaceAll(reg, "");
        }
        return json;
    }

}
