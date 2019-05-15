package com.mmednet.library.http.parse;

import android.text.TextUtils;
import android.util.Log;

import com.mmednet.library.http.Network;
import com.mmednet.library.http.okhttp.OkHttp;
import com.mmednet.library.log.Logger;
import com.mmednet.library.util.SignUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private static final String TAG = "Client";
    private Network mTextNetwork;
    private Network mFileNetwork;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private File mUploadFile;
    private File mDownloadFile;
    private String mUploadFileKey;

    private Client() {
        mTextNetwork = new OkHttp();
        mFileNetwork = new OkHttp(30, 60, 60);
        mHeaders = new HashMap<>();
    }

    private enum Singleton {
        INSTANCE;
        private Client client;

        Singleton() {
            client = new Client();
        }

        public Client getInstance() {
            return client;
        }
    }

    public static Client getInstance() {
        return Singleton.INSTANCE.getInstance();
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
        this.mHeaders = headers;
    }

    /**
     * 设置全局参数
     */
    public void setParams(Map<String, String> params) {
        this.mParams = params;
        if (mParams == null) return;
        for (Map.Entry<String, String> entry : this.mParams.entrySet()) {
            Log.i(TAG, "GlobalParams:" + entry.getKey() + "=" + entry.getValue());
        }
    }

    //设置安全措施的请求参数
    private void setSecurityParams(Map<String, String> params) {
        //时间戳
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        for (Map.Entry<String, String> itEntry : params.entrySet()) {
            String value = itEntry.getValue();
            if (value == null) {
                itEntry.setValue("");//OkHttp的Value不能为NULL
            }
        }
        //签名
        try {
            String sign = SignUtils.generateSign(params);
            params.put("sign", sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
     */
    public <T> void call(String url, Map<String, String> headers, final Map<String, String> params,
                         HttpMode method, final HttpCallBack<T> callBack) {


        //添加请求头
        HashMap<String, String> headersMap = new HashMap<>();
        if (mHeaders != null) {
            headersMap.putAll(mHeaders);
        }
        if (headers != null) {
            headersMap.putAll(headers);
        }

        //合并请求参数并打印(不包括TYPE_JSON)
        Map<String, String> mergeParams = new HashMap<>();
        Map<String, String> jsonParams = new HashMap<>();
        if (mParams != null) {
            mergeParams.putAll(mParams);
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
        this.setSecurityParams(mergeParams);

        //打印请求参数
        Map<String, String> printParams = new HashMap<>();
        printParams.putAll(mergeParams);
        printParams.putAll(jsonParams);
        this.formatUrl(url, printParams, true);

        //请求用的请求参数
        String requestUrl;
        Map<String, String> requestParams;
        if (method == HttpMode.GET) {
            requestUrl = formatUrl(url, mergeParams, false);
            requestParams = new HashMap<>();
        } else {
            if (jsonParams.size() > 0) {
                requestUrl = formatUrl(url, mergeParams, false);
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
            mFileNetwork.uploadFile(mUploadFileKey, mUploadFile);
            mFileNetwork.downloadFile(mDownloadFile);
            mFileNetwork.request(clazz, callBack);
            mFileNetwork.tag(url);
        } else {
            mTextNetwork.header(headersMap);
            mTextNetwork.url(requestUrl);
            mTextNetwork.param(requestParams);
            mTextNetwork.method(method);
            mTextNetwork.request(clazz, callBack);
            mTextNetwork.tag(url);
        }
    }

    private String formatUrl(String url, Map<String, String> params, boolean isPrint) {
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
        if (isPrint) {
            Logger.i(TAG, result);
        }
        return result;
    }

}
