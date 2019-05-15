package com.mmednet.library.http;

import com.mmednet.library.http.parse.HttpCallBack;
import com.mmednet.library.http.parse.HttpMode;

import java.io.File;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;

/**
 * Title:Network
 * <p>
 * Description:网络请求公共接口
 * </p>
 * Author Jming.L
 * Date 2017/10/31 11:18
 */
public abstract class Network {

    public static final String TYPE_JSON = "NETWORK_TYPE_JSON";//JSON类型的参数

    /**
     * 标记
     *
     * @param tag 标记
     */
    public abstract void tag(String tag);

    /**
     * 取消网络请求
     *
     * @param tag 标记
     */
    public abstract void cancel(String tag);

    /**
     * Connection
     *
     * @param url 地址
     */
    public abstract void url(String url);

    /**
     * 请求头
     *
     * @param headers 头集合
     */
    public abstract void header(Map<String, String> headers);

    /**
     * 请求参数
     *
     * @param params 参数集合
     */
    public abstract void param(Map<String, String> params);

    /**
     * 请求方式
     *
     * @param method 请求方式
     */
    public abstract void method(HttpMode method);

    /**
     * 文件上传
     *
     * @param fileKey 文件Key
     * @param file    文件
     */
    public abstract void uploadFile(String fileKey, File file);

    /**
     * 文件下载
     *
     * @param file 文件
     */
    public abstract void downloadFile(File file);

    /**
     * 网络请求
     *
     * @param clazz    Bean字节码
     * @param callBack 请求回调
     * @param <T>      Bean类型
     */
    public abstract <T> void request(Class<T> clazz, HttpCallBack<T> callBack);

}
