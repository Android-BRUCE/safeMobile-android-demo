package com.safe.a360.safemobile.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.safe.a360.safemobile.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取app信息
 * @author BRUCE
 *
 */
public class AppInfos {
	/**
	 * 
	 * @param context
	 * @return
	 * 返回所有应用程序的信息集合：List<AppInfo
	 */
	public static List<AppInfo> getAppInfos(Context context) {
		
		List<AppInfo> packageAppInfos = new ArrayList<AppInfo>();
		//获取包的管理者
		PackageManager manager = context.getPackageManager();
		//获取到安装包,app的信息通过包来管理
		List<PackageInfo> installedPackages = manager.getInstalledPackages(0);//flag : 安卓默认定义用户appflag为0.
		/**
		 * ApplicationInfo：主要是AndroidManifest.xml 文件中<Application/>标签对应的相应信息的封装
		 * PackageInfo：主要包含某个Package全部信息，这些信息主要是这个Package对应的AndroidManifest.xml中的信息 
		 */
		//manager.getInstalledApplications(flags);
		for (PackageInfo installedPackage : installedPackages) {
			AppInfo appInfo = new AppInfo();
			
			//获取到app的图标
			Drawable drawable = installedPackage.applicationInfo.loadIcon(manager);
			//获取app名字
			String apkName = installedPackage.applicationInfo.loadLabel(manager).toString();
			//获取apk的包名 
			String packageName = installedPackage.packageName;
			//获取apk资源的路径
			String apkSourceDir = installedPackage.applicationInfo.sourceDir;
			
			File file = new File(apkSourceDir);
			//apk的长度；
			long apksize = file.length();
			
			appInfo.setApkName(apkName);
			appInfo.setApkPackageName(packageName);
			appInfo.setApkSize(apksize);
			appInfo.setIcon(drawable);
			//获取到安装的app的标记
			int flags = installedPackage.applicationInfo.flags;
			//系统app flag 为1，用户为0
			if((flags & ApplicationInfo.FLAG_SYSTEM) != 0 ) {//表示是系统app
				appInfo.setUserApp(false);
			}else{
				appInfo.setUserApp(true);
			}
			//app放置位置
			if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				appInfo.setRom(false);
			}else {
				appInfo.setRom(true);
			}
			
			//appInfo.setUserApp(userApp);
			
			packageAppInfos.add(appInfo); 
		}
		
		return packageAppInfos;
		
	}
}
