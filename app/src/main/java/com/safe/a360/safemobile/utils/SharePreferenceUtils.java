package com.safe.a360.safemobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtils {
	public static final String SP_NAME = "config";
	public static void saveBoolean(Context context, String key, boolean values) {
		SharedPreferences preferences = context.getSharedPreferences(SP_NAME, 0);
		preferences.edit().putBoolean(key, values).commit();
	}
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences preferences = context.getSharedPreferences(SP_NAME, 0);
		return preferences.getBoolean(key, defValue);
	}
}
