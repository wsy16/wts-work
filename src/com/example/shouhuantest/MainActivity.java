package com.example.shouhuantest;

import java.util.List;

import com.example.shouhuantest.BaseBleActivity.MyHandlerThread;
import com.example.shouhuantest.MultiDirectionSlidingDrawer.OnDrawerCloseListener;
import com.example.shouhuantest.MultiDirectionSlidingDrawer.OnDrawerOpenListener;
import com.example.shouhuantest.R.id;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private MultiDirectionSlidingDrawer sd;
	private ImageView iv;
	private AnimationDrawable animationDrawable;
	private Button addButton;
	private boolean isShowUser = false;
	private LinearLayout modleLin;
	private LinearLayout userLinearLayout, whiteBg;
	private ImageView imgRun, imgSleep, imgXinLv;
	private ImageView userchange1, userchange2, userchange3;
	private TextView user1TextView, user2TextView, user3TextView;
	private ImageView user1ImageView, user2ImageView, user3ImageView;

	/** Called when the activity is first created. */

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_main);
		sd = (MultiDirectionSlidingDrawer) findViewById(R.id.sliding);
		iv = (ImageView) findViewById(R.id.blue_dowm);
		addButton = (Button) findViewById(R.id.add);
		userLinearLayout = (LinearLayout) findViewById(R.id.userLin);
		whiteBg = (LinearLayout) findViewById(R.id.white_bg);
		modleLin = (LinearLayout) findViewById(R.id.modle_lin);
		imgRun = (ImageView) findViewById(R.id.img_run);
		imgSleep = (ImageView) findViewById(R.id.img_sleep);
		imgXinLv = (ImageView) findViewById(R.id.img_xinlv);
		userchange1 = (ImageView) findViewById(R.id.userchange1);
		userchange2 = (ImageView) findViewById(R.id.userchange2);
		userchange3 = (ImageView) findViewById(R.id.userchange3);
		user1TextView = (TextView) findViewById(R.id.text_user1);
		user2TextView = (TextView) findViewById(R.id.text_user2);
		user3TextView = (TextView) findViewById(R.id.text_user3);
		user1ImageView = (ImageView) findViewById(R.id.img_user1);
		user2ImageView = (ImageView) findViewById(R.id.img_user2);
		user3ImageView = (ImageView) findViewById(R.id.img_user3);

		animationDrawable = (AnimationDrawable) iv.getBackground();
		animationDrawable.start();
		// ACache.get(MainActivity.this).clear();
		// finish();

		// if (idString != null && idString.length() > 0) {
		// } else {
		// Intent intent = new Intent();
		// intent.putExtra("userID", 1);
		// intent.setClass(MainActivity.this, UserInfoActivity.class);
		// startActivity(intent);
		// }

		boolean isclose = getIntent().getBooleanExtra("isclose", false);
		if (isclose) {
			sd.close();
			animationDrawable = (AnimationDrawable) iv.getBackground();
			animationDrawable.stop();
			iv.setBackgroundResource(R.anim.blue_handle_down);
			animationDrawable = (AnimationDrawable) iv.getBackground();
			animationDrawable.start();
			modleLin.setVisibility(View.VISIBLE);
			addButton.setVisibility(View.GONE);
		} else {
			sd.open();
			animationDrawable = (AnimationDrawable) iv.getBackground();
			animationDrawable.stop();
			iv.setBackgroundResource(R.anim.blue_handle_up);
			animationDrawable = (AnimationDrawable) iv.getBackground();
			animationDrawable.start();
			modleLin.setVisibility(View.GONE);
			addButton.setVisibility(View.VISIBLE);
		}

		sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				animationDrawable = (AnimationDrawable) iv.getBackground();
				animationDrawable.stop();
				iv.setBackgroundResource(R.anim.blue_handle_up);
				animationDrawable = (AnimationDrawable) iv.getBackground();
				animationDrawable.start();
				modleLin.setVisibility(View.GONE);
				addButton.setVisibility(View.VISIBLE);
			}
		});
		sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				animationDrawable = (AnimationDrawable) iv.getBackground();
				animationDrawable.stop();
				iv.setBackgroundResource(R.anim.blue_handle_down);
				animationDrawable = (AnimationDrawable) iv.getBackground();
				animationDrawable.start();
				modleLin.setVisibility(View.VISIBLE);
				addButton.setVisibility(View.GONE);
			}
		});

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isShowUser) {
					sd.lock();
					addButton.setBackgroundResource(R.drawable.jian);
					whiteBg.setVisibility(View.VISIBLE);
					userLinearLayout.setVisibility(View.VISIBLE);

				} else {
					sd.unlock();
					addButton.setBackgroundResource(R.drawable.add);
					userLinearLayout.setVisibility(View.GONE);
					whiteBg.setVisibility(View.GONE);
				}
				isShowUser = !isShowUser;

			}

		});
		imgRun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("intentView", 1);
				// if (mDeviceAddress != null) {
				// intent.putExtra("adress", mDeviceAddress);
				// }Intent intent = new Intent(B.this, C.class);
				intent.setClass(MainActivity.this, BaseBleActivity.class);
				startActivity(intent);

			}
		});
		imgXinLv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("intentView", 3);
				// if (mDeviceAddress != null) {
				// intent.putExtra("adress", mDeviceAddress);
				// }
				intent.setClass(MainActivity.this, BaseBleActivity.class);
				startActivity(intent);

			}
		});
		imgSleep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("intentView", 2);
				// if (mDeviceAddress != null) {
				// intent.putExtra("adress", mDeviceAddress);
				// }
				intent.setClass(MainActivity.this, BaseBleActivity.class);
				startActivity(intent);

			}
		});
		userchange1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("userID", 1);
				intent.setClass(MainActivity.this, UserInfoActivity.class);
				startActivity(intent);
			}
		});

		userchange2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("userID", 2);
				intent.setClass(MainActivity.this, UserInfoActivity.class);
				startActivity(intent);
			}
		});
		userchange3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ACache.get(MainActivity.this).clear();
				// Toast.makeText(MainActivity.this, "清除缓存，测试用",
				// Toast.LENGTH_SHORT).show();
				//
				// finish();
				Intent intent = new Intent();
				intent.putExtra("userID", 3);
				intent.setClass(MainActivity.this, UserInfoActivity.class);
				startActivity(intent);
			}
		});

		user1TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ACache.get(MainActivity.this).put("id", "1");
				user1TextView.setTextColor(getResources().getColor(
						R.color.dark_blue));
				user2TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				user3TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				Toast.makeText(MainActivity.this, "切换用户成功", Toast.LENGTH_SHORT)
						.show();
			}
		});

		user2TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ACache.get(MainActivity.this).put("id", "2");
				user1TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				user2TextView.setTextColor(getResources().getColor(
						R.color.dark_blue));
				user3TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				Toast.makeText(MainActivity.this, "切换用户成功", Toast.LENGTH_SHORT)
						.show();

			}
		});
		user3TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ACache.get(MainActivity.this).put("id", "3");
				user1TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				user2TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				user3TextView.setTextColor(getResources().getColor(
						R.color.dark_blue));
				Toast.makeText(MainActivity.this, "切换用户成功", Toast.LENGTH_SHORT)
						.show();

			}
		});
		user1ImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ACache.get(MainActivity.this).put("id", "1");
				user1TextView.setTextColor(getResources().getColor(
						R.color.dark_blue));
				user2TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				user3TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				Toast.makeText(MainActivity.this, "切换用户成功", Toast.LENGTH_SHORT)
						.show();
			}
		});

		user2ImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ACache.get(MainActivity.this).put("id", "2");
				user1TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				user2TextView.setTextColor(getResources().getColor(
						R.color.dark_blue));
				user3TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				Toast.makeText(MainActivity.this, "切换用户成功", Toast.LENGTH_SHORT)
						.show();

			}
		});
		user3ImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ACache.get(MainActivity.this).put("id", "3");
				user1TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				user2TextView.setTextColor(getResources().getColor(
						R.color.user_green));
				user3TextView.setTextColor(getResources().getColor(
						R.color.dark_blue));
				Toast.makeText(MainActivity.this, "切换用户成功", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String idString = ACache.get(MainActivity.this).getAsString("id");
		UserInfoAllBean allBeans = (UserInfoAllBean) ACache.get(
				MainActivity.this).getAsObject("userinfo");
		if (allBeans == null || idString == null) {
			Intent intent = new Intent();
			intent.putExtra("userID", 1);
			intent.setClass(MainActivity.this, UserInfoActivity.class);
			startActivity(intent);

		} else {
			int idint = Integer.parseInt(idString);
			for (int i = 0; i < allBeans.getBeans().size(); i++) {
				if (allBeans.getBeans().get(i).getId() == 1) {
					if (idint == 1) {
						user1TextView.setTextColor(getResources().getColor(
								R.color.dark_blue));
					} else {
						user1TextView.setTextColor(getResources().getColor(
								R.color.user_green));
					}
					user1TextView.setText(allBeans.getBeans().get(i)
							.getUserName());
				} else if (allBeans.getBeans().get(i).getId() == 2) {
					if (idint == 2) {
						user2TextView.setTextColor(getResources().getColor(
								R.color.dark_blue));
					} else {
						user2TextView.setTextColor(getResources().getColor(
								R.color.user_green));
					}
					user2TextView.setText(allBeans.getBeans().get(i)
							.getUserName());
				} else if (allBeans.getBeans().get(i).getId() == 3) {
					if (idint == 3) {
						user3TextView.setTextColor(getResources().getColor(
								R.color.dark_blue));
					} else {
						user3TextView.setTextColor(getResources().getColor(
								R.color.user_green));
					}
					user3TextView.setText(allBeans.getBeans().get(i)
							.getUserName());
				}
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	// Device scan callback.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isShowUser) {
				sd.unlock();
				addButton.setBackgroundResource(R.drawable.add);
				userLinearLayout.setVisibility(View.GONE);
				whiteBg.setVisibility(View.GONE);
				isShowUser = !isShowUser;
				return false;
			} else {
				MainApplication.getInstance().exit();
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}
}
