package com.safe.a360.safemobile.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;
import java.util.Timer;

/**
 * 锁屏清理后台
 * @author BRUCE
 *
 */
public class KillerProcessService extends Service {

	private LockScreenReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class LockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> process = manager.getRunningAppProcesses();
			for (RunningAppProcessInfo runningAppProcessInfo : process) {
				
				manager.killBackgroundProcesses(runningAppProcessInfo.processName);
			}
		}
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new LockScreenReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
		Timer timer = new Timer();
//		TimerTask task = new TimerTask() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				//业务逻辑	
//			}
//		};
//		/**
//		 * 进行调度
//		 */
//		timer.schedule(task, 1000,1000);
	}
	private void ondestory() {
		// 当应用退出 需要反注册
		unregisterReceiver(receiver);
		receiver = null;
	}

}
