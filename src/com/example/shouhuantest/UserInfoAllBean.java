package com.example.shouhuantest;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfoAllBean implements Serializable{
     private ArrayList<UserMainBean> beans;

	public ArrayList<UserMainBean> getBeans() {
		return beans;
	}

	public void setBeans(ArrayList<UserMainBean> beans) {
		this.beans = beans;
	}	

}
