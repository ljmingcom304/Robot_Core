package com.mmednet.reader.listener;

import android.util.Log;

import com.sunrise.reader.IDecodeIDServerListener;

/**
 * Title:OnServerListener
 * <p>
 * Description:设备连接监听
 * </p>
 * Author Jming.L
 * Date 2019/3/13 15:40
 */
public class OnServerListener implements IDecodeIDServerListener {

    @Override
    public void getThisServer(String s, int i) {

    }

    @Override
    public void getThisServer(String ip, int port, int recount) {
        Log.i(getClass().getSimpleName(), "设备连接：IP=" + ip + ";Port=" + port + ";Recount=" + recount);
    }
}
