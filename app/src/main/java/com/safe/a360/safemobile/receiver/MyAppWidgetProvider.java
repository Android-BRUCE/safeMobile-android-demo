package com.safe.a360.safemobile.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.safe.a360.safemobile.service.KillProcessWigetService;

/**
 * 
 * 创建桌面小部件的步骤：
 * 1 需要在清单文件里面配置元数据
 * 2 需要配置当前元数据里面要用到xml  
 *   res/xml
 * 3 需要配置一个广播接受者
 * 4 实现一个桌面小部件的xml 
 *   (根据需求。桌面小控件涨什么样子。就实现什么样子)
 *
 *1.onEnabled-》onReceive-》onUpdate-》
 *
 */
public class MyAppWidgetProvider extends AppWidgetProvider {
	/**
	 *广播创建时候调用
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}
	/**
	 * 第一次创建的时候才会调用当前的生命周期的方法
	 * 
	 * 广播的生命周期只有10秒钟。
	 * 不能做耗时的操作
	 */
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		Intent intent = new Intent(context,KillProcessWigetService.class);
		context.startService(intent);
	}
    /**
     * 当桌面上面所有的桌面小控件都删除的时候才调用当前这个方法
     */
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Intent intent = new Intent(context,KillProcessWigetService.class);
		context.stopService(intent);
	}
    /**
     * 每次删除桌面小控件的时候都会调用的方法
     */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}
	/**
	 *  每次有新的桌面小控件生成的时候都会调用
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
