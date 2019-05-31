package com.mmednet.library.http.parse;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mmednet.library.http.json.JsonBuilder;
import com.mmednet.library.http.okhttp.HttpHandler;
import com.mmednet.library.http.okhttp.HttpMessage;
import com.mmednet.library.util.JsonUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Title:Resolver
 * <p>
 * Description:协议解析
 * </p>
 * Author Jming.L
 * Date 2017/11/1 16:35
 */
public class Resolver {

    private Gson gson = JsonBuilder.build(false);
    private JsonParser jsonParser = new JsonParser();
    private HttpHandler httpHandler = new HttpHandler();
    private static final String TAG = "Resolver";

    public <T> void handleFailure(String result, HttpCallBack<T> callBack) {
        if (callBack == null) {
            return;
        }
        HttpResult httpResult = new HttpResult();
        httpResult.setHttpCode(HttpCode.ERROR_NET);
        httpResult.setMsg(result);
        sendMessage(callBack, null, httpResult);
    }

    public <T> void handleSuccess(String result, Class<T> clazz, HttpCallBack<T> callBack) {
        if (callBack == null) {
            return;
        }

        //泛型为String.class或空时不进行解析直接返回
        if (clazz == null || clazz == String.class) {
            HttpResult httpResult = new HttpResult();
            httpResult.setHttpCode(HttpCode.SUCCESS);
            httpResult.setResult(result);
            sendMessage(callBack, result, httpResult);
        } else {
            if (TextUtils.isEmpty(result)) {// 网络请求没数据返回
                HttpResult httpResult = new HttpResult();
                httpResult.setHttpCode(HttpCode.NO_DATA);
                sendMessage(callBack, null, new HttpResult());
                return;
            }

            HttpResult httpResult = new HttpResult();
            try {
                httpResult = gson.fromJson(result, HttpResult.class);
            } catch (Exception e) {
                Log.e(TAG, "HttpResult解析异常：" + e.getMessage());
            }

            // Json数据格式：{data:{},status:0,msg:""}
            try {
                JsonElement parse = jsonParser.parse(result);
                JsonObject jsonObject = parse.getAsJsonObject();
                JsonElement dElement = jsonObject.get("data");
                if (dElement != null && !JsonUtils.isJsonEmpty(dElement)) {
                    httpResult.setResult(dElement.toString());
                }
            } catch (Exception e) {
                Log.e(TAG, "DATA解析异常：" + e.getMessage());
            }

            String data = httpResult.getResult();
            int status = httpResult.getStatus();
            if (HttpCode.SUCCESS.getCode() == status) {

                // 判断返回的结果数据是否为空
                if (!TextUtils.isEmpty(data)) {
                    Serializable serializable = null;
                    try {
                        serializable = parseData(data, clazz);
                    } catch (Exception e) {
                        Log.e(TAG, "BEAN解析异常：" + e.getMessage());
                        e.printStackTrace();
                    }
                    httpResult.setHttpCode(serializable == null ? HttpCode.ERROR_DATA : HttpCode.SUCCESS);
                    sendMessage(callBack, serializable, httpResult);
                } else {
                    //没有数据返回时也走成功状态，当后台返回15时才走没有数据状态
                    httpResult.setHttpCode(HttpCode.SUCCESS);
                    sendMessage(callBack, null, httpResult);
                }
            } else {
                for (HttpCode httpCode : HttpCode.values()) {
                    int code = httpCode.getCode();
                    if (code == status) {
                        httpResult.setHttpCode(httpCode);
                        sendMessage(callBack, data, httpResult);
                        return;
                    }
                }
                HttpCode.ERROR_UNKNOWN.setCode(status);
                httpResult.setHttpCode(HttpCode.ERROR_UNKNOWN);
                sendMessage(callBack, data, httpResult);
            }
        }
    }

    /**
     * 发送消息到主线程
     */
    private <T> void sendMessage(HttpCallBack<T> callBack, Serializable result, @NonNull HttpResult httpResult) {
        HttpMessage<T> message = new HttpMessage<>();
        message.setCallBack(callBack);
        message.setSerializable(result);
        message.setHttpResult(httpResult);
        httpHandler.sendMessage(message.build());
    }

    /**
     * 处理返回的结果数据
     */
    private <T> Serializable parseData(String data, Class<T> clazz) {
        //泛型如果为Serializable则不进行解析直接返回
        if (clazz == Serializable.class) {
            return data;
        }

        if (!TextUtils.isEmpty(data)) {
            // 过滤""、null、[]、{}
            // data = JsonUtils.filterEmpty(data);

            int jsonType = JsonUtils.getJsonType(data);
            // 判断Json的类型，并根据类型转换成对象或者集合

            if (JsonUtils.OBJECT == jsonType) {
                Type type = TypeToken.get(clazz).getType();
                T t = gson.fromJson(data, type);
                return (Serializable) t;
            }
            if (JsonUtils.ARRAY == jsonType) {
                ArrayList<T> list = fromJsonList(data, clazz);
                if (list != null) {
                    return list;
                } else {
                    return new ArrayList<>();
                }
            }
        }

        return data;
    }

    //解决com.google.gson.internal.LinkedTreeMap cannot be cast to Object的异常
    private <T> ArrayList<T> fromJsonList(String json, Class<T> clazz) {
        ArrayList<T> mList = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        Type type = TypeToken.get(clazz).getType();
        for (final JsonElement elem : array) {
            T t = gson.fromJson(elem, type);
            mList.add(t);
        }
        return mList;
    }

}
