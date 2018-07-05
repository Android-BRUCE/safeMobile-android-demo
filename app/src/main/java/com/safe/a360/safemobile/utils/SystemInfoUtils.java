package com.safe.a360.safemobile.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SystemInfoUtils {
	/**
	 * 获取系统进程数
	 * @param context
	 * @return
	 */
	public static int getProcessCount(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
		return runningProcesses.size();
		
	}
	/**
	 * 获取系统可用内存
	 * @param context
	 * @return 
	 */
	
	public static long getAvailMem(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo managerInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(managerInfo);
		return managerInfo.availMem;
	}
	/**
	 * 获取系统总内存大小
	 * @return
	 */
	public static long getTotalMem(){
	
	long totalMem = 0;
	/**
	 * 此处无法再低版本上运行，
	 * 解决办法，直接读取/proc/meminfo的信息获取内存信息（总大小）。adb shell 可看cat meminfo
	 * MemTotal:         215132 kB
	 */
	//long totalMem = managerInfo.totalMem;
	try {
		FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String s = br.readLine().toString();
		char[] a = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : a) {
			if(c >= '0' && c <= '9' ) {
				sb.append(c);
			}
		}
		totalMem = Long.parseLong(sb.toString()) * 1024;
		return totalMem;
		//System.out.println("内存数据数是： " + s);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0; 
	}
}
