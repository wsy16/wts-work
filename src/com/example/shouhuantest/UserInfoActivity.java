package com.example.shouhuantest;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class UserInfoActivity extends Activity {
	private Button saveButton;

	private EditText userNameEditText, yearEditText, heightEditText,
			weightEditText;
	private UserInfoAllBean allBeans;
	private RadioButton manRadioButton, womanRadioButton;
	private int id;
	private UserMainBean mainBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_userinfo);
		id = getIntent().getIntExtra("userID", 1);
		userNameEditText = (EditText) findViewById(R.id.edit_username);
		yearEditText = (EditText) findViewById(R.id.edit_year);
		heightEditText = (EditText) findViewById(R.id.edit_height);
		weightEditText = (EditText) findViewById(R.id.edit_weight);
		saveButton = (Button) findViewById(R.id.btn_save);
		manRadioButton = (RadioButton) findViewById(R.id.radioMan);
		womanRadioButton = (RadioButton) findViewById(R.id.radioWomen);
		allBeans = (UserInfoAllBean) ACache.get(UserInfoActivity.this)
				.getAsObject("userinfo");
		if (allBeans != null) {
			for (int i = 0; i < allBeans.getBeans().size(); i++) {
				if (allBeans.getBeans().get(i).getId() == id) {
					mainBean = allBeans.getBeans().get(i);
				}
			}
		}
		if (mainBean != null) {
			userNameEditText.setText(mainBean.getUserName());
			yearEditText.setText(mainBean.getYear());
			heightEditText.setText(mainBean.getHeight());
			weightEditText.setText(mainBean.getWeight());
			if (mainBean.getSex().equals("男")) {
				manRadioButton.setChecked(true);
			}else {
				womanRadioButton.setChecked(true);
			}
		}
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 存储数据
				if (isNullEdit(userNameEditText) && isNullEdit(heightEditText)
						&& isNullEdit(weightEditText)
						&& isNullEdit(yearEditText)) {

					String userName = userNameEditText.getText().toString();
					String year = yearEditText.getText().toString();
					String sex = null;
					if (manRadioButton.isChecked()) {
						sex = "男";
					} else if (womanRadioButton.isChecked()) {
						sex = "女";
					}
					String w = weightEditText.getText().toString();
					String h = heightEditText.getText().toString();

					if (allBeans != null) {
						if (mainBean != null) {
							mainBean.setHeight(h);
							mainBean.setWeight(w);
							mainBean.setId(id);
							mainBean.setSex(sex);
							mainBean.setUserName(userName);
							mainBean.setYear(year);
							for (int i = 0; i < allBeans.getBeans().size(); i++) {
								if (allBeans.getBeans().get(i).getId() == mainBean
										.getId()) {
									allBeans.getBeans().set(i, mainBean);
								}
							}
						} else {
							mainBean = new UserMainBean();
							mainBean.setHeight(h);
							mainBean.setWeight(w);
							mainBean.setId(id);
							mainBean.setSex(sex);
							mainBean.setUserName(userName);
							mainBean.setYear(year);
							allBeans.getBeans().add(mainBean);
						}
					
						ACache.get(UserInfoActivity.this).put("userinfo",
								allBeans);
					}else {
						allBeans=new UserInfoAllBean();
						mainBean=new UserMainBean();
						mainBean.setHeight(h);
						mainBean.setWeight(w);
						mainBean.setId(id);
						mainBean.setSex(sex);
						mainBean.setUserName(userName);
						mainBean.setYear(year);
						ArrayList<UserMainBean> beans=new ArrayList<UserMainBean>();
						beans.add(mainBean);
						allBeans.setBeans(beans);
						ACache.get(UserInfoActivity.this).put("userinfo",
								allBeans);
					}
					ACache.get(UserInfoActivity.this).put("id",id+"");
					Toast.makeText(UserInfoActivity.this, "设置完成",
							Toast.LENGTH_SHORT).show();
					finish();
					// ACache.get(UserInfoActivity.this).put("bracelet",
					// braceletBean);
				} else {
					Toast.makeText(UserInfoActivity.this, "请输入完整信息",
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});

	}

	private boolean isNullEdit(EditText editText) {
		if (editText.getText().toString() != null
				&& editText.getText().toString().length() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (allBeans==null) {
				Toast.makeText(UserInfoActivity.this, "请输入完整信息",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
