package com.mmednet.klyl.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * socket 发送过来的文件类型的 bean
 * 
 * @author xiaowei
 * 
 */
public class FileMsgBean implements Serializable{

	private String fileName;// 文件名
	private long fileSize;// 文件大小
	private int customType;// 语音类型
	private String question;// 问题
	private Gson gson = new Gson();

	private class Bean {
		private String name;
		private String identification;

		public Bean(String name, String identification) {
			this.name = name;
			this.identification = identification;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIdentification() {
			return identification;
		}

		public void setIdentification(String identification) {
			this.identification = identification;
		}

	}

	public FileMsgBean(String fileName, long fileSize, int customType,
			String name, String id) {
		String question = gson.toJson(new Bean(name, id));
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.customType = customType;
		this.question = question;
	}

	public int getCustomType() {
		return customType;
	}

	public void setCustomType(int customType) {
		this.customType = customType;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public FileMsgBean() {
		super();
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
