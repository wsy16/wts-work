/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.shouhuantest;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
	private final static String TAG = BluetoothLeService.class.getSimpleName();

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;
	private int mConnectionState = STATE_DISCONNECTED;

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_CONNECTING = "com.example.bluetooth.le.ACTION_GATT_CONNECTING";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
	public final static String ACTION_GATT_SERVICES_NOTDISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_NOTDISCOVERED";
	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
			.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
	public final static UUID UUID_READ = UUID
			.fromString("0000aaf2-0000-1000-8000-00805f9b34fb");
	private String adress;
	private BluetoothGattCharacteristic writeBluetoothGattCharacteristic,
			readBluetoothGattCharacteristic;

	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			System.out.println("=======status:" + status);
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				mConnectionState = STATE_CONNECTED;
				broadcastUpdate(intentAction);
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				mConnectionState = STATE_DISCONNECTED;
				Log.i(TAG, "Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			} else {
				intentAction = ACTION_GATT_SERVICES_NOTDISCOVERED;
				broadcastUpdate(intentAction);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
			} else {
				broadcastUpdate(ACTION_GATT_SERVICES_NOTDISCOVERED);
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			System.out.println("onCharacteristicRead");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {

			System.out.println("onDescriptorWriteonDescriptorWrite = " + status
					+ ", descriptor =" + descriptor.getUuid().toString());
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			if (characteristic.getValue() != null) {

				System.out.println("stringbyte--->"
						+ BytesToStr(characteristic.getValue()));
				System.out.println("stringint--->"
						+ IntsToStr(characteristic.getValue()));

			}
			System.out.println("--------onCharacteristicChanged-----");
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			System.out.println("rssi = " + rssi);
		}

		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {

			System.out.println("--------write success----- status:" + status);

		};
	};

	public void onCreate() {
		System.out.println("----onCreat");

	};

//	@Override
//	@Deprecated
//	public void onStart(Intent intent, int startId) {
//		// TODO Auto-generated method stub
//		super.onStart(intent, startId);
////		adress = intent.getStringExtra("adress");
////		Timer timer = new Timer();
////		timer.schedule(new Work(), 0, 30000);
//		System.out.println("----onCreat");
//	}

//	private byte[] addByte(byte[] b, byte[] last) {
//		if (b[0] == 0xAA) {
//			return b;
//		} else {
//			byte[] addbyte = new byte[b.length + last.length];
//			for (int i = 0; i < last.length + b.length; i++) {
//				if (i < last.length) {
//					addbyte[i] = last[i];
//				} else {
//					addbyte[i] = b[i - b.length];
//				}
//			}
//			return addbyte;
//		}
//	}

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	public String BytesToStr(byte[] target) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0, j = target.length; i < j; i++) {
			if (i == target.length - 1) {
				buf.append(Integer.toString(target[i]));
			} else {
				buf.append(Integer.toString(target[i]) + ",");
			}

		}
		return buf.toString();
	}

	public String IntsToStr(byte[] target) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0, j = target.length; i < j; i++) {
			if (i == target.length - 1) {
				buf.append(Integer.toString(0xFF & target[i], 16));
			} else {
				buf.append(Integer.toString(0xFF & target[i], 16) + ",");
			}

		}
		return buf.toString();
	}

	private void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);

		// This is special handling for the Heart Rate Measurement profile. Data
		// parsing is
		// carried out as per profile specifications:
		// http://developer.bluetooth.org/gatt/characteristics/Pages/Characteristth.charactericViewer.aspx?u=org.bluetooistic.heart_rate_measurement.xml
		// Intent localIntent = new Intent(paramString);
		if (UUID_READ.equals(characteristic.getUuid())) {
			byte[] arrayOfByte = characteristic.getValue();
			// if ((arrayOfByte != null) && (arrayOfByte.length > 0)) {
			intent.putExtra(EXTRA_DATA, arrayOfByte);
			sendBroadcast(intent);
			// }

		} else if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			int flag = characteristic.getProperties();
			int format = -1;
			if ((flag & 0x01) != 0) {
				format = BluetoothGattCharacteristic.FORMAT_UINT16;
				Log.d(TAG, "Heart rate format UINT16.");
			} else {
				format = BluetoothGattCharacteristic.FORMAT_UINT8;
				Log.d(TAG, "Heart rate format UINT8.");
			}
			final int heartRate = characteristic.getIntValue(format, 1);
			System.out.println("Received heart rate: %d" + heartRate);
			Log.d(TAG, String.format("Received heart rate: %d", heartRate));
			intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
		} else {
			// For all other profiles, writes the data formatted in HEX.
			byte[] arrayOfByte = characteristic.getValue();
			if ((arrayOfByte != null) && (arrayOfByte.length > 0)) {
				intent.putExtra(EXTRA_DATA, arrayOfByte);
			}
		}

	}

	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// After using a given device, you should make sure that
		// BluetoothGatt.close() is called
		// such that resources are cleaned up properly. In this particular
		// example, close() is
		// invoked when the UI is disconnected from the Service.
		close();
		return super.onUnbind(intent);
	}

	private final IBinder mBinder = new LocalBinder();

	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 * 
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param address
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null
				|| address.length() <= 0) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)
//		if (mBluetoothDeviceAddress != null
//				&& address.equals(mBluetoothDeviceAddress)
//				&& mBluetoothGatt != null) {
//			Log.d(TAG,
//					"Trying to use an existing mBluetoothGatt for connection.");
//			if (mBluetoothGatt.connect()) {
//				mConnectionState = STATE_CONNECTING;
//				broadcastUpdate(ACTION_GATT_CONNECTING);
//				return true;
//			} else {
//				return false;
//			}
//		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		device.setPairingConfirmation(true);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		broadcastUpdate(ACTION_GATT_CONNECTING);
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	
	}
	
	
	public int getConnectStatas(String address){
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return -1;
		}
		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		return  mBluetoothGatt.getConnectionState(device);
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	public synchronized void wirteCharacteristic(
			BluetoothGattCharacteristic characteristic) {

		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.writeCharacteristic(characteristic);

	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
				.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
		if (descriptor != null) {
			System.out.println("write descriptor");
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		}
	}

	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}

	/**
	 * Read the RSSI for a connected remote device.
	 * */
	public boolean getRssiVal() {
		if (mBluetoothGatt == null)
			return false;

		return mBluetoothGatt.readRemoteRssi();
	}

//	class Work extends TimerTask {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			Message message = new Message();
//			message.what = 1;
//			handler.sendMessage(message);
//		}
//	}

//	Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			if (msg.what == 1) {
//				// 试图连接手环设备
//				connect(adress);
//			}
//		}
//
//	};

	// private final BroadcastReceiver mGattUpdateReceiver = new
	// BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// final String action = intent.getAction();
	// System.out.println("action = " + action);
	// if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
	// .equals(action)) {
	// displayGattServices(getSupportedGattServices());
	// } else if (BluetoothLeService.ACTION_GATT_SERVICES_NOTDISCOVERED
	// .equals(action)) {
	//
	// // if (sendProgressDialog.isShowing()) {
	// // sendProgressDialog.dismiss();
	// // }
	// // Toast.makeText(BraceletActivity.this, "蓝牙连接失败，请尝试重启蓝牙",
	// // Toast.LENGTH_SHORT);
	// } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
	// byte[] byteValues = intent
	// .getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
	// // showNotify(LudeUtiles.receiveValues(byteValues));
	// // displayData(LudeUtiles.receiveValues(byteValues));
	// } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
	// .equals(action)) {
	// // isconnect = false;
	// // connectProgressDialog.dismiss();
	// // connectBtn.setText("设备未连接");
	// } else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
	//
	// // connectBtn.setText("设备已连接");
	// // isconnect = true;
	// // connectProgressDialog.dismiss();
	// // if (!sendProgressDialog.isShowing()) {
	// // sendProgressDialog.show();
	// // }
	// } else if (BluetoothLeService.ACTION_GATT_CONNECTING.equals(action)) {
	//
	// }
	// }
	// };

	// @SuppressLint("NewApi")
	// private void displayGattServices(List<BluetoothGattService> gattServices)
	// {
	// if (gattServices != null)
	// // Loops through available GATT Services.
	// for (BluetoothGattService gattService : gattServices) {
	// List<BluetoothGattCharacteristic> gattCharacteristics = gattService
	// .getCharacteristics();
	// for (BluetoothGattCharacteristic gattCharacteristic :
	// gattCharacteristics) {
	// if (gattCharacteristic.getUuid().toString()
	// .equals("0000aaf1-0000-1000-8000-00805f9b34fb")) {
	// writeBluetoothGattCharacteristic = gattCharacteristic;
	// }
	// if (gattCharacteristic.getUuid().toString()
	// .equals("0000aaf2-0000-1000-8000-00805f9b34fb")) {
	// readBluetoothGattCharacteristic = gattCharacteristic;
	// setCharacteristicNotification(
	// readBluetoothGattCharacteristic, true);
	// }
	//
	// }
	// }
	// }

	/**
	 * 发送手机状态 发送02让手环发送数据，发送01 让手环不发送数据
	 * 
	 * @param isShow
	 */
	// private void sendPhoneType(final boolean isShow) {
	//
	// if (writeBluetoothGattCharacteristic != null
	// && mBluetoothLeService != null) {
	// if (isShow) {
	// writeBluetoothGattCharacteristic.setValue(LudeUtiles
	// .sendValues(BraceletActivity.this, new byte[] {
	// (byte) 0xA5, 0x05, 0x02 }));
	// wirteCharacteristic(writeBluetoothGattCharacteristic);
	// } else {
	// writeBluetoothGattCharacteristic.setValue(LudeUtiles
	// .sendValues(BraceletActivity.this, new byte[] {
	// (byte) 0xA5, 0x05, 0x01 }));
	// wirteCharacteristic(writeBluetoothGattCharacteristic);
	// }
	//
	// }
	// }

	// private void showNotify(String msg) {
	// // 创建一个NotificationManager的引用
	//
	// String ns = Context.NOTIFICATION_SERVICE;
	//
	// NotificationManager mNotificationManager = (NotificationManager)
	// getSystemService(ns);
	//
	// // 定义Notification的各种属性
	//
	// int icon = R.drawable.ic_launcher; // 通知图标
	//
	// CharSequence tickerText = "通知"; // 状态栏显示的通知文本提示
	//
	// long when = System.currentTimeMillis(); // 通知产生的时间，会在通知信息里显示
	//
	// // 用上面的属性初始化Nofification
	//
	// Notification notification = new Notification(icon, tickerText, when);
	//
	// /*
	// *
	// * 添加声音
	// *
	// * notification.defaults |=Notification.DEFAULT_SOUND;
	// *
	// * 或者使用以下几种方式
	// *
	// * notification.sound =
	// * Uri.parse("file:///sdcard/notification/ringer.mp3");
	// *
	// * notification.sound =
	// * Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
	// *
	// * 如果想要让声音持续重复直到用户对通知做出反应，则可以在notification的flags字段增加"FLAG_INSISTENT"
	// *
	// * 如果notification的defaults字段包括了"DEFAULT_SOUND"属性，则这个属性将覆盖sound字段中定义的声音
	// */
	//
	// /*
	// *
	// * 添加振动
	// *
	// * notification.defaults |= Notification.DEFAULT_VIBRATE;
	// *
	// * 或者可以定义自己的振动模式：
	// *
	// * long[] vibrate = {0,100,200,300};
	// * //0毫秒后开始振动，振动100毫秒后停止，再过200毫秒后再次振动300毫秒
	// *
	// * notification.vibrate = vibrate;
	// *
	// * long数组可以定义成想要的任何长度
	// *
	// * 如果notification的defaults字段包括了"DEFAULT_VIBRATE",则这个属性将覆盖vibrate字段中定义的振动
	// */
	//
	// /*
	// *
	// * 添加LED灯提醒
	// *
	// * notification.defaults |= Notification.DEFAULT_LIGHTS;
	// *
	// * 或者可以自己的LED提醒模式:
	// *
	// * notification.ledARGB = 0xff00ff00;
	// *
	// * notification.ledOnMS = 300; //亮的时间
	// *
	// * notification.ledOffMS = 1000; //灭的时间
	// *
	// * notification.flags |= Notification.FLAG_SHOW_LIGHTS;
	// */
	//
	// /*
	// *
	// * 更多的特征属性
	// *
	// * notification.flags |= FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
	// *
	// * notification.flags |= FLAG_INSISTENT; //重复发出声音，直到用户响应此通知
	// *
	// * notification.flags |= FLAG_ONGOING_EVENT;
	// * //将此通知放到通知栏的"Ongoing"即"正在运行"组中
	// *
	// * notification.flags |= FLAG_NO_CLEAR; //表明在点击了通知栏中的"清除通知"后，此通知不清除，
	// *
	// * //经常与FLAG_ONGOING_EVENT一起使用
	// *
	// * notification.number = 1; //number字段表示此通知代表的当前事件数量，它将覆盖在状态栏图标的顶部
	// *
	// * //如果要使用此字段，必须从1开始
	// *
	// * notification.iconLevel = ; //
	// */
	//
	// // 设置通知的事件消息
	//
	// Context context = getApplicationContext(); // 上下文
	//
	// CharSequence contentTitle = "手环通知"; // 通知栏标题
	//
	// CharSequence contentText = msg; // 通知栏内容
	//
	// Intent notificationIntent = new Intent(this, BraceletActivity.class); //
	// 点击该通知后要跳转的Activity
	//
	// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	// notificationIntent, 0);
	//
	// notification.setLatestEventInfo(context, contentTitle, contentText,
	// contentIntent);
	//
	// // 把Notification传递给NotificationManager
	//
	// mNotificationManager.notify(0, notification);
	// }

	// private static IntentFilter makeGattUpdateIntentFilter() {
	// final IntentFilter intentFilter = new IntentFilter();
	// intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
	// intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
	// intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTING);
	// intentFilter
	// .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
	// intentFilter
	// .addAction(BluetoothLeService.ACTION_GATT_SERVICES_NOTDISCOVERED);
	// intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
	// return intentFilter;
	// }
}
