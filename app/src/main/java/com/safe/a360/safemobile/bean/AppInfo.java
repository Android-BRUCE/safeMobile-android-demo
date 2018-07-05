package com.safe.a360.safemobile.bean;

import android.graphics.drawable.Drawable;

/**
 * 应用程序信息的java bean。
 * @author BRUCE
 *
 */
public class AppInfo {
	//Drawable只要可以丢到Drawable中都可以定义为Drawable
	/**
	 * 程序的图标
	 */
	private Drawable icon;
	/**
	 * 程序名字
	 */
	private String apkName;
	/**
	 * 程序大小
	 */
	private long apkSize;
	/**
	 * 
	 * 判断是系统还是用户app
	 * @see
	 * false 为系统app，true 为 用户app
	 */
	private boolean userApp;
	/**
	 * 包名
	 */
	private String apkPackageName;
	/**
	 * app放置的位置
	 */
	private boolean  isRom;
	
	
	public boolean isRom() {
		return isRom;
	}
	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public long getApkSize() {
		return apkSize;
	}
	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	public String getApkPackageName() {
		return apkPackageName;
	}
	public void setApkPackageName(String apkPackageName) {
		this.apkPackageName = apkPackageName;
	}
	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", apkName=" + apkName + ", apkSize="
				+ apkSize + ", userApp=" + userApp + ", apkPackageName="
				+ apkPackageName + ", isRom=" + isRom + "]";
	}

	
}
