package com.example.shouhuantest;

import java.io.Serializable;
import java.util.ArrayList;

public class XinlvMainBean implements Serializable {
	
	private ArrayList<XinlvBean> xinlvBeans;

	public ArrayList<XinlvBean> getXinlvBeans() {
		return xinlvBeans;
	}

	public void setXinlvBeans(ArrayList<XinlvBean> xinlvBeans) {
		this.xinlvBeans = xinlvBeans;
	}

}
