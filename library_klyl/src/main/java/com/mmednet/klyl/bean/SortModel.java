package com.mmednet.klyl.bean;

import java.io.Serializable;

public class SortModel implements Serializable {

	private String name;   
	private String sortLetters; 
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
