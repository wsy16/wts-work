package com.example.shouhuantest;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class LudeUtiles {
	public static boolean isConnect = false;

	// 发送数据
	public static byte[] sendValues(byte[] b) {

		byte[] values = new byte[b.length + 1];
		byte sumb = 0;
		for (int i = 0; i < b.length; i++) {
			values[i] = b[i];
			sumb = (byte) (sumb + b[i]);
		}
		values[values.length - 1] = sumb;
		System.out.println("sendbyte--->" + IntsToStr16(values));
		return values;

	}


	public static int getLowAddHighByte(byte high, byte low) {
		int i = ((0xff & high) * 256 + (0xff & low));
		return i;
	}

	// 发送数据
	// public static byte[] sendValues(BaseBleActivity context, byte[] b) {
	// ArrayList<Byte> byteList = new ArrayList<Byte>();
	// byteList.add((byte) 0x55);
	// byteList.add((byte) 0xAA);
	// byteList.add((byte) (b.length + 1));
	// for (int i = 0; i < b.length; i++) {
	// byteList.add(b[i]);
	// }
	//
	// byte[] values = new byte[byteList.size() + 1];
	// byte sumb = 0;
	// for (int i = 0; i < byteList.size(); i++) {
	// values[i] = byteList.get(i);
	// sumb = (byte) (sumb + byteList.get(i));
	// }
	// values[values.length - 1] = sumb;
	// // Toast.makeText(context, "发送数据成功" + IntsToStr16(values),
	// // Toast.LENGTH_SHORT).show();
	// System.out.println("sendbyte--->" + IntsToStr16(values));
	// return values;
	//
	// }

	// 接收数据
	public static byte[] receiveValues(byte[] b) {

		if (b != null && b.length > 2) {
			byte sumByte = 0;
			for (int i = 0; i < b.length - 1; i++) {
				sumByte += b[i];
			}
			if (sumByte == b[b.length - 1]) {
				return b;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public static String int2String10(byte[] target) {
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

	public static String IntsToStr16(byte[] target) {
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

	public static byte[] StrToByte(String s) {
		String[] strings = s.split(",");
		if (strings.length > 0) {
			byte[] bytes = new byte[strings.length];
			try {
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = (byte) Integer.parseInt(strings[i], 16);
				}
				return bytes;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}

		} else {
			return null;
		}

	}

	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[8];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < 8; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public static float getkaluli(int w, double l) {
		BigDecimal b = new BigDecimal((float) (w * l * 1.036 / 100000));
		float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		return f1;
	}

	public static void addAppNotify(Context context, String tickerText,
			String notifyTitle) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher; // 通知图标
		notification.tickerText = tickerText; // 通知的内容
		notification.defaults = Notification.DEFAULT_SOUND; // 通知的铃声
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent();
		intent.setComponent(new ComponentName("carman.execise",
				"carman.execise.Main"));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		// 点击状态栏的图标出现的提示信息设置
		notification.setLatestEventInfo(context, notifyTitle, "notify",
				pendingIntent);
		manager.notify(1, notification); // 这个函数中第一个参数代表identifier.如果要同时弹出多条通知，每个通知的这个参数必须不同。否则，后面的会覆盖前面的通知。
	}

	public static void setAlarmTime(Context context, long timeInMillis) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("android.alarm.demo.action");
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		int interval = 7 * 24 * 60 * 60 * 1000; // 7天时间的毫秒形式
		am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,

		interval, sender);// 参数2表示唤醒时间，参数2表示时间间隔。

	}

	public static ProgressDialog createProgressDialog(final BaseBleActivity a,
			final String msg) {
		final ProgressDialog dialog = new ProgressDialog(a);
		dialog.setMessage(msg);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
}
