package com.mmednet.library.http.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class JsonBuilder {
    /**
     * 创建Gson对象，将NULL格式化为""字符串
     *
     * @param build 是否进行转化
     * @return Gson
     */
    public static Gson build(boolean build) {
        if (build) {
            GsonBuilder builder = new GsonBuilder();
            NullStringToEmptyAdapterFactory adapterFactory = new NullStringToEmptyAdapterFactory();
            GsonBuilder gsonBuilder = builder.registerTypeAdapterFactory(adapterFactory);
            return gsonBuilder.create();
        }
        return new Gson();
    }

}
