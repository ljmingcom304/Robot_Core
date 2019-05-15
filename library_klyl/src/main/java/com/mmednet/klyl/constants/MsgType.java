package com.mmednet.klyl.constants;

/**
 * <p>
 * Title:MsgType
 * </p>
 * <p>
 * Description:机器人控制指令
 * </p>
 * 
 * @author 梁敬明
 * @date 2017年3月18日 下午2:22:07
 */
public class MsgType {

	// 平板接受的消息编号---- 1--- 8399
	// 平板发送的消息编号---- 8400-65535
	/**
	 * 消息类型---停止播放音频
	 */
	public static final int SEND_MSGTYPE_STOP_SOUND = 8399;
	/**
	 * 消息类型---播放音频
	 */
	public static final int AUDIO_PLAY_SEND = 8400;

	/** 获取音频列表 */
	public static final int AUDIO_LIST_SEND = 10003;
	/** 接收音频列表 */
	public static final int AUDIO_LIST_RECEIVE = 10004;
	/**
	 * 消息类型---播放TTS不进入语音识别模式
	 */
	public static final int TTS_SEND = 8401;

	/**
	 * 消息类型--设置wifi密码
	 */
	public static final int WIFI_SEND = 8405;

	/**
	 * 消息类型--停止滑动声音SeekBar时的进度
	 */
	public static final int VOICE_SEND = 8407;

	/**
	 * 发送动作指令
	 */
	public static final int ACTION_SEND = 8420;

	/**
	 * 消息类型--拍照预览开始
	 */
	public static final int SEND_SCAN_START = 9003;
	/**
	 * 消息类型--拍照预览结束
	 */
	public static final int SEND_SCAN_CLOSE = 9004;

	/**
	 * 消息类型----接受语音识别的结果--同时是结束录音的消息
	 */
	public static final int STT_RECEIVE = 1;

	/**
	 * 消息----收到唤醒的消息，
	 */
	public static final int MODE_WAKEUP_RECEIVE = 6;

	/**
	 * 退出识别，进入唤醒时--pad跳转到唤醒界面
	 */
	public static final int MODE_SLEEP_RECEIVE = 9;

	public static final int FACE_FILE_TYPE = 8398;

	/**
	 * 消息类型--开始声控
	 */
	public static final int CHAT_START_SEND = 8403;

	/**
	 * 消息类型--第三方注册
	 */
	public static final int FACE_REGISTER_SEND = 8439;

	/**
	 * 消息类型--接收人脸识别结果
	 */
	public static final int FACE_RECOGNITION_START_RECEIVE = 8440;

	/**
	 * 消息类型--接收启动注册命令
	 */
	public static final int PHOTO_START_RECEIVE = 8441;
	/**
	 * 消息类型--第三方注册结果
	 */
	public static final int FACE_REGISTER_RECEIVE = 8438;

	/**
	 * 消息类型--收到开始拍照
	 */
	public static final int PHOTO_START_SEND = 8442;

	/**
	 * 收到开始人脸识别
	 */
	public static final int FACE_RECOGNITION_START_SEND = 8443;

	/**
	 * 收到停止人脸
	 */
	public static final int FACE_RECOGNITION_CLOSE_SEND = 8444;

	public static final int STT_START_SEND = 8429;

	/**
	 * 退出语音识别
	 */
	public static final int STT_CLOSE_SEND = 8428;

	/**
	 * 消息类型--播放TTS后继续开始语音识别
	 */
	public static final int TTS_START_RECOGNITION_SEND = 8421;
	/**
	 * 消息类型--地图去具体的某个点
	 */
	public static final int ACTION_ADDRESS_REACH_SEND = 8433;
	/**
	 * 消息类型--机器人到达某个点进行提示
	 */
	public static final int ACTION_ADDRESS_REACH_RECEIVE = 8434;

	/**
	 * 消息类型 --- 开始录音
	 */
	public static final int RECEIVE_START_RECORD = 10000;
	
	/**
     * 声控从聊天模式进入语音识别模式的回调
     */
    public static final int STT_PASSWORD_RECEIVE = 10009;

	/**
	 * 消息类型 --- 设置进入识别模式关键字(经纶世纪有识别模式和聊天模式)，将关键词传过来(在聊天模式中通过关键词进入语音识别模式)
	 */
	public static final int STT_PASSWORD_SEND = 10001;

	/**
	 * 消息类型 --- 设置错误识别次数，到次数后进入休眠模式
	 */
	public static final int MODE_SWITCH_COUNT = 10002;

	/**
	 * 发送系统设置信息到pad
	 */
	public static final int SYSTEM_INFO_RECEIVE = 16;

	/**
	 * 消息类型--获取优友系统信息，发送到pad端
	 */
	public static final int SYSTEM_INFO_SEND = 8432;
	/** 获取表情列表 */
	public static final int EMOJI_LIST_SEND = 10005;
	/** 接收表情列表 */
	public static final int EMOJI_LIST_RECEIVE = 10006;
	/** 播放指定表情 */
	public static final int EMOJI_PLAY_SEND = 10007;
	/** 消息类型---播放tts结束 */
	public static final int TTS_RECEIVE = 10008;

	/**
	 * 消息类型--接收联系人列表
	 */
	public static final int PHONE_CONTANT_LIST_RECEIVE = 18428;

	/**
	 * 消息类型--发送联系人信息--手机端拨打电话
	 */
	public static final int PHONE_START_SEND = 18434;

	/**
	 * 消息类型--来电操作--接听/拒接
	 */
	public static final int PHONE_ACCEPT_SEND = 18436;

	/**
	 * 消息类型--来电提醒
	 */
	public static final int PHONE_REMIND_SEND = 18437;

	/**
	 * 消息类型 --取消拨打
	 */
	public static final int PHONE_CANCEL_SEND = 18438;

	/**
	 * 消息类型--主动获取联系人列表
	 */
	public static final int PHONE_CONTANT_LIST_SEND = 18439;

	/**
	 * 消息类型--收到挂断电话
	 */
	public static final int PHONE_HANGUP_OTHER_RECEIVE = 18440;

	/**
	 * 消息类型-- 机器给手机拨打电话，对方无应答时给pad发送的消息
	 */
	public static final int PHONE_START_RECEIVE = 18441;
	/**
	 * 消息类型--发送挂断电话
	 */
	public static final int PHONE_HANGUP_SELF_SEND = 18442;

}
