package com.example.shouhuantest;

import java.io.Serializable;
import java.util.ArrayList;

public class BraceleteMainBean implements Serializable{
	
	private ArrayList<BraceletBean> braceletBeans;
	public ArrayList<BraceletBean> getBraceletBeans() {
		return braceletBeans;
	}
	public void setBraceletBeans(ArrayList<BraceletBean> braceletBeans) {
		this.braceletBeans = braceletBeans;
	}

}
