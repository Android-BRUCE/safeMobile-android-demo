package com.safe.a360.safemobile.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.util.List;

/**
 * 桌面小控件 |清理后台所有进程服务
 * @author BRUCE
 *
 */
public class KillProcessWidgetReveive extends BroadcastReceiver {

	private ActivityManager activityManager;
	private List<RunningAppProcessInfo> process;

	@Override
	public void onReceive(Context context, Intent intent) {
		int clearncount = 0;
		activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		process = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo p : process) {
			activityManager.killBackgroundProcesses(p.processName);
			clearncount ++;
		}
		Toast.makeText(context, "一共清理" + clearncount, Toast.LENGTH_SHORT).show();
	}

}
