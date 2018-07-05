package com.safe.a360.safemobile.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.bean.AppInfo;
import com.safe.a360.safemobile.engine.AppInfos;

import java.util.ArrayList;
import java.util.List;

/**
 * 软件管理
 * @author BRUCE
 *
 */
public class AppManagerAvtivity extends AppCompatActivity implements OnClickListener {
	/**
	 * xUtils 中注解方式 绑定id
	 */
	@ViewInject(R.id.tv_rom)
	private TextView tv_rom;
	@ViewInject(R.id.tv_SD)
	private TextView tv_SD;
	@ViewInject(R.id.list_view)
	private ListView list_view;
	@ViewInject(R.id.tv_apk_user_system)
	private TextView tv_apk_user_system;
	
	private List<AppInfo> appInfos;
	private List<AppInfo> userApp;
	private List<AppInfo> SystemApp;
	AppManagerAdpter adapter; 
	AppInfo clickAppInfo;
	private PopupWindow popupWindow;
	View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_uninstall:
            Intent uninstall_localIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + clickAppInfo.getApkPackageName()));
            startActivity(uninstall_localIntent);
            popupWindowDismiss();
			break;
		case R.id.ll_start:
            Intent start_localIntent = this.getPackageManager().getLaunchIntentForPackage(clickAppInfo.getApkPackageName());
            this.startActivity(start_localIntent);
            popupWindowDismiss();
		break;
		case R.id.ll_share:
            Intent share_localIntent = new Intent("android.intent.action.SEND");
            share_localIntent.setType("text/plain");
            share_localIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
            share_localIntent.putExtra("android.intent.extra.TEXT",
                    "Hi！推荐您使用软件：" + clickAppInfo.getApkName() + "下载地址:" + "http://app.flyme.cn/apps/public/detail?package_name="+clickAppInfo.getApkPackageName());
            this.startActivity(Intent.createChooser(share_localIntent, "分享"));
            //https://play.google.com/store/apps/details?id= 利用谷歌服务器分享数据，
            //在国内，只要做分享数据，手机上所有应用商店会接收这个intent
            //http://app.flyme.cn/apps/public/detail?package_name=魅族
            popupWindowDismiss();
			break;
		case R.id.ll_xiangqing://配置在setting中的.applications.InstalledAppDetails下
			//学习：通过打开一个应用详情页面，同时查看日志
            Intent detail_intent = new Intent();
            detail_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
            detail_intent.setData(Uri.parse("package:" + clickAppInfo.getApkPackageName()));
            startActivity(detail_intent);
			break;
		}	
	}
	
	private class AppManagerAdpter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userApp.size() + SystemApp.size() + 2;
		}

		@Override
		public Object getItem(int position) {//对象
			AppInfo appInfo;
			if(position == 0) {
				return null;
			}else if(position == userApp.size() + 1) {
				return null;
			}
			if(position < userApp.size() + 1) {
				appInfo = userApp.get(position - 1);
			}else {
				int location = userApp.size() + 2;
				appInfo =  SystemApp.get(position - location);
			}
			
			return appInfo;
		}

		@Override
		public long getItemId(int position) {//iten位置id
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			AppInfo appInfo = null ;
			//添加text条目
			if(position == 0) {
				//用户应用程序
				TextView textView = new TextView(AppManagerAvtivity.this);
				textView.setText("用户程序：(" + userApp.size() + "个)");
				textView.setBackgroundColor(Color.GREEN);
				textView.setTextColor(Color.RED);
				
				return textView;
			}
			else if(position == userApp.size() + 1){
				//系统程序
				TextView textView = new TextView(AppManagerAvtivity.this);
				textView.setText("系统程序：(" + SystemApp.size() + "个)");
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.RED);
				
				return textView;
			}
			//根据position拆分分为两部分信息去获取并设置内容
			if(position < userApp.size() + 1) {
				appInfo = userApp.get(position - 1);
			}else {
				int location = userApp.size() + 2 ;
				appInfo =  SystemApp.get(position - location);
			}
			
			if(convertView != null && convertView instanceof LinearLayout) {//防止TextView加入。导致复用TextView。
				holder = (ViewHolder) convertView.getTag();
			}else {
				convertView = View.inflate(AppManagerAvtivity.this, R.layout.item_app_manager, null);
				holder = new ViewHolder();
				holder.iv_iconn = (ImageView)convertView.findViewById(R.id.iv_iconn);
				holder.tv_apk_sieze = (TextView) convertView.findViewById(R.id.tv_apk_size);
				holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				
				convertView.setTag(holder);
			}
			//AppInfo appInfo = appInfos.get(position);
			holder.iv_iconn.setPadding(5, 5, 5, 5);
			//此方法在安卓4.1以上有效。在资源文件中默认icon不可设为src，否则会重叠
			//holder.iv_iconn.setBackground(appInfo.getIcon());
			//过时，但是低版本有效。
			holder.iv_iconn.setBackgroundDrawable(appInfo.getIcon());
			//System.out.println("这是icon=======" + appInfo.getIcon());
			holder.tv_apk_sieze.setText(Formatter.formatFileSize(AppManagerAvtivity.this, appInfo.getApkSize()));
			if(appInfo.isRom()) {
				holder.tv_location.setText("手机内存");
			}else {
				holder.tv_location.setText("SD卡");
			}
			holder.tv_name.setText(appInfo.getApkName());
			return convertView;
		}
		
	}
	static class ViewHolder {
		ImageView iv_iconn;
		TextView tv_apk_sieze;
		TextView tv_location;
		TextView tv_name;
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			adapter = new AppManagerAdpter();
			list_view.setAdapter(adapter);
			super.handleMessage(msg);
		}
	};
	/**
	 * 初始化数据
	 */
	private void initData() {
		//获取数据是耗时操作
		new Thread() {
			@Override
			public void run() {
				super.run();
				appInfos = AppInfos.getAppInfos(AppManagerAvtivity.this);
				userApp = new ArrayList<AppInfo>();
				SystemApp = new ArrayList<AppInfo>();
				//appInfos拆分用户和系统app集合
				for (AppInfo appinfo : appInfos) {
					if(appinfo.isUserApp()) {
						userApp.add(appinfo);
					}else {
						SystemApp.add(appinfo);
					}
					
				}
				
				handler.sendEmptyMessage(0);
				
			}
		}.start();
		
	}
/**
 * 初始化UI。
 */
	private void initUI() {
		setContentView(R.layout.activity_app_manager);
	    ViewUtils.inject(this); //注入view和事件
	    //获取到Rom剩余空间。
	    long rom_freeSpace = Environment.getDataDirectory().getFreeSpace();
	    //获取SD卡剩余空间
	    long sd_freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
	    
	    tv_rom.setText("内存可用：" + Formatter.formatFileSize(this, rom_freeSpace));//格式化获取的数据
	    tv_SD.setText("SD卡可用：" + Formatter.formatFileSize(this, sd_freeSpace));
	    
	    //注册程序被卸载成功的广播
	    UninstallBroadcastReceiver receiver = new UninstallBroadcastReceiver();
	    IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
	    filter.addDataScheme("package");//scheme 相当于http://中的http；根据package去执行
	    registerReceiver(receiver, filter);
	    
	    list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * @param view 
			 * @param firstVisibleItem 第一个可见条目的位置
			 * @param visibleItemCount 一页可展示的条目
			 * @param totalItemCount 总共的item的条数
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
				popupWindowDismiss();//滑动时候把popupWindow dismiss掉。
				if(userApp != null && SystemApp != null) {
					if( firstVisibleItem > (userApp.size())) {
						tv_apk_user_system.setText("系统程序：(" + SystemApp.size() + "个)");
					}else {
						tv_apk_user_system.setText("用户程序：(" + userApp.size() + "个)");
					}
				}
			}
		});
	    
	    list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {
			
				Object obj = list_view.getItemAtPosition(position);
				if(obj != null && obj instanceof AppInfo) {
					
					clickAppInfo = (AppInfo)obj;//在重写的onclick（）中需要获取当前点击的条目的包名
					View contentView = View.inflate(AppManagerAvtivity.this, R.layout.item_popup, null);
					
					
					LinearLayout ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
					LinearLayout ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);
					LinearLayout ll_share = (LinearLayout) contentView.findViewById(R.id.ll_share);
					LinearLayout ll_xiangqing = (LinearLayout) contentView.findViewById(R.id.ll_xiangqing);
					/**
					 * 让当前activity实现OnClickListener接口并重写onClick方法。实现让popupWindow
					 * 的click在一个onclick中实现
					 */
					ll_uninstall.setOnClickListener(AppManagerAvtivity.this);
					ll_start.setOnClickListener(AppManagerAvtivity.this);
					ll_share.setOnClickListener(AppManagerAvtivity.this);
					ll_xiangqing.setOnClickListener(AppManagerAvtivity.this);
						
					popupWindowDismiss();//移除之前的popupWindow如果有
						
						
					//popupWindow对象
					popupWindow = new PopupWindow(contentView, -2, -2);//-2表示包裹内容。
					//需要注意：使用PopupWindow 必须设置背景(透明)。不然没有动画
					popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					int[] location = new int[2];//showAtLocation(Y)
					view.getLocationInWindow(location);//获取view展示到窗体上面的位置
					popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 70, location[1]);
					
					ScaleAnimation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					animation.setDuration(1000);
					contentView.startAnimation(animation);
				}
			}
		});
	}
	private void popupWindowDismiss() {
		if(popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	//程序被卸载广播
	class UninstallBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			initData();
			Toast.makeText(AppManagerAvtivity.this, clickAppInfo.getApkName() + "卸载成功！", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		popupWindowDismiss();//activity销毁时候将popupWindow销毁
		super.onDestroy();
	}
}
