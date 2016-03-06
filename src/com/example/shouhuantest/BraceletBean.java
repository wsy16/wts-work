package com.example.shouhuantest;

import java.io.Serializable;
import java.util.ArrayList;

public class BraceletBean implements Serializable {
	private ArrayList<BraceletDetailBean> brArrayList;
    private int  allRun;
    private int  allHot;
    private int  allDistance;
    private int id;
	public ArrayList<BraceletDetailBean> getBrArrayList() {
		return brArrayList;
	}

	public void setBrArrayList(ArrayList<BraceletDetailBean> brArrayList) {
		this.brArrayList = brArrayList;
	}

	public int getAllRun() {
		return allRun;
	}

	public void setAllRun(int allRun) {
		this.allRun = allRun;
	}

	public int getAllHot() {
		return allHot;
	}

	public void setAllHot(int allHot) {
		this.allHot = allHot;
	}

	public int getAllDistance() {
		return allDistance;
	}

	public void setAllDistance(int allDistance) {
		this.allDistance = allDistance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
