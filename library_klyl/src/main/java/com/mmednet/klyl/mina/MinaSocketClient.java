package com.mmednet.klyl.mina;

import android.os.SystemClock;
import android.util.Log;

import com.mmednet.klyl.HandleMessage;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinaSocketClient implements IMsgCallback {

	private static final String TAG = "MinaSocketClient";
	private static final String CHAR_SET = ChangeCharSet.US_ASCII;
	private static final int THEAD_POOL_NUM = 5;
	private ExecutorService mService = null;
	private NioSocketConnector mConnector;
	private volatile IoSession mSession;
	private String mAddress;
	private int mPort;
	private volatile boolean flag = true;

	public MinaSocketClient(String host,int port) {
		mAddress = host;
		mPort = port;
		mService = Executors.newFixedThreadPool(THEAD_POOL_NUM);
		connectServer();
	}

	/**
	 * 连接服务器
	 */
	public void connectServer() {
		if (mService == null) {
			return;
		}
		mService.submit(new Runnable() {
			@Override
			public void run() {
				connectToServer();
			}
		});
	}

	private void connectToServer() {
		if (mConnector == null) {
			mConnector = new NioSocketConnector();
			DefaultIoFilterChainBuilder chain = mConnector.getFilterChain();
			TextLineCodecFactory factory = new TextLineCodecFactory(
					Charset.forName(CHAR_SET));
			factory.setDecoderMaxLineLength(1024*1024);//设置文本的解码长度
			factory.setEncoderMaxLineLength(1024*1024);//设置文本的编码长度
			chain.addLast("codec", new ProtocolCodecFilter(factory));
			mConnector.setHandler(new ClientMinaHandler(this));
			mConnector.setConnectTimeoutCheckInterval(30);
			mConnector.setConnectTimeoutMillis(3000); // 设置连接超时
			mConnector.getFilterChain()
					.addLast("mdc", new MdcInjectionFilter());
			//factory.setDecoderMaxLineLength(1024*1024);
			//factory.setEncoderMaxLineLength(1024*1024);
			mConnector.getSessionConfig().setReceiveBufferSize(1024); // 设置接收缓冲区的大小
			mConnector.getSessionConfig().setSendBufferSize(1024);// 设置输出缓冲区的大小
			mConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
					30000); // 读写都空闲时间:30秒
			mConnector.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE,
					40000);// 读(接收通道)空闲时间:40秒
			mConnector.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE,
					50000);// 写(发送通道)空闲时间:50秒
			mConnector.setDefaultRemoteAddress(new InetSocketAddress(mAddress,
					mPort));// 设置默认访问地
			mConnector.getFilterChain().addFirst("reconnection",
					new IoFilterAdapter() {
				
				
						@Override
						public void sessionClosed(NextFilter nextFilter,
								IoSession ioSession) throws Exception {
							flag = true;
							mSession = null;
							while (flag) {
								Log.e(TAG, "lost-----reconnect -------");
								SystemClock.sleep(1000);
								ConnectFuture future = mConnector.connect();
								future.awaitUninterruptibly();// 等待连接创建成功
								mSession = future.getSession();// 获取会话
								if (mSession.isConnected()) {
									flag = false;
									Log.e(TAG, "connect success ~1");
									break;
								}
							}
						}
					});
			while (flag) {
				try {
					Log.i(TAG, "start connect ");
					ConnectFuture future = mConnector.connect();
					// 等待连接创建成功
					future.awaitUninterruptibly();
					// 获取会话
					mSession = future.getSession();// 获取会话
					// 获取会话
					if (mSession.isConnected()) {
						flag = false;
						Log.e(TAG, "connect success ~0");
						break;
					}
				} catch (RuntimeIoException e) {
					SystemClock.sleep(2000);// 连接失败后,重连10次,间隔30s
				}
			}
		}
	}
	
	

	public NioSocketConnector getConnector() {
		return mConnector;
	}

	/**
	 * 关闭socket连接
	 */
	public void closeConnect() {
		if (null != mSession && !mSession.isClosing()) {
			mSession.close(false);
		}
	}

	/**
	 * 发送消息
	 */
	public void sendMessage(String msg) {
		if (mSession != null) {
			try {
				msg = URLEncoder.encode(msg, ChangeCharSet.UTF_8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			mSession.write(msg);
		} else {
			Log.e(TAG, "socket not connect!");
		}
	}

	/**
	 * 发送消息
	 */
	public void sendMessage(byte[] data) {
		if (mSession != null) {
			String temp = "";
			for (int i = 0; i < data.length; i++) {
				temp += "  " + Integer.toHexString(data[i] & 0xff);
			}
			mSession.write(IoBuffer.wrap(data));
		} else {
			Log.e(TAG, "socket not connect!");
		}
	}

	/**
	 * 发送消息，采用可不参数，将多条消息合并一起发送
	 */
	public synchronized void sendMessage(byte[] first, byte[]... rest) {
		if (mSession != null) {
			mSession.write(IoBuffer.wrap(concatAll(first, rest)));
		} else {
			Log.e(TAG, "socket not connect!");
		}
	}

	/**
	 * 合并多个数组
	 * 
	 * @param first
	 * @param rest
	 * @return
	 */
	private byte[] concatAll(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		String temp = " ";
		for (int i = 0; i < result.length; i++) {
			temp += Integer.toHexString(result[i] & 0xff) + "  ";
		}
		return result;
	}

	@Override
	public void receiveMessage(String msg) {
		try {
			msg = URLDecoder.decode(msg, ChangeCharSet.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HandleMessage.getInstance().handleStringMsg(msg);
	}
}
