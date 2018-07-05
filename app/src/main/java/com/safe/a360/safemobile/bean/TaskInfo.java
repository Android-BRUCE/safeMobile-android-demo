package com.safe.a360.safemobile.bean;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	
	private Drawable icon;
	
	private String packageName;
	
	private String appName;
	
	private long memerySize;
	
	private boolean checked;
	/**
	 * 是否是用户进程
	 */
	private boolean userApp;
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public long getMemerySize() {
		return memerySize;
	}
	public void setMemerySize(long memerySize) {
		this.memerySize = memerySize;
	}
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	
	public boolean ischecked() {
		return checked;
	}
	public void setchecked(boolean checked) {
		this.checked = checked;
	}
	@Override
	public String toString() {
		return "TaskInfo [packageName=" + packageName + ", appName=" + appName
				+ ", memerySize=" + memerySize + ", userApp=" + userApp + ", checked=" + checked +"]";
	}
	
}
