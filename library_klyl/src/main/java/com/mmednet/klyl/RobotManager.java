package com.mmednet.klyl;

import android.content.Context;

public abstract class RobotManager {
	/**开启聊天*/
	public void startChat(){};
	/**退出聊天*/
	public void stopChat(){};
	/**文本转语音*/
	public abstract void text2Speech(String text, KLCallback callback);
	/**语音转文本*/
	public abstract void speech2Text(KLCallback callback);
	
	// --------------------视频通信---------------------
	/** 电话被挂 */
	public void hangupPhoneByOther(KLCallback callback){};
	/** 拒接电话 */
	public void refusePhone(){};
	/** 挂断电话 */
	public void hangupPhoneBySelf(){};
	/** 主动接听电话 */
	public void acceptPhone(){};
	/** 取消拨打*/
	public void stopPhone(){};
	/** 拨打电话 */
	public void startPhone(String name, KLCallback callback){};
	/** 来电提醒 */
	public void receivePhone(KLCallback callback){};
	//--------------------视频通信---------------------
	
	/**初始化资源*/
	public void init(Context context){};
	/**回收资源*/
	public void release(){};
}
