package com.example.shouhuantest;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

public class BraceletDetailBean implements Serializable {
	private int bracelet;
	private int hot;
	private int distance;
	private Date date;

	public int getBracelet() {
		return bracelet;
	}

	public void setBracelet(int bracelet) {
		this.bracelet = bracelet;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
