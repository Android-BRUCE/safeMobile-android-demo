package com.safe.a360.safemobile.engine;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.safe.a360.safemobile.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;


public class TaskInfoParser {

	public static List<TaskInfo> getTaskInfos(Context context) {
		PackageManager packageManager = context.getPackageManager();
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();//集合.将获取到当前应用信息存放在集合中。
		
		ActivityManager activiityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		/**
		 * getRunningAppProcesses 获取手机上所有在运行的进程
		 */
		List<RunningAppProcessInfo> appProcesses = activiityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
			TaskInfo taskInfo = new TaskInfo();
			String packageName = runningAppProcessInfo.processName;///进程名字(包名)
			taskInfo.setPackageName(packageName);
			/**
			 * 通过获取包管理器 获取应用的名字图标等信息
			 */
			try {
				// 获取到内存基本信息
				/**
				 * 这个里面一共只有一个数据
				 */
				android.os.Debug.MemoryInfo[] memeryInfo = activiityManager.getProcessMemoryInfo(new int [] {runningAppProcessInfo.pid} );
				// Dirty弄脏
				// 获取到总共弄脏多少内存(当前应用程序占用多少内存)
				int totalProvateDirty = memeryInfo[0].getTotalPrivateDirty() * 1024;
				
				taskInfo.setMemerySize(totalProvateDirty);
				
				PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
				Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
				String apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
				taskInfo.setIcon(icon);//应用图标
				taskInfo.setAppName(apkName);//应用名字
				System.out.println();
				/*
				 * 获取当前应用的标记
				 *  packageInfo.applicationInfo.flags 表示当前应用的标记（标记为0）
				 *  ApplicationInfo.FLAG_SYSTEM       表示系统应用程序（标记为1）
				 */
				int flags = packageInfo.applicationInfo.flags;
			    if((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					/*
					 * 系统应用
					 */
			    	taskInfo.setUserApp(false);
				}else {
					/*
					 * 用户应用
					 */
					taskInfo.setUserApp(true);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 *  系统核心库里面有些系统没有图标。必须给一个默认的图标
				 */
				taskInfo.setAppName(packageName);
				taskInfo.setIcon(context.getResources().getDrawable(
						android.R.drawable.ic_secure));
			}
			taskInfos.add(taskInfo);
			
		}
		return taskInfos;
		
	}
}
