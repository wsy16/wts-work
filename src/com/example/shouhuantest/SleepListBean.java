package com.example.shouhuantest;

import java.io.Serializable;
import java.util.ArrayList;

public class SleepListBean implements Serializable{
	private int id;
	private ArrayList<SleepLogicBean> logicBeans;

	public ArrayList<SleepLogicBean> getLogicBeans() {
		return logicBeans;
	}

	public void setLogicBeans(ArrayList<SleepLogicBean> logicBeans) {
		this.logicBeans = logicBeans;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
