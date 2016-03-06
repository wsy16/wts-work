package com.example.shouhuantest;

import java.io.Serializable;
import java.util.ArrayList;

public class UserMainBean  implements Serializable {
	private int id;
	private String userName;
	private String sex;
	private String year;
	private String height;
	private String  weight;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String burnyear) {
		this.year = burnyear;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
