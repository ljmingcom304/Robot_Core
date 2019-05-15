package com.mmednet.klyl.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpUtil {
	
	public interface HttpCallBackListener{
		void onCallBack(String result);
	}

	private static HttpCallBackListener listener;

	private static Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				listener.onCallBack((String)msg.obj);
			}else{
                listener.onCallBack((String)msg.obj);
			}
		}
	};

	public static void go(String url, HttpCallBackListener listener) {
		listener = listener;
		OkHttpClient mHttpClient = new OkHttpClient();

		Request request = new Request.Builder().url(url).build();
		mHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, final IOException ex) {
				Message msg = mHandler.obtainMessage();
                msg.obj = "error";
                msg.what = 0;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				if (response.isSuccessful()) {
					mHandler.sendEmptyMessage(1);
					final String str = response.body().string();
                    Message msg = mHandler.obtainMessage();
                    msg.obj = str;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
				}
			}

		});
	}
}
