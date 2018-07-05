package com.safe.a360.safemobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		SharedPreferences mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String usersim = mPref.getString("sim", null);
		String safephone = mPref.getString("safe_phone", "");
		boolean protect = mPref.getBoolean("protect",false);
		
		if(protect) {
			
			//如果开启保护了，那么获取当前手机sim卡信息并与开启保护时候的sim比较判断
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
					Context.TELEPHONY_SERVICE);
			String simnum =  telephonyManager.getSimSerialNumber();
			
			String currentnum = telephonyManager.getLine1Number();
			System.out.println("当前手机号码是 ： " + currentnum);
			
			if(usersim.equals(simnum)) {
				System.out.println("手机安全");
			}else{
				//发送sim卡变更消息给安全号码。
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(safephone, null, "sim card was changed !", null, null);
			}
		}
	}

}
