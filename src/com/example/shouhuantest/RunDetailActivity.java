package com.example.shouhuantest;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RunDetailActivity implements ViewInterface {
	private ImageView back;
	private AnimationDrawable animationDrawable, animationDrawable2;
	private View view;
	private Activity context;
	private LinearLayout baseLayout;
	private int totleRun, totleDistance, totalHot;
	private TextView runTextView, hotTextView, distanceTextView,
			allRunTextView, allHotTextView, allDistanceTextView;
	private BraceletBean braceletBean;
	private BraceleteMainBean braceleteMainBean;
	private DatePicker runDatePicker;
	private Button conncetButton;
	int id;

	public RunDetailActivity(Activity context, LinearLayout baseLayout) {
		this.context = context;
		this.baseLayout = baseLayout;
		onCreate();

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		view = context.getLayoutInflater().inflate(R.layout.act_run, null);
		back = (ImageView) view.findViewById(R.id.blue_dowm);
		runTextView = (TextView) view.findViewById(R.id.text_runnum);
		hotTextView = (TextView) view.findViewById(R.id.text_hotnum);
		distanceTextView = (TextView) view.findViewById(R.id.text_distance);
		allRunTextView = (TextView) view.findViewById(R.id.text_allrun);
		allDistanceTextView = (TextView) view.findViewById(R.id.text_allkilo);
		allHotTextView = (TextView) view.findViewById(R.id.text_allkll);
		runDatePicker = (DatePicker) view.findViewById(R.id.datePicker1);
		conncetButton = (Button) view.findViewById(R.id.conncet);
		animationDrawable = (AnimationDrawable) back.getBackground();
		animationDrawable.start();
		animationDrawable2 = (AnimationDrawable) conncetButton.getBackground();
		animationDrawable2.start();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MainActivity.class);
				intent.putExtra("isclose", true);
				context.startActivity(intent);
			}
		});
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		runDatePicker.init(year, monthOfYear, dayOfMonth,
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						// DatePick监听
						if (braceletBean != null) {
							boolean isShowData = false;
							for (int i = 0; i < braceletBean.getBrArrayList()
									.size(); i++) {
								Calendar c = Calendar.getInstance();
								c.setTime(braceletBean.getBrArrayList().get(i)
										.getDate());
								System.out.println(c.get(Calendar.YEAR) + "年"
										+ c.get(Calendar.MONTH) + "月"
										+ c.get(Calendar.DAY_OF_MONTH) + "日");
								if (c.get(Calendar.YEAR) == year
										&& c.get(Calendar.MONTH) == monthOfYear
										&& c.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
									isShowData = true;
									int brancelet = braceletBean
											.getBrArrayList().get(i)
											.getBracelet();
									int distance = braceletBean
											.getBrArrayList().get(i)
											.getDistance();
									int hot = braceletBean.getBrArrayList()
											.get(i).getHot();
									runTextView.setText(brancelet + "");
									hotTextView.setText(hot + "    千卡");
									distanceTextView
											.setText(distance + "    米");
								}
							}
							if (!isShowData) {
								runTextView.setText("--");
								hotTextView.setText("--");
								distanceTextView.setText("--");
							}
						} else {
							runTextView.setText("--");
							hotTextView.setText("--");
							distanceTextView.setText("--");
						}

					}
				});
		braceleteMainBean = (BraceleteMainBean) ACache.get(context)
				.getAsObject("braceletmain");
		String idString = ACache.get(context).getAsString("id");
	
		if (idString != null) {
			id = Integer.parseInt(idString);
		}
		if (braceleteMainBean != null) {
			if (braceleteMainBean.getBraceletBeans().size() > 0) {

			}
			for (int i = 0; i < braceleteMainBean.getBraceletBeans().size(); i++) {
				if (id == braceleteMainBean.getBraceletBeans().get(i).getId()) {
					braceletBean = braceleteMainBean.getBraceletBeans().get(i);
				}
			}
		}
		// braceletBean = (BraceletBean) ACache.get(context).getAsObject(
		// "bracelet");
		if (braceletBean != null) {
			for (int i = 0; i < braceletBean.getBrArrayList().size(); i++) {
				Calendar c = Calendar.getInstance();
				c.setTime(braceletBean.getBrArrayList().get(i).getDate());
				if (c.get(Calendar.YEAR) == year
						&& c.get(Calendar.MONTH) == monthOfYear
						&& c.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
					int brancelet = braceletBean.getBrArrayList().get(i)
							.getBracelet();
					int distance = braceletBean.getBrArrayList().get(i)
							.getDistance();
					int hot = braceletBean.getBrArrayList().get(i).getHot();
					runTextView.setText(brancelet + "");
					hotTextView.setText(hot + "    千卡");
					distanceTextView.setText(distance + "    米");
					int allRun = braceletBean.getAllDistance() + brancelet;
					int allDistance = braceletBean.getAllDistance() + distance;
					int allHot = braceletBean.getAllHot() + hot;
					allRunTextView.setText(allRun + "");
					allHotTextView.setText(allHot + "");
					allDistanceTextView.setText(allDistance + "");
				}
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public void displayData(byte[] b) {
		// TODO Auto-generated method stub
		if (LudeUtiles.receiveValues(b) != null) {
			if ((0xff & b[0]) == 0x82) {
				int run = LudeUtiles.getLowAddHighByte(b[1], b[2]);
				int hot = LudeUtiles.getLowAddHighByte(b[3], b[4]);
				int distance = LudeUtiles.getLowAddHighByte(b[5], b[6]);
				runTextView.setText(run + "");
				hotTextView.setText(hot + "    千卡");
				distanceTextView.setText(distance + "    米");
				addValue(run, hot, distance);
				if (braceletBean != null) {
					int allRun = braceletBean.getAllDistance() + run;
					int allDistance = braceletBean.getAllDistance() + distance;
					int allHot = braceletBean.getAllHot() + hot;
					allRunTextView.setText(allRun + "");
					allHotTextView.setText(allHot + "");
					allDistanceTextView.setText(allDistance + "");
				}
			}

		}

	}

	private void addValue(int run, int hot, int distance) {
		// BraceletBean braceletBean = (BraceletBean) ACache.get(context)
		// .getAsObject("bracelet");
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		if (braceleteMainBean != null) {
			if (braceletBean != null) {
				ArrayList<BraceletDetailBean> brArrayList = braceletBean
						.getBrArrayList();

				if (brArrayList != null && brArrayList.size() > 0) {

					if (brArrayList.get(brArrayList.size() - 1).getDate()
							.getDay() == date.getDay()) {
						brArrayList.get(brArrayList.size() - 1)
								.setBracelet(run);
						brArrayList.get(brArrayList.size() - 1).setHot(hot);
						brArrayList.get(brArrayList.size() - 1).setDistance(
								distance);
					} else {
						braceletBean.setAllDistance(braceletBean
								.getAllDistance()
								+ brArrayList.get(brArrayList.size() - 1)
										.getDistance());
						braceletBean.setAllHot(braceletBean.getAllHot()
								+ brArrayList.get(brArrayList.size() - 1)
										.getHot());
						braceletBean.setAllRun(braceletBean.getAllRun()
								+ brArrayList.get(brArrayList.size() - 1)
										.getBracelet());
						BraceletDetailBean braceletDetailBean = new BraceletDetailBean();
						braceletDetailBean.setBracelet(run);
						braceletDetailBean.setHot(hot);
						braceletDetailBean.setDistance(distance);
						braceletDetailBean.setDate(date);
						brArrayList.add(braceletDetailBean);

					}
					braceletBean.setBrArrayList(brArrayList);
				} else {
					brArrayList = new ArrayList<BraceletDetailBean>();
					BraceletDetailBean braceletDetailBean = new BraceletDetailBean();
					braceletDetailBean.setBracelet(run);
					braceletDetailBean.setHot(hot);
					braceletDetailBean.setDistance(distance);
					braceletDetailBean.setDate(date);
					brArrayList.add(braceletDetailBean);
					braceletBean.setId(id);
					braceletBean.setBrArrayList(brArrayList);
				}
			} else {
				braceletBean = new BraceletBean();
				ArrayList<BraceletDetailBean> brArrayList = new ArrayList<BraceletDetailBean>();
				BraceletDetailBean braceletDetailBean = new BraceletDetailBean();
				braceletDetailBean.setBracelet(run);
				braceletDetailBean.setHot(hot);
				braceletDetailBean.setDistance(distance);
				braceletDetailBean.setDate(date);
				brArrayList.add(braceletDetailBean);
				braceletBean.setId(id);
				braceletBean.setBrArrayList(brArrayList);
			}
			ArrayList<BraceletBean> braceletBeans = braceleteMainBean
					.getBraceletBeans();
			boolean isIN = false;
			if (braceletBeans != null) {
				for (int i = 0; i < braceletBeans.size(); i++) {
					if (braceletBean.getId() == braceletBeans.get(i).getId()) {
						braceletBeans.set(i, braceletBean);
						isIN = true;
						braceleteMainBean.setBraceletBeans(braceletBeans);
					}
				}
				if (!isIN) {
					braceletBeans.add(braceletBean);
					braceleteMainBean.setBraceletBeans(braceletBeans);
				}
			} else {
				braceletBeans = new ArrayList<BraceletBean>();
				braceletBeans.add(braceletBean);
				braceleteMainBean.setBraceletBeans(braceletBeans);
			}

		} else {
			braceleteMainBean = new BraceleteMainBean();
			braceletBean = new BraceletBean();
			ArrayList<BraceletDetailBean> brArrayList = new ArrayList<BraceletDetailBean>();
			BraceletDetailBean braceletDetailBean = new BraceletDetailBean();
			braceletDetailBean.setBracelet(run);
			braceletDetailBean.setHot(hot);
			braceletDetailBean.setDistance(distance);
			braceletDetailBean.setDate(date);
			brArrayList.add(braceletDetailBean);
			braceletBean.setId(id);
			braceletBean.setBrArrayList(brArrayList);
			ArrayList<BraceletBean> braceletBeans = new ArrayList<BraceletBean>();
			braceletBeans.add(braceletBean);
			braceleteMainBean.setBraceletBeans(braceletBeans);
		}

		// 存储数据
		ACache.get(context).put("braceletmain", braceleteMainBean);
	}

	@Override
	public void conncet() {
		// TODO Auto-generated method stub
		conncetButton.setBackgroundResource(R.drawable.shebeiyilianjie);

	}

	@Override
	public void disconncet() {
		// TODO Auto-generated method stub
		conncetButton.setBackgroundResource(R.anim.ble_anim);
		animationDrawable2 = (AnimationDrawable) conncetButton.getBackground();
		animationDrawable2.start();
	}
}
