package com.example.shouhuantest;

public class SleepBean {

	private int type;
	private long min;

	public int getType() {  
		
		// 2-10ǳ˯��  0-2 ��˯�� 10���� û˯
		if (type <= 2 && type >= 0) {
			return 3;
		} else if (type <= 10 && type > 2) {
			return 2;
		} else if (type > 10) {
			return 1;
		}else {
			return 1;
		}
	}

	public void setType(int type) {

		this.type = type;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

}
