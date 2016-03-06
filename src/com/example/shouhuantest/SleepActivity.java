package com.example.shouhuantest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;

public class SleepActivity implements ViewInterface {

	private ImageView back;
	private AnimationDrawable animationDrawable, animationDrawable2;
	private View view;
	private BaseBleActivity context;
	private LinearLayout baseLayout;
	private SleepSendCallBack sleepSendCallBack;
	private LinearLayout layout;
	private ArrayList<SleepBean> arrayList;
	private final long tenmin = 10 * 60 * 1000;
	private Button conncetButton;
	private SleepListBean sleepListBean;
	private boolean isadd = false;
	private TextView hourTextView, minTextView, shenTextView, qianTextView;
	private DatePicker runDatePicker;
	private TextView conncetTimeTextView;
	private int id;
    private TextView sleepTextView;
	public SleepActivity(BaseBleActivity context, LinearLayout baseLayout,
			SleepSendCallBack sleepSendCallBack) {
		arrayList = new ArrayList<SleepBean>();
		this.context = context;
		this.baseLayout = baseLayout;
		this.sleepSendCallBack = sleepSendCallBack;
		onCreate();

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		view = context.getLayoutInflater().inflate(R.layout.act_sleep, null);
		back = (ImageView) view.findViewById(R.id.blue_dowm);
		layout = (LinearLayout) view.findViewById(R.id.lin_sleep_tu);
		hourTextView = (TextView) view.findViewById(R.id.text_hour);
		minTextView = (TextView) view.findViewById(R.id.text_min);
		shenTextView = (TextView) view.findViewById(R.id.text_shensleep);
		qianTextView = (TextView) view.findViewById(R.id.text_qiansleep);
		conncetButton = (Button) view.findViewById(R.id.conncet);
		sleepTextView=(TextView)view.findViewById(R.id.sleep_time);
		runDatePicker = (DatePicker) view.findViewById(R.id.datePicker1);
		conncetTimeTextView = (TextView) view.findViewById(R.id.conncet_time);
		animationDrawable = (AnimationDrawable) back.getBackground();
		animationDrawable.start();
		animationDrawable2 = (AnimationDrawable) conncetButton.getBackground();
		animationDrawable2.start();
		String idString = ACache.get(context).getAsString("id");

		if (idString != null) {
			id = Integer.parseInt(idString);
		}
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
						boolean isShowData = false;
						conncetTimeTextView.setText("同步日期：" + year + "_"
								+ (monthOfYear + 1) + "_" + dayOfMonth);
						if (sleepListBean != null) {
							for (int i = 0; i < sleepListBean.getLogicBeans()
									.size(); i++) {
								Calendar c = Calendar.getInstance();
								c.setTime(sleepListBean.getLogicBeans().get(i)
										.getDate());
								System.out.println(c.get(Calendar.YEAR) + "年"
										+ c.get(Calendar.MONTH) + "月"
										+ c.get(Calendar.DAY_OF_MONTH) + "日");
								if (c.get(Calendar.YEAR) == year
										&& c.get(Calendar.MONTH) == monthOfYear
										&& c.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
									isShowData = true;
									String shenString = sleepListBean
											.getLogicBeans().get(i)
											.getShenString();
									String qianString = sleepListBean
											.getLogicBeans().get(i)
											.getQianString();
									long allshi = sleepListBean.getLogicBeans()
											.get(i).getAllshi();
									long allfen = sleepListBean.getLogicBeans()
											.get(i).getAllfen();
									shenTextView.setText(shenString);
									qianTextView.setText(qianString);
									hourTextView.setText(allshi + "");
									minTextView.setText(allfen + "");

								}
							}
							if (!isShowData) {
								shenTextView.setText("_小时_分");
								qianTextView.setText("_小时_分");
								hourTextView.setText("_");
								minTextView.setText("_");
							}
						} else {
							shenTextView.setText("_小时_分");
							qianTextView.setText("_小时_分");
							hourTextView.setText("_");
							minTextView.setText("_");
						}
					}
				});
		sleepListBean = (SleepListBean) ACache.get(context).getAsObject(
				"sleep" + id);
		if (sleepListBean != null) {
			for (int i = 0; i < sleepListBean.getLogicBeans().size(); i++) {
				Calendar c = Calendar.getInstance();
				c.setTime(sleepListBean.getLogicBeans().get(i).getDate());
				System.out.println(c.get(Calendar.YEAR) + "年"
						+ c.get(Calendar.MONTH) + "月"
						+ c.get(Calendar.DAY_OF_MONTH) + "日");
				if (c.get(Calendar.YEAR) == year
						&& c.get(Calendar.MONTH) == monthOfYear
						&& c.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
					String shenString = sleepListBean.getLogicBeans().get(i)
							.getShenString();
					String qianString = sleepListBean.getLogicBeans().get(i)
							.getQianString();
					long allshi = sleepListBean.getLogicBeans().get(i)
							.getAllshi();
					long allfen = sleepListBean.getLogicBeans().get(i)
							.getAllfen();
					shenTextView.setText(shenString);
					qianTextView.setText(qianString);
					hourTextView.setText(allshi + "");
					minTextView.setText(allfen + "");

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

	@SuppressWarnings("deprecation")
	@Override
	public void displayData(byte[] b) {
		// TODO Auto-generated method stub
		if (LudeUtiles.receiveValues(b) != null) {
			if ((0xff & b[0]) == 0x87) {

				int nian = (0xff & b[1]) + 2000;
				int yue = (0xff & b[2]+1);
				int ri = (0xff & b[3]+1);
				int shi = (0xff & b[4]);
				int fen = (0xff & b[3]);
//				Calendar calendar = Calendar.getInstance();
//				calendar.set(nian, yue, ri, shi, fen);
//                sleepTextView.setText(yue+"月"+ri+"日"+shi+"时"+fen+"分");
				context.sendMsg(LudeUtiles
						.sendValues(SendMessage.SH_SLEEP_DATA));

			}
			if ((0xff & b[0]) == 0x88) {
				if ((0xff & b[1]) == 0xff) {
					if (!isadd) {
						addView(layout.getWidth());
					}

				} else {
					for (int i = 2; i < b.length - 1; i++) {

						// if (i == 1) {
						SleepBean sleepBean = new SleepBean();
						// 修改深睡眠浅睡眠格式
						sleepBean.setType(0xff & b[i]);
						sleepBean.setMin(tenmin);
						arrayList.add(sleepBean);
					}
					sleepSendCallBack.callbackSuccess();
				}

			}
		}

	}

	private void addView(int w) {
		// long allMin = 0;
		long allsleep = 0;
		isadd = true;
		long shenSleep = 0;
		long qianSleep = 0;
		// for (int i = 0; i < arrayList.size(); i++) {
		// allMin=allMin+arrayList.get(i).getMin();
		// }
//		int childw = w / arrayList.size();

		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).getType() == 1) {
//				LinearLayout childlayout = new LinearLayout(context);
//				childlayout.setBackgroundResource(R.color.light_blue);
//				LayoutParams param1 = new LayoutParams(childw,
//						LayoutParams.MATCH_PARENT);
//				layout.addView(childlayout, param1);
			}
			if (arrayList.get(i).getType() == 2) {
//				LinearLayout childlayout = new LinearLayout(context);
//				childlayout.setBackgroundResource(R.color.dark_orange);
//				LayoutParams param1 = new LayoutParams(childw,
//						LayoutParams.MATCH_PARENT);
//				layout.addView(childlayout, param1);
				allsleep = allsleep + arrayList.get(i).getMin();
				qianSleep = qianSleep + arrayList.get(i).getMin();
			} else if (arrayList.get(i).getType() == 3) {
//				LinearLayout childlayout = new LinearLayout(context);
//				childlayout.setBackgroundResource(R.color.dark_blue);
//				LayoutParams param1 = new LayoutParams(childw,
//						LayoutParams.MATCH_PARENT);
//				layout.addView(childlayout, param1);
				allsleep = allsleep + arrayList.get(i).getMin();
				shenSleep = shenSleep + arrayList.get(i).getMin();

			}

		}
		if (allsleep != 0) {
			long qianhour = qianSleep / (3600 * 1000);
			qianSleep = qianSleep % (3600 * 1000);
			long shenhour = shenSleep / (3600 * 1000);
			shenSleep = shenSleep % (3600 * 1000);
			long qianmin = qianSleep / (60 * 1000);
			long shenmin = shenSleep / (60 * 1000);
			String qianString = qianhour + "时" + qianmin + "分";
			String shenString = shenhour + "时" + shenmin + "分";
			long allshi = allsleep / (3600 * 1000);
			allsleep = allsleep % (3600 * 1000);
			long allfen = allsleep / (60 * 1000);
			shenTextView.setText(shenString);
			qianTextView.setText(qianString);
			hourTextView.setText(allshi + "");
			minTextView.setText(allfen + "");
			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			conncetTimeTextView
					.setText("同步日期：" + simpleDateFormat.format(date));
			SleepLogicBean sleepLogicBean = new SleepLogicBean();
			sleepLogicBean.setAllfen(allfen);
			sleepLogicBean.setAllshi(allshi);
			sleepLogicBean.setDate(date);
			sleepLogicBean.setQianString(qianString);
			sleepLogicBean.setShenString(shenString);
			if (sleepListBean == null) {
				sleepListBean = new SleepListBean();
				ArrayList<SleepLogicBean> arrayList = new ArrayList<SleepLogicBean>();
				arrayList.add(sleepLogicBean);
				sleepListBean.setLogicBeans(arrayList);
				sleepListBean.setId(id);
				// 存储数据
				ACache.get(context).put("sleep" + id, sleepListBean);
			} else {
				ArrayList<SleepLogicBean> arrayList = sleepListBean
						.getLogicBeans();
				arrayList.add(sleepLogicBean);
				sleepListBean.setLogicBeans(arrayList);
				sleepListBean.setId(id);
				ACache.get(context).put("sleep" + id, sleepListBean);

			}

		}

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
