package com.example.shouhuantest;

import java.io.Serializable;
import java.util.ArrayList;

public class XinlvBean  implements Serializable {
	private int id;
	private ArrayList<Integer> xinlv;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Integer> getXinlv() {
		return xinlv;
	}

	public void setXinlv(ArrayList<Integer> xinlv) {
		this.xinlv = xinlv;
	}

}
