package com.safe.a360.safemobile.activity;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.dao.AntivirusDao;
import com.safe.a360.safemobile.utils.MD5Utils;

import java.util.List;

/**
 * 病毒查杀界面
 * @author BRUCE
 *
 */
public class AntivirusActivity extends AppCompatActivity {
	private TextView tv_init_virus;
	private ProgressBar progressBar1;
	private LinearLayout ll_content;
	private ScrollView scrollView;
	private int totalInstallAppSize;
	protected static final int BEGING = 1;
	protected static final int SCANING = 2;
	protected static final int FINISH = 3;
	private ImageView iv_scanning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	} 
	//在闪屏页面更新病毒库
	
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BEGING:
				tv_init_virus.setText("初始化八核杀毒引擎中");
				break;
				
			case SCANING:
				tv_init_virus.setText("正在查杀");
				TextView child = new TextView(AntivirusActivity.this);
				ScanInfo scanInfo = (ScanInfo) msg.obj;
				if(scanInfo.desc) {
					child.setTextColor(Color.RED);
					child.setText(scanInfo.appName +"       有病毒！");
				}else {
					child.setTextColor(Color.BLACK);
					child.setText(scanInfo.appName +"        安全");
				}
				ll_content.addView(child,0);
				
				scrollView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						scrollView.fullScroll(scrollView.FOCUS_DOWN);
					}
				});
				break;
			case FINISH:
				/*
				 * 扫描结束 停止扫描动画
				 */
				iv_scanning.clearAnimation();
				
				tv_init_virus.setText("查杀完毕");
				break;
			
			default:
				break;
			}
		}
	};
	
	private void initData() {
		// TODO Auto-generated method stub
		new Thread(){
			private Message message;

			@Override
			public void run() {
				
				message = Message.obtain();
				
				message.what = BEGING;
				
				//handler.sendMessage(message);
				
				PackageManager packageManager = getPackageManager();
				List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
				
				//返回一共多少应用程序
				totalInstallAppSize = packageInfos.size();
				//设置进度条最大值
				progressBar1.setMax(totalInstallAppSize);
				int currentProgressCount = 0;
				
				for (PackageInfo packageInfo : packageInfos) {
					ScanInfo scanInfo = new ScanInfo();
					String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
					scanInfo.appName = appName;
					String PackageName = packageInfo.applicationInfo.packageName;
					scanInfo.appPackName = PackageName;
					//每个应用的目录路径
					String sourceDir = packageInfo.applicationInfo.sourceDir;
					//System.out.println("--------------------");
					//System.out.println(sourceDir);
					String md5 = MD5Utils.getFileMD5(sourceDir);
					/**
					 * 判断md5值是否在数据库中
					 */
					String desc = AntivirusDao.checkFileVirus(md5);
					/**
					 * 描述信息为空 无病毒
					 */
					if(desc == null) {
						scanInfo.desc = false;
					}else {
						scanInfo.desc = true;
					}
					
					SystemClock.sleep(100);
					currentProgressCount ++;
					progressBar1.setProgress(currentProgressCount);
					
					message = Message.obtain();
					message.what = SCANING;
					message.obj = scanInfo;
					handler.sendMessage(message);
				}
				message = Message.obtain();
				message.what = FINISH;
				handler.sendMessage(message);
				//progressBar1.setVisibility(View.VISIBLE);
			};
		}.start();
	}
	static class ScanInfo {

		boolean desc;
		String appName;
		String appPackName;
		
	}
	private void initUI() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_antivirus);
		iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
		
		tv_init_virus = (TextView) findViewById(R.id.tv_init_virus);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		/**
		 * 第一个参数 表示开始角度
		 * 结束角度
		 * 参照自己
		 * 初始化旋转动画
		 */
		RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(5000);
		// 设置动画无限循环
		animation.setRepeatCount(Animation.INFINITE);
		iv_scanning.startAnimation(animation);
	}
}
