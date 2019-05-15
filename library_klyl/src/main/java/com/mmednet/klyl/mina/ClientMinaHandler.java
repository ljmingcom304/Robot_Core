package com.mmednet.klyl.mina;

import java.net.URLDecoder;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.text.TextUtils;
import android.util.Log;

public class ClientMinaHandler extends IoHandlerAdapter {

	public static final String TAG = "ClientMinaHandler";

	private MinaSocketClient mClient;

	public ClientMinaHandler(MinaSocketClient client) {
		mClient = client;
	}

	// 服务器与客户端创建连接
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		// Log.e(TAG, "sessionCreated~");
	}

	// 服务器与客户端连接打开
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		// Log.e(TAG, "sessionOpened~");
	}

	// 关闭与服务端的连接时会调用此方法
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		// Log.e(TAG, "sessionClosed~");
	}

	// 进入空闲状态
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		super.sessionIdle(session, status);
		// Log.e(TAG, "sessionIdle~");
	}

	// 异常状态
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {

		super.exceptionCaught(session, cause);
		Log.e(TAG, "exception：" + cause.getLocalizedMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		super.messageReceived(session, message);
		if (!TextUtils.isEmpty(message.toString())) {
			Log.e(TAG,
					"messaeg received："
							+ URLDecoder.decode(message.toString(), "UTF-8"));
			if (null != mClient) {
				mClient.receiveMessage(message.toString());
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		Log.e(TAG,
				"message sent:"
						+ URLDecoder.decode(message.toString(), "UTF-8"));
	}

}
