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
			xinlvHighEditText.setHint("��ǰֵ" + xinlvHigh);
		}
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (xinlv != 0) {
					saveXinlvDate(xinlv);
					Toast.makeText(context, "����ɹ�", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(context, "���óɹ�", Toast.LENGTH_SHORT).show();
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

		// ����һ�����ݼ���ʵ����������ݼ�������������ͼ��
		dataset = new XYMultipleSeriesDataset();

		// ���㼯��ӵ�������ݼ���
		dataset.addSeries(series);

		// ���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��
		int color = context.getResources().getColor(R.color.black);
		PointStyle style = PointStyle.CIRCLE;
		renderer = buildRenderer(color, style, true);

		// ���ú�ͼ�����ʽ
		setChartSettingsXinlv(renderer, "��������ͼ", "����", "����", 0, 10, 0, 200 + 5,
				context.getResources().getColor(R.color.dark_blue), Color.BLACK);
		// ����ͼ��
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
				conncetTimeTextView.setText("ͬ�����ڣ�"
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

		// ��ȡ�û�ID;
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
	// String[] titles = new String[] { "�ֻ�����" };
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
	// setChartSettingsXinlv(renderer, "��������ͼ", "����", "����",
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
		renderer.setPanEnabled(false, false);// �����ƶ�
		renderer.setZoomEnabled(false, false);// ͼ���Ƿ��������
		// renderer.setPanEnabled(false, false);// ��������

		// renderer.setClickEnabled(true);// �Ƿ�ɵ��
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
		int length = titles.length; // �м�����
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i]); // ����ÿ���ߵ����ƴ���
			double[] xV = (double[]) xValues.get(i); // ��ȡ��i���ߵ�����
			double[] yV = (double[]) yValues.get(i);
			int seriesLength = xV.length; // �м�����
			for (int k = 0; k < seriesLength; k++) // ÿ�������м�����
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

		// ����ͼ�������߱������ʽ��������ɫ����Ĵ�С�Լ��ߵĴ�ϸ��
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(color);
		r.setPointStyle(style);
		r.setFillPoints(fill);
		r.setLineWidth(3);
		renderer.addSeriesRenderer(r);

		return renderer;
	}

	private void updateChart(int y) {

		// �Ƴ����ݼ��оɵĵ㼯
		dataset.removeSeries(series);

		// �жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������100�������Ե���������100ʱ��������Զ��100
		int length = series.getItemCount();
		if (length > maxSize) {
			length = maxSize;
		}
		// ���ú���һ����Ҫ���ӵĽڵ�
		int addX = 0;
		int addY = y;
		double[] xV = new double[length];
		double[] yV = new double[length];
		// ���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
		for (int i = 0; i < length; i++) {
			xV[i] = series.getX(i) + 1;
			yV[i] = series.getY(i);
		}

		// �㼯����գ�Ϊ�������µĵ㼯��׼��
		series.clear();

		// ���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
		// �����������һ�°�˳��ߵ�������ʲôЧ������������ѭ���壬������²����ĵ�
		series.add(addX, addY);
		for (int k = 0; k < length; k++) {
			series.add(xV[k], yV[k]);
		}

		// �����ݼ�������µĵ㼯
		dataset.addSeries(series);

		// ��ͼ���£�û����һ�������߲�����ֶ�̬
		// ����ڷ�UI���߳��У���Ҫ����postInvalidate()������ο�api
		chart.invalidate();
	}
}
