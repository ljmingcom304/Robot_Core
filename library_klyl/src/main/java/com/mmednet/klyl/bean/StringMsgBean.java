package com.mmednet.klyl.bean;

import java.io.Serializable;

/**
 * <p>Title:StringMsgBean</p>
 * <p>Description:传输文本消息</p>
 *
 * @author 梁敬明
 * @date 2017年3月18日 下午2:08:58
 */
public class StringMsgBean implements Serializable {

	/**
	 * 消息类型，详见MsgType，，和串口保持一致
	 */
	private int msgType;

	/**
	 * 数据
	 */
	private String msgData;

	public StringMsgBean(int msgType,  String msgData) {
		super();
		this.msgType = msgType;
		this.msgData = msgData;
	}

	public StringMsgBean() {
		super();
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getMsgData() {
		return msgData;
	}

	public void setMsgData(String msgData) {
		this.msgData = msgData;
	}

	@Override
	public String toString() {
		return "StringMsgBean [msgType=" + msgType + ", msgData=" + msgData + "]";
	}
	
}
