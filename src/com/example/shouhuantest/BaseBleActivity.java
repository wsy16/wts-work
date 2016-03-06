package com.example.shouhuantest;

import java.util.Calendar;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class BaseBleActivity extends Activity {
	private static final long SCAN_PERIOD = 15000;

	private static final long RUN_SLEEP = 20000;
	private BluetoothAdapter mBluetoothAdapter;

	private BluetoothGattCharacteristic writeBluetoothGattCharacteristic,
			readBluetoothGattCharacterist0ic;
	private BluetoothLeService mBluetoothLeService;
	private Handler mHandler;
	private boolean mRunning = true;
	protected boolean isconnect = false;
	private String mDeviceAddress;
	private LinearLayout baseLayout;
	private int type;
	private ViewInterface viewInterface;
	public String w, h;

	// private Button conncetButton;
	private Handler mainHandle = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				// if (!connectProgressDialog.isShowing()) {
				// connectProgressDialog.show();
				// }
				// if (sendProgressDialog.isShowing()) {
				// sendProgressDialog.dismiss();
				// }
			}
			return false;

		}
	});
	private final ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				finish();
			}

			connectDevice();
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	}; 
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			System.out.println("action = " + action);
			if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				displayGattServices(mBluetoothLeService
						.getSupportedGattServices());
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_NOTDISCOVERED
					.equals(action)) {
				// if (sendProgressDialog.isShowing()) {
				// sendProgressDialog.dismiss();
				// }
				Toast.makeText(BaseBleActivity.this, "蓝牙连接失败，请尝试重启蓝牙",
						Toast.LENGTH_SHORT);
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				byte[] byteValues = intent
						.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				displayData(byteValues);
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				isconnect = false;
				viewInterface.disconncet();

				// connectProgressDialog.dismiss();
			} else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				// connectProgressDialog.dismiss();
				isconnect = true;
				viewInterface.conncet();
				// conncetButton.setBackgroundResource(R.drawable.shebeiyilianjie);
				// if (!sendProgressDialog.isShowing()) {
				// sendProgressDialog.show();
				// }
			} else if (BluetoothLeService.ACTION_GATT_CONNECTING.equals(action)) {

			}
		}
	};

	@SuppressLint("NewApi")
	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices != null)
			// Loops through available GATT Services.
			for (BluetoothGattService gattService : gattServices) {
				List<BluetoothGattCharacteristic> gattCharacteristics = gattService
						.getCharacteristics();
				for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
					if (gattCharacteristic.getUuid().toString()
							.equals("0000aaf1-0000-1000-8000-00805f9b34fb")) {
						writeBluetoothGattCharacteristic = gattCharacteristic;

					}
					if (gattCharacteristic.getUuid().toString()
							.equals("0000aaf2-0000-1000-8000-00805f9b34fb")) {
						readBluetoothGattCharacterist0ic = gattCharacteristic;
						mBluetoothLeService.setCharacteristicNotification(
								readBluetoothGattCharacterist0ic, true);
					}

				}
			}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (type == 1) {
					sendMsg(LudeUtiles.sendValues(SendMessage.SH_STEPS));
				} else if (type == 2) {
					sendMsg(LudeUtiles.sendValues(SendMessage.SH_SLEEP_TIME));
				} else if (type == 3) {
					sendMsg(LudeUtiles.sendValues(SendMessage.SH_HEART));

				}

			}
		}, 300);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendTime();

			}
		}, 1300);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (w != null && h != null) {
					sendUserInfo();

				}

			}
		}, 2300);
	}

	protected void displayData(byte[] bytes) {
		viewInterface.displayData(bytes);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MainApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		type = intent.getIntExtra("intentView", 0);
		setContentView(R.layout.base_view);

		baseLayout = (LinearLayout) findViewById(R.id.base_lin);
		baseLayout.removeAllViews();

		if (type == 1) {
			viewInterface = new RunDetailActivity(BaseBleActivity.this,
					baseLayout);
			baseLayout.addView(viewInterface.getView());
		} else if (type == 2) {
			viewInterface = new SleepActivity(BaseBleActivity.this, baseLayout,
					new SleepSendCallBack() {

						@Override
						public void callbackSuccess() {
							// TODO Auto-generated method stub
							sendMsg(LudeUtiles
									.sendValues(SendMessage.ACK_GET_SLEEP_DATA));
						}

						@Override
						public void callFail() {
							// TODO Auto-generated method stub

						}
					});
			baseLayout.addView(viewInterface.getView());
		} else if (type == 3) {
			viewInterface = new XinLvActivity(BaseBleActivity.this, baseLayout);
			baseLayout.addView(viewInterface.getView());
		}

		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		MyHandlerThread thread = new MyHandlerThread("MyHandlerThread");
		thread.start();// 创建一个HandlerThread并启动它
		mHandler = new Handler(thread.getLooper(), thread);//
		// 使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程

		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		mDeviceAddress = ACache.get(BaseBleActivity.this).getAsString("adress");
		mRunning = true;
		mHandler.post(mBackgroundRunnable);// 将线程post到Handler中
	
		String idString = ACache.get(BaseBleActivity.this).getAsString("id");
		int id = 0;
		if (idString != null) {
			id = Integer.parseInt(idString);
		}
		UserInfoAllBean allBeans = (UserInfoAllBean) ACache.get(
				BaseBleActivity.this).getAsObject("userinfo");
		if (allBeans != null) {
			for (int i = 0; i < allBeans.getBeans().size(); i++) {
				if (allBeans.getBeans().get(i).getId() == id) {
					UserMainBean mainBean = allBeans.getBeans().get(i);
					w = mainBean.getWeight();
					h = mainBean.getHeight();
				}
			}
		}
	}

	Runnable mBackgroundRunnable = new Runnable() {

		@Override
		public void run() {
			while (mRunning) {
				System.out.println("Thread is run");
				try {
					if (!isconnect) {
						mainHandle.sendEmptyMessage(1);
						scanLeDevice(true);
					}
					Thread.sleep(RUN_SLEEP);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private void connectDevice() {
		if (mBluetoothLeService != null && mDeviceAddress != null) {
			// if (connectProgressDialog != null
			// && !connectProgressDialog.isShowing()) {
			// connectProgressDialog.show();
			// }
			// if (sendProgressDialog.isShowing()) {
			// sendProgressDialog.dismiss();
			// }
			mBluetoothLeService.connect(mDeviceAddress);
		}
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			BaseBleActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if (mBluetoothLeService != null) {
						System.out.println("name--->" + device.getName());
						if (device.getName().equals("SH_KPT1")) {
							mDeviceAddress = device.getAddress();
							ACache.get(BaseBleActivity.this).put("adress",
									mDeviceAddress);
							mBluetoothLeService.connect(mDeviceAddress);
							scanLeDevice(false);
						}
					}
				}
			});
		}
	};

	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		type = getIntent().getIntExtra("intentView", 0);
		baseLayout.removeAllViews();

		if (type == 1) {
			viewInterface = new RunDetailActivity(BaseBleActivity.this,
					baseLayout);
			baseLayout.addView(viewInterface.getView());
			sendMsg(LudeUtiles.sendValues(SendMessage.SH_STEPS));
		} else if (type == 2) {
			viewInterface = new SleepActivity(BaseBleActivity.this, baseLayout,
					new SleepSendCallBack() {

						@Override
						public void callbackSuccess() {
							// TODO Auto-generated method stub
							sendMsg(LudeUtiles
									.sendValues(SendMessage.ACK_GET_SLEEP_DATA));
						}

						@Override
						public void callFail() {
							// TODO Auto-generated method stub

						}
					});
			baseLayout.addView(viewInterface.getView());
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					sendMsg(LudeUtiles.sendValues(SendMessage.SH_SLEEP_TIME));

				}
			}, 300);

		} else if (type == 3) {
			viewInterface = new XinLvActivity(BaseBleActivity.this, baseLayout);
			baseLayout.addView(viewInterface.getView());
			sendMsg(LudeUtiles.sendValues(SendMessage.SH_HEART));

		}
		if (isconnect) {
			viewInterface.conncet();
		} else {
			viewInterface.disconncet();
		}

	};

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTING);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_NOTDISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isconnect) {
			connectDevice();
		}
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		scanLeDevice(false);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
		if (mBluetoothLeService != null) {
			mBluetoothLeService.disconnect();
			mBluetoothLeService.close();
		}

		unbindService(mServiceConnection);
		mBluetoothLeService = null;
		// 启动服务后台运行
		// if (mDeviceAddress != null && mDeviceAddress.length() > 0) {
		// Intent intent = new Intent(BraceletActivity.this,
		// XinLvSerivice.class);
		// intent.putExtra("adress", mDeviceAddress);
		// startService(intent);
		// }

		mRunning = false;
		mHandler.removeCallbacks(mBackgroundRunnable);
	}

	class MyHandlerThread extends HandlerThread implements Callback {

		public MyHandlerThread(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	protected void sendMsg(byte[] b) {
		if (isconnect) {
			if (writeBluetoothGattCharacteristic != null) {

				writeBluetoothGattCharacteristic.setValue(b);
				mBluetoothLeService
						.wirteCharacteristic(writeBluetoothGattCharacteristic);
			}
		}

	}

	private void sendTime() {
		if (writeBluetoothGattCharacteristic != null) {
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR) - 2000;
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE) - 1;
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int second = c.get(Calendar.SECOND);
			writeBluetoothGattCharacteristic.setValue(LudeUtiles
					.sendValues(new byte[] { (byte) 0xA0, (byte) year,
							(byte) month, (byte) date, (byte) hour,
							(byte) minute, (byte) second }));
			mBluetoothLeService
					.wirteCharacteristic(writeBluetoothGattCharacteristic);
		}
	}

	public void sendUserInfo() {
		if (writeBluetoothGattCharacteristic != null) {
			String xinlvHigh = ACache.get(BaseBleActivity.this).getAsString(
					"xinlvhigh");
			if (xinlvHigh == null) {
				xinlvHigh = "0";
			}
			writeBluetoothGattCharacteristic.setValue(LudeUtiles
					.sendValues(new byte[] { (byte) 0xa1,
							(byte) Integer.parseInt(h),
							(byte) Integer.parseInt(w),
							(byte) Integer.parseInt(xinlvHigh) }));
			mBluetoothLeService
					.wirteCharacteristic(writeBluetoothGattCharacteristic);
		}
	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mainHandle.postDelayed(new Runnable() {
				@Override
				public void run() {
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);

			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(BaseBleActivity.this, MainActivity.class);
			intent.putExtra("isclose", true);
			startActivity(intent);
			overridePendingTransition(R.anim.leftout, R.anim.rightin);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
