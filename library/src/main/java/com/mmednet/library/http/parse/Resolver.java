package com.mmednet.library.http.parse;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mmednet.library.http.json.JsonBuilder;
import com.mmednet.library.http.okhttp.HttpHandler;
import com.mmednet.library.http.okhttp.HttpMessage;
import com.mmednet.library.util.JsonUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

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
        sendMessage(callBack, HttpCode.ERROR_NET, null, result);
    }

    public <T> void handleSuccess(String result, Class<T> clazz, HttpCallBack<T> callBack) {
        if (callBack == null) {
            return;
        }

        //泛型为String.class或空时不进行解析直接返回
        if (clazz == null || clazz == String.class) {
            sendMessage(callBack, HttpCode.SUCCESS, result, null);
        } else {
            if (TextUtils.isEmpty(result)) {// 网络请求没数据返回
                sendMessage(callBack, HttpCode.NO_DATA, null, null);
                return;
            }

            //响应数据
            String data = null;
            //响应状态码
            int statusCode = HttpCode.SUCCESS.getCode();
            //响应消息
            String msg = null;

            // Json数据格式：{data:{},status:0,msg:""}
            JsonObject jsonObject = null;
            try {
                JsonElement parse = jsonParser.parse(result);
                jsonObject = parse.getAsJsonObject();
            } catch (Exception e) {
                Log.e(TAG, "JSON解析异常：" + e.getMessage());
                statusCode = HttpCode.ERROR_UNKNOWN.getCode();
            }

            if (jsonObject != null) {
                //解析状态码
                try {
                    JsonElement sElement = jsonObject.get("status");
                    if (sElement != null && !sElement.isJsonNull()) {
                        statusCode = sElement.getAsInt();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "status解析异常：" + e.getMessage());
                    statusCode = HttpCode.ERROR_UNKNOWN.getCode();
                }


                try {
                    JsonElement dElement = jsonObject.get("data");
                    if (dElement != null && !dElement.isJsonNull()) {
                        if (dElement.isJsonArray() || dElement.isJsonObject()) {
                            data = dElement.toString();
                        } else {
                            //data接字符串时
                            data = dElement.getAsString();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "data解析异常：" + e.getMessage());
                }


                try {
                    JsonElement mElement = jsonObject.get("msg");
                    if (mElement != null && !mElement.isJsonNull()) {
                        msg = mElement.getAsString();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "msg解析异常：" + e.getMessage());
                }

                //解析apiCondition，如果apiMsg中存在数据则使用apiMsg中数据
                try {
                    JsonElement aElement = jsonObject.get("apiCondition");
                    if (aElement != null && !aElement.isJsonNull()) {
                        JsonObject aJsonObject = aElement.getAsJsonObject();
                        JsonElement element = aJsonObject.get("apiMsg");
                        if (element != null && element.isJsonNull()) {
                            msg = element.getAsString();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "apiCondition解析异常：" + e.getMessage());
                }
            }


            if (HttpCode.SUCCESS.getCode() == statusCode) {

                // 判断返回的结果数据是否为空
                if (!TextUtils.isEmpty(data)) {
                    Serializable serializable = null;
                    try {
                        serializable = parseData(data, clazz);
                    } catch (Exception e) {
                        Log.e(TAG, "BEAN解析异常：" + e.getMessage());
                        e.printStackTrace();
                    }

                    if (serializable != null) {
                        sendMessage(callBack, HttpCode.SUCCESS, serializable, msg);
                    } else {
                        sendMessage(callBack, HttpCode.ERROR_DATA, null, msg);
                    }
                } else {
                    //没有数据返回时也走成功状态，当后台返回15时才走没有数据状态
                    sendMessage(callBack, HttpCode.SUCCESS, null, msg);
                }
            } else {
                for (HttpCode httpCode : HttpCode.values()) {
                    int code = httpCode.getCode();
                    if (code == statusCode) {
                        sendMessage(callBack, httpCode, data, msg);
                        return;
                    }
                }
                HttpCode.ERROR_UNKNOWN.setCode(statusCode);
                sendMessage(callBack, HttpCode.ERROR_UNKNOWN, data, msg);
            }
        }
    }

    /**
     * 发送消息到主线程
     */
    private <T> void sendMessage(HttpCallBack<T> callBack, HttpCode httpCode, Serializable result, String msg) {
        HttpMessage<T> message = new HttpMessage<>();
        message.setCallBack(callBack);
        message.setHttpCode(httpCode);
        message.setSerializable(result);
        message.setMessage(msg);
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
