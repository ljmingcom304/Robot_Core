package com.mmednet.klyl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.mmednet.klyl.bean.StringMsgBean;
import com.mmednet.klyl.callback.OnListener;

public class HandleMessage {

	private Gson gson = new Gson();
	private static HandleMessage message = new HandleMessage();
	private ConcurrentHashMap<String, OnListener> mMap = new ConcurrentHashMap<>();
	private static final String TAG = "HandleMessage";
	/** 消息返回主线程 */
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			for (Map.Entry<String, OnListener> entry : mMap.entrySet()) {
				OnListener value = entry.getValue();
				value.onListen((String) msg.obj, msg.what);
			}
		}
	};

	private HandleMessage() {

	}

	public static HandleMessage getInstance() {
		return message;
	}

	/** 处理服务器返回的消息 */
	public void handleStringMsg(String data) {
		if (data != null) {
			try {
				StringMsgBean strMsgBean = gson.fromJson(data,StringMsgBean.class);
				if (strMsgBean != null) {
					int msgType = strMsgBean.getMsgType();
					String msgData = strMsgBean.getMsgData();
					mHandler.obtainMessage(msgType, msgData).sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setOnListener(OnListener listener) {
		mMap.put(listener.getTag(), listener);
	}

}
