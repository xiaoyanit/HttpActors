package com.novoda.lib.httpservice.tester.util;


public class Log {

	private static final String TAG = "httpservice-tester";
	private static final String HIGHLIGHTHER = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";

	public static final void v(String msg) {
		android.util.Log.v(TAG, msg);
	}
	
	public static final void dev(String msg) {
		android.util.Log.v(TAG, HIGHLIGHTHER);
		android.util.Log.v(TAG, msg);
		android.util.Log.v(TAG, HIGHLIGHTHER);
	}

	public static final void w(String msg) {
		android.util.Log.w(TAG, msg);
	}

	public static final void e(String msg) {
		android.util.Log.e(TAG, msg);
	}

	public static final void e(String msg, Throwable t) {
		android.util.Log.e(TAG, msg, t);
	}

}