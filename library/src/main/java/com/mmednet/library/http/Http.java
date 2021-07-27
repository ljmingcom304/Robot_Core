package com.mmednet.library.http;

import com.mmednet.library.common.Constants;
import com.mmednet.library.http.parse.Client;
import com.mmednet.library.http.parse.HttpCallBack;
import com.mmednet.library.http.parse.HttpMode;
import com.mmednet.library.util.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Title:Http
 * <p>
 * Description:发送HTTP请求,要求所有Bean均需要实现序列化接口
 * </p>
 * Author Jming.L
 * Date 2017/11/1 16:21
 */
public class Http {

    private static final String TAG = Http.class.getSimpleName();

    public static final String TYPE_JSON = Network.TYPE_JSON;

    /**
     * 全局请求头
     *
     * @param headers 请求头
     */
    public static void setHeaders(Map<String, String> headers) {
        getClient().setHeaders(headers);
    }

    /**
     * 返回请求头
     *
     * @return 请求头
     */
    public static Map<String, String> getHeaders() {
        return getClient().getHeaders();
    }

    /**
     * 全局请求参数
     *
     * @param params 请求参数
     */
    public static void setParams(Map<String, String> params) {
        getClient().setParams(params);
    }

    /**
     * 返回全局参数
     *
     * @return 全局参数
     */
    public static Map<String, String> getParams() {
        return getClient().getParams();
    }

    /**
     * 文件下载
     *
     * @param url      请求地址
     * @param fileName 文件名称（默认file目录）
     * @param callBack 请求回调
     * @param <T>      泛型类型
     */
    public static <T> void download(String url, String fileName, HttpCallBack<T> callBack) {
        String file = FileUtils.getDirectoryPath(Constants.FILE);
        File outFile = new File(file + fileName);
        if (outFile.exists()) outFile.delete();
        download(url, outFile, callBack);
    }

    /**
     * 文件下载
     *
     * @param url      请求地址
     * @param file     文件
     * @param callBack 请求回调
     * @param <T>      泛型类型
     */
    public static <T> void download(String url, File file, HttpCallBack<T> callBack) {
        download(url, file, callBack, null);
    }

    /**
     * 文件下载
     *
     * @param url      请求地址
     * @param file     文件
     * @param callBack 请求回调
     * @param <T>      泛型类型
     */
    public static <T> void download(String url, File file, HttpCallBack<T> callBack, Object tag) {
        Client client = getClient();
        client.uploadFile(null, null);
        client.downloadFile(file);
        client.call(url, null, null, HttpMode.GET, callBack, tag);
    }

    public static <T> void upload(String url, Map<String, String> params, File file, HttpCallBack<T> callBack) {
        upload(url, params, "filename", file, callBack, null);
    }

    public static <T> void upload(String url, Map<String, String> params, String fileKey, File file, HttpCallBack<T> callBack) {
        upload(url, params, fileKey, file, callBack, null);
    }

    /**
     * 文件上传
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param fileKey  文件关键字
     * @param file     上传文件
     * @param callBack 上传回调
     * @param tag      上传标记
     * @param <T>      泛型类型
     */
    public static <T> void upload(String url, Map<String, String> params, String fileKey, File file, HttpCallBack<T> callBack, Object tag) {
        Client client = getClient();
        client.uploadFile(fileKey, file);
        client.downloadFile(null);
        client.call(url, null, params, HttpMode.POST, callBack, tag);
    }

    public static <T> void send(String url, HttpCallBack<T> callBack) {
        send(url, null, null, HttpMode.GET, callBack, null);
    }

    public static <T> void send(String url, HttpCallBack<T> callBack, Object tag) {
        send(url, null, null, HttpMode.GET, callBack, tag);
    }

    public static <T> void send(String url, Map<String, String> params, HttpCallBack<T> callBack) {
        send(url, null, params, HttpMode.POST, callBack, null);
    }

    public static <T> void send(String url, Map<String, String> params, HttpCallBack<T> callBack, Object tag) {
        send(url, null, params, HttpMode.POST, callBack, tag);
    }

    public static <T> void send(String url, Map<String, String> params, HttpMode method, HttpCallBack<T> callBack) {
        send(url, null, params, method, callBack, null);
    }

    public static <T> void send(String url, Map<String, String> params, HttpMode method, HttpCallBack<T> callBack, Object tag) {
        send(url, null, params, method, callBack, tag);
    }

    public static <T> void send(String url, Map<String, String> headers, Map<String, String> params, HttpCallBack<T> callBack) {
        send(url, headers, params, HttpMode.POST, callBack, null);
    }

    public static <T> void send(String url, Map<String, String> headers, Map<String, String> params, HttpMode method, HttpCallBack<T> callBack) {
        send(url, headers, params, method, callBack, null);
    }

    /**
     * @param url      请求地址
     * @param headers  请求头
     * @param params   请求参数
     * @param method   请求方式
     * @param callBack 请求回调
     */
    public static <T> void send(String url, Map<String, String> headers, Map<String, String> params, HttpMode method, HttpCallBack<T> callBack, Object tag) {
        Client client = getClient();
        client.uploadFile(null, null);
        client.downloadFile(null);
        client.call(url, headers, params, method, callBack, tag);
    }

    /**
     * 取消网络请求
     *
     * @param url 地址
     */
    public static void cancel(String url) {
        getClient().cancel(url);
    }

    /**
     * 获取网络请求实例
     */
    public static Client getClient() {
        return Client.getInstance();
    }

}
