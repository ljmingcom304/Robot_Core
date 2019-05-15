package com.mmednet.library.http.okhttp;

import android.text.TextUtils;
import android.util.Log;

import com.mmednet.library.http.Network;
import com.mmednet.library.http.parse.HttpCallBack;
import com.mmednet.library.http.parse.HttpHeader;
import com.mmednet.library.http.parse.HttpMode;
import com.mmednet.library.http.parse.Resolver;
import com.mmednet.library.log.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

/**
 * Title:OkHttp
 * <p>
 * Description:
 * </p>
 * Author Jming.L
 * Date 2017/11/1 11:29
 */
public class OkHttp extends Network {

    private String url;
    private String tag;
    private String uploadFileKey;
    private File downloadFile;
    private File uploadFile;
    private HttpMode method;
    private OkHttpClient client;
    private Request.Builder requestBuilder;
    private Map<String, Call> tags;
    private Map<String, String> headers;
    private Map<String, String> params;
    private static final String TAG = "OkHttp";
    private Resolver resolver;

    public OkHttp() {
        this(15, 30, 30);
    }

    public OkHttp(int connectTimeout, int readTimeout, int writeTimeout) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(connectTimeout, TimeUnit.SECONDS);   //连接超时
        clientBuilder.readTimeout(readTimeout, TimeUnit.SECONDS);      //读取超时
        clientBuilder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        clientBuilder.cookieJar(new OkCookie());
        clientBuilder.sslSocketFactory(OkHttpFactory.buildSSLSocketFactory());
        clientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        client = clientBuilder.build();
        resolver = new Resolver();
        tags = new HashMap<>();
    }


    @Override
    public <T> void request(final Class<T> clazz, final HttpCallBack<T> callBack) {
        requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        //设置请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                try {
                    requestBuilder.header(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //设置请求体
        if (method == HttpMode.GET) {
            requestBuilder.get();
        } else {
            method = HttpMode.POST;//其他情况默认POST请求
            //文件上传
            if (uploadFile != null) {
                if (uploadFile.exists()) {
                    RequestBody requestBody = this.upload(uploadFile, params, callBack);
                    requestBuilder.post(requestBody);
                } else {
                    Logger.e(TAG, uploadFile.getName() + " does not exist.");
                }
            } else {
                if (params.keySet().contains(TYPE_JSON)) {//JSON类型的请求
                    String json = params.get(TYPE_JSON);
                    MediaType type = MediaType.parse("application/json;charset=utf-8");
                    RequestBody body = RequestBody.create(type, json);
                    requestBuilder.post(body);
                } else {//普通POST请求
                    FormBody.Builder body = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (key != null && value != null) {
                            body.add(entry.getKey(), entry.getValue());
                        } else {
                            Logger.e(TAG, "请求参数为空：key=" + key + " value=" + value);
                        }
                    }
                    FormBody formBody = body.build();
                    requestBuilder.post(formBody);
                }
            }

        }

        //响应
        Call call = client.newCall(requestBuilder.build());
        tags.put(tag, call);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                tags.remove(tag);
                tag = null;
                Logger.e(TAG, "###################################################################");
                Logger.e(TAG, call.request().url().toString());
                Logger.e(TAG, e.getMessage());
                Logger.e(TAG, "###################################################################");
                resolver.handleFailure(e.getMessage(), callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                tags.remove(tag);
                tag = null;
                Request request = response.request();
                Headers reqHeaders = request.headers();
                Headers resHeaders = response.headers();
                Logger.i(TAG, "###################################################################");
                Logger.i(TAG, call.request().url().toString());
                Logger.i(TAG, "Status:[Method:" + request.method() + "][Code:" + response.code() + "][Message:" + response.message() + "]");
                Logger.i(TAG, "Request:" + headersToString(reqHeaders));//请求
                String reqCookie = cookiesToString(request.url(), reqHeaders);
                if (!TextUtils.isEmpty(reqCookie)) {
                    Logger.i(TAG, "Cookie-Request:" + reqCookie);
                }
                String resCookie = cookiesToString(request.url(), resHeaders);
                if (!TextUtils.isEmpty(resCookie)) {
                    Logger.i(TAG, "Cookie-Response:" + resCookie);
                }
                Logger.i(TAG, "Response:" + headersToString(resHeaders));//响应
                Logger.i(TAG, "###################################################################");
                String result = "";
                if (downloadFile == null) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        long length = body.contentLength();
                        if (length > 10 * 1024 * 1024) {
                            Log.e(TAG, "ContentLength of response is too long.[>10MB]");
                        } else {
                            result = body.string();
                            String key = resHeaders.get(HttpHeader.KEY);
                            if (!TextUtils.isEmpty(key)) {
                                long startTime = System.currentTimeMillis();
                                result = HttpHeader.format(result, key);
                                long endTime = System.currentTimeMillis();
                                Logger.i("执行时间=" + (endTime - startTime) + "ms");
                            }
                        }
                    }
                }
                Logger.i(TAG, result);
                if (TextUtils.equals(result, "Canceled")) {
                    result = "";
                }
                if (response.isSuccessful()) {
                    resolver.handleSuccess(result, clazz, callBack);
                } else {
                    result = response.code() + " " + response.message();
                    resolver.handleFailure(result, callBack);
                }

                if (downloadFile != null) {
                    download(response, callBack);
                }
            }
        });
    }

    private String cookiesToString(HttpUrl url, Headers headers) {
        List<Cookie> cookies = Cookie.parseAll(url, headers);
        StringBuilder builder = new StringBuilder();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String str = "[" + cookie.name() + ":" + cookie.value() + "]";
                builder.append(str);
            }
        }
        return builder.toString();
    }

    private String headersToString(Headers headers) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = headers.size(); i < size; i++) {
            builder.append("[").append(headers.name(i)).append(":").append(headers.value(i)).append("]");
        }
        return builder.toString();
    }

    //文件上传
    private RequestBody upload(File file, Map<String, String> params, HttpCallBack callback) {
        RequestBody requestBody = null;
        if (file.exists()) {
            //设置文件的类型
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody fileBody = RequestBody.create(mediaType, file);
            //设置类型
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            bodyBuilder.addFormDataPart(uploadFileKey, file.getName(), fileBody);//上传文件
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null && value != null) {
                    bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                } else {
                    Logger.e(TAG, "请求参数为空：key=" + key + " value=" + value);
                }
            }
            MultipartBody build = bodyBuilder.build();
            requestBody = ProgressHelper.withProgress(build, callback);
        }
        return requestBody;
    }

    //文件下载（GET请求）
    private void download(Response response, HttpCallBack callback) {
        ResponseBody responseBody = ProgressHelper.withProgress(response.body(), callback);
        BufferedSource source = responseBody.source();
        BufferedSink bufferedSink = null;
        try {
            //将文件输出到指定目录
            bufferedSink = Okio.buffer(Okio.sink(downloadFile));
            source.readAll(bufferedSink);
            bufferedSink.flush();
        } catch (IOException e) {
            e.printStackTrace();
            resolver.handleFailure(e.getMessage(), callback);
        } finally {
            if (bufferedSink != null) {
                try {
                    bufferedSink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void tag(String tag) {
        Call call = tags.get(tag);
        if (call != null) {
            if (!call.isCanceled()) {
                call.cancel();
                tags.remove(tag);
            }
        }
        this.tag = tag;
        requestBuilder.tag(this.tag);
    }

    @Override
    public void cancel(String tag) {
        Call call = tags.get(tag);
        if (call != null) {
            if (!call.isCanceled()) {
                call.cancel();
                tags.remove(tag);
            }
        }
    }

    @Override
    public void url(String url) {
        this.url = url;
    }

    @Override
    public void header(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void param(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public void method(HttpMode method) {
        this.method = method;
    }

    @Override
    public void uploadFile(String fileKey, File file) {
        this.uploadFileKey = fileKey;
        this.uploadFile = file;
    }

    @Override
    public void downloadFile(File file) {
        this.downloadFile = file;
    }


}
