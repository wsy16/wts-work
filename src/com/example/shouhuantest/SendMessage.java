package com.example.shouhuantest;

public class SendMessage {

	public static byte[] SH_ID = { (byte) 0x80 };
	public static byte[] SH_VER = { (byte) 0x81 };
	public static byte[] SH_STEPS = { (byte) 0x82 };
	public static byte[] SH_HEART = { (byte) 0x83 };
	public static byte[] SH_KCALS = { (byte) 0x84 };
	public static byte[] SH_DISTANCE = { (byte) 0x85 };
	public static byte[] SH_CELL = { (byte) 0x86 };
	public static byte[] SH_SLEEP_TIME = { (byte) 0x87 };
	public static byte[] SH_SLEEP_DATA = { (byte) 0x88 };
	public static byte[] ACK_GET_SLEEP_DATA = { (byte) 0x89 };
	public static byte[] NACK_GET_SLEEP_DATA = { (byte) 0x8a };
	public static byte[] SH_SHAKE = { (byte) 0xa2 };
}
