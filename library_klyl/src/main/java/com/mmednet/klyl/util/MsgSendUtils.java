package com.mmednet.klyl.util;

import com.google.gson.Gson;
import com.mmednet.klyl.bean.StringMsgBean;
import com.mmednet.klyl.constants.IPConfig;
import com.mmednet.klyl.mina.MinaSocketClient;

/**
 * 平板发送消息给3288 ----通过串口和socket在这里切换
 * 
 * @author xiaowei
 * 
 */
public class MsgSendUtils {

	private Gson gson = new Gson();
	public static final String TAG = "MsgSendUtils";
	private MinaSocketClient mMsgClient;
	private MinaSocketClient mFaceClient;
	private MinaSocketClient mPhoneClient;
	private static MsgSendUtils instance = new MsgSendUtils();
	private MsgSendUtils(){
		initSocketClient();
	}
	
	public static MsgSendUtils getInstance(){
		return instance;
	}
	
	public void initSocketClient(){
		mMsgClient = new MinaSocketClient(
				IPConfig.SOCKET_HOST, IPConfig.SOCKET_PORT);
		mFaceClient = new MinaSocketClient(
				IPConfig.SOCKET_HOST, IPConfig.FACE_SOCKET_PORT);
		mPhoneClient = new MinaSocketClient(
				IPConfig.SOCKET_HOST, IPConfig.PHONE_SOCKET_PORT);
	}

	/**
	 * 发送String类型的数据
	 */
	public void sendStringMsg(int msgType, String msgData) {
		StringMsgBean bean = new StringMsgBean();
		bean.setMsgType(msgType);
		bean.setMsgData(msgData);
		String sendBuf = gson.toJson(bean);
		mMsgClient.sendMessage(sendBuf);
	}
	
	/**
	 * 发送人脸识别的数据
	 */
	public void sendFaceMsg(int msgType, String msgData){
		StringMsgBean bean = new StringMsgBean();
		bean.setMsgType(msgType);
		bean.setMsgData(msgData);
		String sendBuf = gson.toJson(bean);
		mFaceClient.sendMessage(sendBuf);
	}
	
	/**
	 * 发送视频聊天数据
	 */
	public void sendPhoneMsg(int msgType, String msgData){
		StringMsgBean bean = new StringMsgBean();
		bean.setMsgType(msgType);
		bean.setMsgData(msgData);
		String sendBuf = gson.toJson(bean);
		mPhoneClient.sendMessage(sendBuf);
	}
	
	

}
