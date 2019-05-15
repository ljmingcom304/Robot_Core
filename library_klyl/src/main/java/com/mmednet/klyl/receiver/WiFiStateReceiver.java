package com.mmednet.klyl.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mmednet.klyl.KLRobot;
import com.mmednet.klyl.util.MsgSendUtils;

/**
 * <p>
 * Title:WiFiStateReceiver
 * </p>
 * <p>
 * Description:监听WiFi状态改变
 * </p>
 * 
 * @author 梁敬明
 * @date 2017年6月6日 下午7:06:23
 */
public class WiFiStateReceiver extends BroadcastReceiver {

	private Context mContext;
	private KLRobot mManger = KLRobot.getInstance();

	private Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			WifiManager wifiManager = (WifiManager) mContext
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			Log.e("WifiStateReceiver", wifiInfo.getSSID());
			MsgSendUtils.getInstance().initSocketClient();
			mManger.setSpeech2TextCode("健康服务", null);
		}

	};

	@Override
	public void onReceive(Context context, Intent intent) {
		/*if (Robot.getContext() != RobotContext.KLYL) {
			return;
		}*/
		Bundle bundle = intent.getExtras();
		int statusInt = bundle.getInt("wifi_state");
		if (statusInt == WifiManager.WIFI_STATE_ENABLED) {
			mContext = context;
			mHandler.sendEmptyMessageDelayed(0, 3000);
		}
	}

}
