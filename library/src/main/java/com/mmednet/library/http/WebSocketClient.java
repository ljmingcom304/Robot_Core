package com.mmednet.library.http;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Title:WebSocket
 * <p>
 * Description:OkHttp版本WebSocket
 * </p >
 * Author Jming.L
 * Date 2021/1/11 11:26
 */
public class WebSocketClient extends WebSocketListener {

    private static final String TAG = WebSocketClient.class.getSimpleName();

    public okhttp3.WebSocket initWebSocket(String url) {
        long timeOut = 15;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(timeOut, TimeUnit.SECONDS)       //设置读取超时时间
                .writeTimeout(timeOut, TimeUnit.SECONDS)      //设置写的超时时间
                .connectTimeout(timeOut, TimeUnit.SECONDS)    //设置连接超时时间build();
                .pingInterval(20, TimeUnit.SECONDS)       //设置 PING 帧发送间隔
                .build();
        Request request = new Request.Builder().url(url).build();
        return okHttpClient.newWebSocket(request, this);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        Log.i(TAG, "WebSocket连接成功：" + response.message());
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        Log.i(TAG, "WebSocket接收消息：" + text);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        Log.i(TAG, "WebSocket主动关闭：[" + code + "]" + reason);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        Log.i(TAG, "WebSocket连接关闭：[" + code + "]" + reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        Log.e(TAG, "WebSocket连接异常", t);
    }
}
