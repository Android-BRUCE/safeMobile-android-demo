package com.safe.a360.safemobile.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.activity.DeviceAdminSample;
import com.safe.a360.safemobile.service.LocationService;

/**
 * 拦截短信
 * @author BRUCE
 *
 */
public class SmsReceiver extends BroadcastReceiver {
	private SharedPreferences mPerf ;
	DevicePolicyManager mDPM;
	@Override
	public void onReceive(Context context, Intent intent) {
		 Object[] object = (Object[]) intent.getExtras().get("pdus");
		
		mPerf = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean bProtect = mPerf.getBoolean("protect", false);
		String safeNum = mPerf.getString("safe_phone", "");
		
		
		if(bProtect) {
			for (Object object2 : object) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object2);
				String smsBody = smsMessage.getMessageBody();
				String oriNum = smsMessage.getOriginatingAddress();
				//"#*alarm*#"报警音乐
				if("#*alarm*#".equals(smsBody)) {
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setVolume(1.0f, 1.0f);
					player.setLooping(true);
					player.start();
					abortBroadcast();//中断短信被其他应用接收。
				}else if ("#*location*#".equals(smsBody) ) {
					context.startService(new Intent(context, LocationService.class));
					
					String locationString = mPerf.getString("location", "getting location...");
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safeNum, null, locationString, null, null);
					
					System.out.println("location:" + locationString);
					abortBroadcast();
				} else if ("#*wipedata*#".equals(smsBody)) {
					System.out.println("远程清除数据");
					 mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
					 ComponentName compenentName = new ComponentName(context, DeviceAdminSample.class);//激活组件名
					 if(mDPM.isAdminActive(compenentName)){//判断是否激活否则会奔溃。
						 mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
					 }
					 
					abortBroadcast();
				} else if ("#*lockscreen*#".equals(smsBody)) {
					System.out.println("远程锁屏");
					 mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
					 ComponentName compenentName = new ComponentName(context, DeviceAdminSample.class);//激活组件名
					 if(mDPM.isAdminActive(compenentName)){//判断是否激活否则会奔溃。
						 mDPM.lockNow();
					 }
					abortBroadcast();
				}
			}
		}
	}

}
