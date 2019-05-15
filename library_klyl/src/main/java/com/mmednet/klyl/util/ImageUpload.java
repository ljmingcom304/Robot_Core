package com.mmednet.klyl.util;

import android.util.Log;

import com.mmednet.klyl.constants.IPConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** 上传文件 */
public class ImageUpload extends Thread {

	private final static String TAG = "FileSendThread";
	private static String mFileName;
	private ServerSocket mServerSocket;
	private Socket mClientSocket;

	public ImageUpload(String fileName) {
		mFileName = fileName;
	}

	@Override
	public void run() {
		BufferedOutputStream bos = null;
		BufferedInputStream fbis = null;
		try {
			File sendFile = new File(mFileName); // 要发送的文件
			if (!sendFile.exists()) {
				Log.d(TAG, "file " + sendFile + " not exist!");
				return;
			}
			if (mServerSocket == null) {
				mServerSocket = new ServerSocket(IPConfig.FILE_PORT);
			}
			mClientSocket = mServerSocket.accept();
			Log.e(TAG, mServerSocket.getInetAddress()+":"+mServerSocket.getLocalPort());
			bos = new BufferedOutputStream(mClientSocket.getOutputStream());
			fbis = new BufferedInputStream(new FileInputStream(sendFile));
			int rlen = 0;
			byte[] readBuffer = new byte[1024];
			while ((rlen = fbis.read(readBuffer)) != -1) {
				bos.write(readBuffer, 0, rlen);
			}
			bos.flush();
			Log.d(TAG, "文件发送成功");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fbis != null) {
				try {
					fbis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fbis = null;
			}

			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos = null;
			}
			if (mClientSocket != null) {
				try {
					mClientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mClientSocket = null;
			}
			if (mServerSocket != null) {
				try {
					mServerSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mServerSocket = null;
			}
		}
	}
}
