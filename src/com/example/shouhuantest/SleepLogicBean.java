package com.example.shouhuantest;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class SleepLogicBean implements Serializable {
	private Date date;
	private String shenString;
	private String qianString;
	private long allshi;
	private long allfen;
	public String getShenString() {
		return shenString;
	}
	public void setShenString(String shenString) {
		this.shenString = shenString;
	}
	public String getQianString() {
		return qianString;
	}
	public void setQianString(String qianString) {
		this.qianString = qianString;
	}
	public long getAllshi() {
		return allshi;
	}
	public void setAllshi(long allshi) {
		this.allshi = allshi;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getAllfen() {
		return allfen;
	}
	public void setAllfen(long allfen) {
		this.allfen = allfen;
	}
	

}
