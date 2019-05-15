package com.mmednet.klyl.callback;

/**
 * <p>Title:ModeCallback</p>
 * <p>Description:机器人模式</p>
 *
 * @author 梁敬明
 * @date 2017年6月5日 下午1:52:29
 */
public interface ModeCallback {
	/**唤醒*/
	void onWake();
	/**休眠*/
	void onSleep();
}
