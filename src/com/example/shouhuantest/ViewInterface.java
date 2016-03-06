package com.example.shouhuantest;

import android.view.View;

public interface ViewInterface {
	void onCreate();

	void onResume();

	void stop();

	View getView();

	void displayData(byte[] b);

	void conncet();

	void disconncet();

}
