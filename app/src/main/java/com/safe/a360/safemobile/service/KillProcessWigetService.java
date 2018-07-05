package com.safe.a360.safemobile.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.receiver.MyAppWidgetProvider;
import com.safe.a360.safemobile.utils.SystemInfoUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 桌面小部件清理后台服务
 * @author BRUCE
 *
 */
public class KillProcessWigetService extends Service {

	private AppWidgetManager appWidgetManager;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//管理者 桌面小部件
		appWidgetManager = AppWidgetManager.getInstance(this);
		/**
		 * 每隔五秒 更新
		 * 初始化 定时任务
		 */
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				//System.out.println("KillProcessWidgetService");
				/**
				 * 获取AppWidget对应的视图
				 * RemoteViews 初始化远程view
				 * remote 远程 （因为 小部件运行在桌面上 相当于运行在别的应用中）
				 */
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				/**
				 * 1.第一个参数 上下文
				 * 2.第二参数 当前按一个广播进行处理当前桌面小控件
				 * Component 部件 ；零件
				 */
				ComponentName provider = new ComponentName(KillProcessWigetService.this, MyAppWidgetProvider.class);
				int processCount = SystemInfoUtils.getProcessCount(getApplicationContext());
				views.setTextViewText(R.id.process_count, "正在运行软件：" + String.valueOf(processCount));
				long availMem = SystemInfoUtils.getAvailMem(getApplicationContext());
				views.setTextViewText(R.id.process_memory, "可用内存" + android.text.format.Formatter.formatFileSize(KillProcessWigetService.this, availMem));
				/**
				 * 设置点击事件
				 * 设置响应 “按钮(bt_refresh)” 的intent
				 */
				Intent intent = new Intent();
				//设置一个隐式意图
				intent.setAction("com.ncepu.mobilesafe");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
			/**
			 * 刷新控件
			 */
				appWidgetManager.updateAppWidget(provider, views);
				
				
			}
		};
		timer.schedule(task, 0, 5000);//从0开始每隔五秒开始刷新
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
