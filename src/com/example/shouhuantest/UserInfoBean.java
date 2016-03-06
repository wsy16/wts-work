package com.example.shouhuantest;

import java.io.Serializable;
import java.util.Calendar;

public class UserInfoBean implements Serializable {

	private int bushu;
	private Calendar calendar;

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public int getBushu() {
		return bushu;
	}

	public void setBushu(int bushu) {
		this.bushu = bushu;
	}
}
