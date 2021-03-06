package com.mmednet.library.http.parse;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mmednet.library.Library;
import com.mmednet.library.http.Network;
import com.mmednet.library.http.okhttp.OkHttp;
import com.mmednet.library.util.SignUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private static final String TAG = "Client_TAG";
    private static final String HEADER = "Client_Header";
    private static final String PARAMS = "Client_Params";

    private Network mTextNetwork;
    private Network mFileNetwork;
    private Map<String, String> mGlobalHeaders;
    private Map<String, String> mGlobalParams;
    private File mUploadFile;
    private File mDownloadFile;
    private String mUploadFileKey;
    private static volatile Client mInstance;

    private Client() {
        mTextNetwork = new OkHttp();
        mFileNetwork = new OkHttp(30, 60, 60);
        mGlobalHeaders = new HashMap<>();
    }

    public static Client getInstance() {
        if (mInstance == null) {
            synchronized (Client.class) {
                if (mInstance == null) {
                    mInstance = new Client();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置超时时间
     */
    public void setTimeout(int connectTimeout, int readTimeout, int writeTimeout) {
        this.mTextNetwork = new OkHttp(connectTimeout, readTimeout, writeTimeout);
    }

    /**
     * 设置请求头
     */
    public void setHeaders(Map<String, String> headers) {
        this.mGlobalHeaders = headers;
        this.writeCache(HEADER, headers);
        if (mGlobalHeaders != null) {
            for (Map.Entry<String, String> entry : mGlobalHeaders.entrySet()) {
                Log.i(TAG, HEADER + ":" + entry.getKey() + "=" + entry.getValue());
            }
        }
    }

    /**
     * 返回请求头
     *
     * @return 请求头
     */
    public Map<String, String> getHeaders() {
        if (mGlobalHeaders == null) {
            mGlobalHeaders = this.readCache(HEADER);
        }
        return mGlobalHeaders;
    }

    /**
     * 设置全局参数
     */
    public void setParams(Map<String, String> params) {
        this.mGlobalParams = params;
        this.writeCache(PARAMS, params);
        if (mGlobalParams != null) {
            for (Map.Entry<String, String> entry : mGlobalParams.entrySet()) {
                Log.i(TAG, PARAMS + ":" + entry.getKey() + "=" + entry.getValue());
            }
        }
    }

    /**
     * 返回全局参数
     *
     * @return 全局参数
     */
    public Map<String, String> getParams() {
        if (mGlobalParams == null) {
            mGlobalParams = this.readCache(PARAMS);
        }
        return mGlobalParams;
    }

    /**
     * 写入缓存
     */
    private void writeCache(String key, Map<String, String> values) {
        Context context = Library.getInstance().getContext();
        if (context == null) {
            return;
        }
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        if (shared != null) {
            SharedPreferences.Editor editor = shared.edit();
            if (values == null || values.size() == 0) {
                editor.remove(key);
            } else {
                Gson gson = new Gson();
                String json = gson.toJson(values);
                editor.putString(key, json);
            }
            editor.apply();
        }
    }

    /**
     * 读取缓存
     */
    private Map<String, String> readCache(String key) {
        Context context = Library.getInstance().getContext();
        if (context == null) {
            return null;
        }
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        String json = shared.getString(key, null);
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }
        return null;
    }

    //设置安全措施的请求参数
    private void setSecurityParams(Map<String, String> params, Map<String, String> jsonParams) {
        //时间戳
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        HashMap<String, String> signParams = new HashMap<>();
        signParams.putAll(params);
        signParams.putAll(jsonParams);
        //签名
        try {
            String sign = SignUtils.generateSign(signParams);
            params.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //OkHttp的Value不能为NULL
    private void formatNullParams(Map<String, String> params) {
        if (params == null) {
            return;
        }
        for (Map.Entry<String, String> itEntry : params.entrySet()) {
            String value = itEntry.getValue();
            if (value == null) {
                itEntry.setValue("");//OkHttp的Value不能为NULL
            }
        }
    }

    public void cancel(String tag) {
        mTextNetwork.cancel(tag);
    }

    /**
     * 设置下载文件
     */
    public void downloadFile(File downloadFile) {
        this.mDownloadFile = downloadFile;
    }

    /**
     * 设置上传文件Key与上传文件
     */
    public void uploadFile(String uploadFileKey, File uploadFile) {
        this.mUploadFileKey = uploadFileKey;
        this.mUploadFile = uploadFile;
    }

    /**
     * 网络请求
     *
     * @param url      地址
     * @param params   请求参数
     * @param method   请求方式
     * @param callBack 回调
     * @param <T>      回调Bean
     * @param tag      已标记的请求不可重复发送
     */
    public <T> void call(String url, Map<String, String> headers, final Map<String, String> params,
                         HttpMode method, final HttpCallBack<T> callBack, Object tag) {
        //添加请求头
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("x-meridian-sign-version", "v1");        //将JSON数据也进行签名
        if (mGlobalHeaders != null) {
            headersMap.putAll(mGlobalHeaders);
        }
        if (headers != null) {
            headersMap.putAll(headers);
        }

        //OKHttp中Value值不能为NULL
        formatNullParams(mGlobalParams);
        formatNullParams(params);

        //合并请求参数并打印(不包括TYPE_JSON)
        Map<String, String> mergeParams = new HashMap<>();
        Map<String, String> jsonParams = new HashMap<>();
        if (mGlobalParams != null) {
            mergeParams.putAll(mGlobalParams);
        }
        if (params != null) {
            String key = Network.TYPE_JSON;
            String value = params.get(key);
            if (!TextUtils.isEmpty(value)) {
                jsonParams.put(key, value);
            } else {
                mergeParams.putAll(params);
            }
        }
        this.setSecurityParams(mergeParams, jsonParams);

        //打印请求参数
        Map<String, String> printParams = new HashMap<>();
        printParams.putAll(mergeParams);
        printParams.putAll(jsonParams);
        //打印输出用的URL
        String printUrl = formatUrl(url, printParams);

        //请求用的请求参数
        String requestUrl;
        Map<String, String> requestParams;
        if (method == HttpMode.GET) {
            requestUrl = formatUrl(url, mergeParams);
            requestParams = new HashMap<>();
        } else {
            if (jsonParams.size() > 0) {
                //JSON数据将全局参数拼接到URL上，局部数据填充与JSON中
                requestUrl = formatUrl(url, mergeParams);
                requestParams = jsonParams;
            } else {
                requestUrl = url;
                requestParams = mergeParams;
            }
        }


        Class<T> clazz = null;
        if (callBack != null) {
            try {
                //获取泛型的实际类型
                ParameterizedType type = (ParameterizedType) callBack.getClass()
                        .getGenericSuperclass();
                //noinspection unchecked
                clazz = (Class<T>) type.getActualTypeArguments()[0];
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        if (mUploadFile != null || mDownloadFile != null) {
            mFileNetwork.header(headersMap);
            mFileNetwork.url(requestUrl);
            mFileNetwork.param(requestParams);
            mFileNetwork.method(method);
            mFileNetwork.setPrint(printUrl);
            mFileNetwork.uploadFile(mUploadFileKey, mUploadFile);
            mFileNetwork.downloadFile(mDownloadFile);
            mFileNetwork.setTag(tag == null ? url : tag);
            mFileNetwork.request(clazz, callBack);
        } else {
            mTextNetwork.header(headersMap);
            mTextNetwork.url(requestUrl);
            mTextNetwork.param(requestParams);
            mTextNetwork.method(method);
            mTextNetwork.setPrint(printUrl);
            mTextNetwork.setTag(tag == null ? url : tag);
            mTextNetwork.request(clazz, callBack);
        }
    }

    private String formatUrl(String url, Map<String, String> params) {
        String result;
        if (params == null || params.size() == 0) {
            result = url;
        } else {
            StringBuilder builder = new StringBuilder(url);
            if (!builder.toString().contains("?")) {
                builder.append("?");
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!builder.toString().endsWith("?")) {
                    builder.append("&");
                }
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
            }
            result = builder.toString();
        }
        return result;
    }

}
