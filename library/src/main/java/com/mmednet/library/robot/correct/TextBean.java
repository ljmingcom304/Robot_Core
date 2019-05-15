package com.mmednet.library.robot.correct;

import java.io.Serializable;
import java.util.ArrayList;

public class TextBean implements Serializable {

	private static final long serialVersionUID = 1026891709055026644L;

	private String key;						//校正后的结果词
	private ArrayList<String> values;		//需要被矫正的词

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

}
