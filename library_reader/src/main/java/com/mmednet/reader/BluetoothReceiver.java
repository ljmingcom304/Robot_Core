package com.mmednet.reader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mmednet.reader.Bluetooth;

/**
 * Title:BluetoothReceiver
 * <p>
 * Description:蓝牙广播接收者
 * </p>
 * Author Jming.L
 * Date 2019/3/13 14:40
 */
public class BluetoothReceiver extends BroadcastReceiver {
	
	private Bluetooth mBluetoothUtils = Bluetooth.initBluetooth();

	@Override
	public void onReceive(Context context, Intent intent) {
		mBluetoothUtils.onReceive(context,intent);
	}

}
