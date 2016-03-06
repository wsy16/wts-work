package com.example.shouhuantest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class XinLvActivity implements ViewInterface {

	private ImageView back;
	private AnimationDrawable animationDrawable, animationDrawable2;;
	private View view;
	private BaseBleActivity context;
	private LinearLayout baseLayout;
	private TextView xinlvTextView;
	private RelativeLayout relativeLayout;
	private ImageView xinlvLineView;
	private ImageView xinlvYuan;
	private boolean isremove = false;
	private Button button;
	private XYMultipleSeriesRenderer renderer;
	XYMultipleSeriesDataset dataset;
	// private ArrayList<Integer> allbyte;
	private GraphicalView chart;
	private int xinlv;
	private LinearLayout chartLayout;
	private Button conncetButton;
	private TextView conncetTimeTextView;
	XYSeries series;
	private int maxSize = 50;
	private ArrayList<Integer> xinlvIArrayList;
	private Button xinlvHighOkButton;
	private EditText xinlvHighEditText;

	public XinLvActivity(BaseBleActivity context, LinearLayout baseLayout) {
		this.context = context;
		this.baseLayout = baseLayout;
		XinlvMainBean xinlvMainBean = (XinlvMainBean) ACache.get(context)
				.getAsObject("xinlvbean");
		String idString = ACache.get(context).getAsString("id");
		int id = 0;
		if (idString != null) {
			id = Integer.parseInt(idString);
		}
		if (xinlvMainBean != null) {
			if (xinlvMainBean.getXinlvBeans() != null) {
				for (int i = 0; i < xinlvMainBean.getXinlvBeans().size(); i++) {
					if (xinlvMainBean.getXinlvBeans().get(i).getId() == id) {
						xinlvIArrayList = xinlvMainBean.getXinlvBeans().get(i)
								.getXinlv();
					}
				}
			}

		}
		onCreate();

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		view = context.getLayoutInflater().inflate(R.layout.act_xinlv, null);
		back = (ImageView) view.findViewById(R.id.blue_dowm);
		xinlvTextView = (TextView) view.findViewById(R.id.text_xinlv);
		xinlvLineView = (ImageView) view.findViewById(R.id.xinlv_line);
		xinlvHighOkButton = (Button) view.findViewById(R.id.ok);
		xinlvHighEditText = (EditText) view.findViewById(R.id.edit_xinlvhigh);
		relativeLayout = (RelativeLayout) view
				.findViewById(R.id.rel_xinlv_line);
		animationDrawable = (AnimationDrawable) back.getBackground();
		animationDrawable.start();
		
		xinlvYuan = new ImageView(context);

		conncetTimeTextView = (TextView) view.findViewById(R.id.conncet_time);
		conncetButton = (Button) view.findViewById(R.id.conncet);
		animationDrawable2 = (AnimationDrawable) conncetButton.getBackground();
		animationDrawable2.start();
		button = (Button) view.findViewById(R.id.save);
		chartLayout = (LinearLayout) view.findViewById(R.id.chart_view);

		String xinlvHigh = ACache.get(context).getAsString(
				"xinlvhigh");
		if (xinlvHigh != null) {
			xinlvHighEditText.setHint("当前值" + xinlvHigh);
		}
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (xinlv != 0) {
					saveXinlvDate(xinlv);
					Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
				}

				// context.sendMsg(LudeUtiles.sendValues(SendMessage.SH_SHAKE));
			}
		});
		xinlvHighOkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (xinlvHighEditText.getText().toString() != null
						&& xinlvHighEditText.getText().toString().length() > 0) {
					String xinlvHighString = xinlvHighEditText.getText()
							.toString();
					ACache.get(context).put("xinlvhigh", xinlvHighString);
					context.sendUserInfo();
					Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
				}
			}
		});
		xinlvYuan.setImageResource(R.drawable.xinlv_yuan);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MainActivity.class);
				intent.putExtra("isclose", true);
				context.startActivity(intent);
			}
		});

		// if (xinlvIArrayList != null) {
		// chartLayout.addView(addAChartXinLvView(xinlvIArrayList));
		// }

		series = new XYSeries("");

		// 创建一个数据集的实例，这个数据集将被用来创建图表
		dataset = new XYMultipleSeriesDataset();

		// 将点集添加到这个数据集中
		dataset.addSeries(series);

		// 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
		int color = context.getResources().getColor(R.color.black);
		PointStyle style = PointStyle.CIRCLE;
		renderer = buildRenderer(color, style, true);

		// 设置好图表的样式
		setChartSettingsXinlv(renderer, "心率曲线图", "次数", "心率", 0, 10, 0, 200 + 5,
				context.getResources().getColor(R.color.dark_blue), Color.BLACK);
		// 生成图表
		chart = ChartFactory.getLineChartView(context, dataset, renderer);
		chartLayout.addView(chart);
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
			if ((0xff & b[0]) == 0x83) {
				xinlv = 0xff & b[1];
				xinlvTextView.setText(xinlv + "");
				Calendar c = Calendar.getInstance();
				Date date = c.getTime();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");
				conncetTimeTextView.setText("同步日期："
						+ simpleDateFormat.format(date));
				if (xinlv > 40 && xinlv < 200) {
					if (isremove) {
						relativeLayout.removeView(xinlvYuan);
					}
					isremove = true;
					int w = xinlvLineView.getWidth();
					int marginleft = ((xinlv - 40) * w) / 160;
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					// layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
					layoutParams.setMargins(marginleft, 0, 0, 0);
					relativeLayout.addView(xinlvYuan, layoutParams);
					updateChart(xinlv);
				}
				saveXinlvDate(xinlv);

			}

		}
	}

	private void saveXinlvDate(int xinlv) {

		// 获取用户ID;
		String idString = ACache.get(context).getAsString("id");
		int id = 0;
		if (idString != null) {
			id = Integer.parseInt(idString);
		}
		XinlvMainBean xinlvmainBean = (XinlvMainBean) ACache.get(context)
				.getAsObject("xinlvbean");
		if (xinlvmainBean == null) {
			xinlvmainBean = new XinlvMainBean();
			XinlvBean xinlvBean = new XinlvBean();

			xinlvBean.setId(id);
			ArrayList<Integer> arrayList = new ArrayList<Integer>();
			arrayList.add(xinlv);
			xinlvBean.setXinlv(arrayList);
			ArrayList<XinlvBean> xinlvBeans = new ArrayList<XinlvBean>();
			xinlvBeans.add(xinlvBean);
			xinlvmainBean.setXinlvBeans(xinlvBeans);
			ACache.get(context).put("xinlvbean", xinlvmainBean);
		} else {
			if (xinlvmainBean.getXinlvBeans() != null
					&& xinlvmainBean.getXinlvBeans().size() > 0) {
				ArrayList<XinlvBean> xinlvBeans = xinlvmainBean.getXinlvBeans();
				boolean isIDs = false;
				for (int i = 0; i < xinlvBeans.size(); i++) {
					if (xinlvBeans.get(i).getId() == id) {
						isIDs = true;
						XinlvBean xinlvBean = xinlvBeans.get(i);
						if (xinlvBean.getXinlv() != null
								&& xinlvBean.getXinlv().size() > 0) {
							ArrayList<Integer> xinlvs = xinlvBean.getXinlv();
							xinlvs.add(xinlv);
							xinlvBean.setXinlv(xinlvs);

						} else {
							ArrayList<Integer> xinlvs = new ArrayList<Integer>();
							xinlvs.add(xinlv);
							xinlvBean.setXinlv(xinlvs);
						}
						xinlvBeans.set(i, xinlvBean);
						break;
					}
				}
				if (!isIDs) {
					XinlvBean xinlvBean = new XinlvBean();
					xinlvBean.setId(id);
					ArrayList<Integer> aIntegers = new ArrayList<Integer>();
					aIntegers.add(xinlv);
					xinlvBean.setXinlv(aIntegers);
					xinlvBeans.add(xinlvBean);
				}
				xinlvmainBean.setXinlvBeans(xinlvBeans);
				ACache.get(context).put("xinlvbean", xinlvmainBean);
			} else {
				ArrayList<XinlvBean> xinlvBeans = new ArrayList<XinlvBean>();
				XinlvBean xinlvBean = new XinlvBean();
				xinlvBean.setId(id);
				ArrayList<Integer> xinlvIntegers = new ArrayList<Integer>();
				xinlvIntegers.add(xinlv);
				xinlvBean.setXinlv(xinlvIntegers);
				xinlvBeans.add(xinlvBean);
				xinlvmainBean.setXinlvBeans(xinlvBeans);

				ACache.get(context).put("xinlvbean", xinlvmainBean);
			}

		}

	}

	// private View addAChartXinLvView(ArrayList<Integer> brArrayList) {
	// String[] titles = new String[] { "手环数据" };
	// List<double[]> x = new ArrayList<double[]>();
	// List<double[]> y = new ArrayList<double[]>();
	// double[] xbyte = new double[brArrayList.size()];
	// double[] ybyte = new double[brArrayList.size()];
	// double ymax = 0.0;
	// for (int i = 0; i < brArrayList.size(); i++) {
	// xbyte[i] = (double) i + 1;
	// ybyte[i] = (double) brArrayList.get(i);
	// if (ymax < brArrayList.get(i)) {
	// ymax = brArrayList.get(i);
	// }
	// }
	// x.add(xbyte);
	// y.add(ybyte);
	// XYMultipleSeriesDataset dataset = buildDataset(titles, x, y);
	// int[] colors = new int[] { context.getResources().getColor(
	// R.color.black) };
	// PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
	// renderer = buildRenderer(colors, styles, true);
	// setChartSettingsXinlv(renderer, "心率曲线图", "次数", "心率",
	// getXmin(xbyte.length), getXmax(xbyte.length) + 1, 0, 200 + 5,
	// context.getResources().getColor(R.color.dark_blue), Color.BLACK);
	// chart = ChartFactory.getLineChartView(context, dataset, renderer);
	// return chart;
	// }

	private int getXmin(int x) {
		if (x > 5) {
			return x - 5;
		} else {
			return 0;
		}
	}

	private int getXmax(int x) {
		if (x < 5) {
			return 5;
		} else {
			return x;
		}
	}

	protected void setChartSettingsXinlv(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setChartTitleTextSize(40);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsTextSize(20);
		renderer.setMargins(new int[] { 50, 50, 0, 50 });
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setMarginsColor(Color.WHITE);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setPanEnabled(false, false);// 表盘移动
		renderer.setZoomEnabled(false, false);// 图表是否可以缩放
		// renderer.setPanEnabled(false, false);// 表盘缩放

		// renderer.setClickEnabled(true);// 是否可点击
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			r.setFillPoints(fill);
			r.setDisplayChartValues(true);
			r.setChartValuesSpacing(10);
			r.setChartValuesTextSize(25);
			renderer.addSeriesRenderer(r);

		}

		return renderer;
	}

	protected XYMultipleSeriesDataset buildDataset(String[] titles,
			List xValues, List yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length; // 有几条线
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i]); // 根据每条线的名称创建
			double[] xV = (double[]) xValues.get(i); // 获取第i条线的数据
			double[] yV = (double[]) yValues.get(i);
			int seriesLength = xV.length; // 有几个点
			for (int k = 0; k < seriesLength; k++) // 每条线里有几个点
			{
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
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

	protected XYMultipleSeriesRenderer buildRenderer(int color,
			PointStyle style, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		// 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(color);
		r.setPointStyle(style);
		r.setFillPoints(fill);
		r.setLineWidth(3);
		renderer.addSeriesRenderer(r);

		return renderer;
	}

	private void updateChart(int y) {

		// 移除数据集中旧的点集
		dataset.removeSeries(series);

		// 判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
		int length = series.getItemCount();
		if (length > maxSize) {
			length = maxSize;
		}
		// 设置好下一个需要增加的节点
		int addX = 0;
		int addY = y;
		double[] xV = new double[length];
		double[] yV = new double[length];
		// 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
		for (int i = 0; i < length; i++) {
			xV[i] = series.getX(i) + 1;
			yV[i] = series.getY(i);
		}

		// 点集先清空，为了做成新的点集而准备
		series.clear();

		// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
		// 这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
		series.add(addX, addY);
		for (int k = 0; k < length; k++) {
			series.add(xV[k], yV[k]);
		}

		// 在数据集中添加新的点集
		dataset.addSeries(series);

		// 视图更新，没有这一步，曲线不会呈现动态
		// 如果在非UI主线程中，需要调用postInvalidate()，具体参考api
		chart.invalidate();
	}
}
