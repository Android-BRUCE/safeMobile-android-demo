package com.safe.a360.safemobile.activity;


import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.bean.TaskInfo;
import com.safe.a360.safemobile.engine.TaskInfoParser;
import com.safe.a360.safemobile.utils.SharePreferenceUtils;
import com.safe.a360.safemobile.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {
	 @ViewInject(R.id.tv_task_count)
	 private TextView tv_task_count;
	 @ViewInject(R.id.tv_task_memory)
	 private TextView tv_task_memory;
	 @ViewInject(R.id.lv_list_process)
	 private ListView lv_list_process;
	 private TaskManagerAdapter adapter; 
	 private List<TaskInfo> taskInfos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {//Bundle 存储值。比如游戏存档
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//preferences = getSharedPreferences("config", 0);
		initUI();
		initData();
	}
	/**
	 * 从设置返回这个页面 调用这个方法 重获焦点
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(adapter != null) {
			adapter.notifyDataSetChanged(); 
		}

	}
	
	private class TaskManagerAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			/**
			 * 在openSetting按钮的页面中设置是否要显示系统应用
			 */
			//boolean b = preferences.getBoolean("is_show_system", false);
			boolean b = SharePreferenceUtils.getBoolean(TaskManagerActivity.this, "is_show_system", false);
			if(b) {
				return userTaskInfo.size() + 1 + systemInfo.size() + 1;
			}else {
				return userTaskInfo.size() + 1;
			}
		}

		@Override
		public Object getItem(int position) {
			if(position == 0) {
				return null;
			}else if(position == userTaskInfo.size()
					+ 1) {
				return null;
			}
			TaskInfo taskinfo = taskInfos.get(position);
			if(position < userTaskInfo.size() + 1) {
				taskinfo = userTaskInfo.get(position - 1);
			}else {
				int location = userTaskInfo.size() + 2 ;
				taskinfo =  systemInfo.get(position - location);
			}
			/**
			 * 返回条目对象 给条目监听拿到此条目对象进行操作
			 */
			return taskinfo;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			TaskInfo taskinfo;
			if(position == 0) {
				//用户应用程序
				TextView textView = new TextView(getApplicationContext());
				textView.setText("用户程序：(" + userTaskInfo.size() + "个)");
				textView.setBackgroundColor(Color.GREEN);
				textView.setTextColor(Color.RED);
				
				return textView;
			}
			else if(position == userTaskInfo.size() + 1){
				//系统程序
				TextView textView = new TextView(TaskManagerActivity.this);
				textView.setText("系统程序：(" + systemInfo.size() + "个)");
				textView.setBackgroundColor(Color.GREEN);
				textView.setTextColor(Color.RED);
				
				return textView;
			}
			if(convertView != null  && convertView instanceof LinearLayout) {
				holder = (ViewHolder) convertView.getTag();
			}else {
				convertView = View.inflate(TaskManagerActivity.this, R.layout.item_task_manager, null);
				holder = new ViewHolder();
				holder.tv_apkname = (TextView)convertView.findViewById(R.id.tv_apkname);
				holder.tv_memery_size = (TextView) convertView.findViewById(R.id.tv_memery_size);
				holder.tv_apk_status = (CheckBox) convertView.findViewById(R.id.tv_apk_status);
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				
				convertView.setTag(holder);
			}
	
			
			
			/*
			 * 根据position拆分分为两部分信息去获取并设置内容
			 */
			if(position < userTaskInfo.size() + 1) {
				taskinfo = userTaskInfo.get(position - 1);
			}else {
				//int location = userTaskInfo.size() + 2 ;
				int location = position - 1 - userTaskInfo.size() - 1;
				taskinfo =  systemInfo.get(location);
			}
			holder.iv_icon.setImageDrawable(taskinfo.getIcon());
			holder.tv_apkname.setText(taskinfo.getAppName());
			holder.tv_memery_size.setText("内存占用" + Formatter.formatFileSize(TaskManagerActivity.this, taskinfo.getMemerySize()));
			/*
			 * 感觉相当于初始化，不然当点击条目时候为null 报错。
			 */
			if(taskinfo.ischecked()) {
				holder.tv_apk_status.setChecked(true);
//				System.out.println("------------初始化判断是否打勾了");
			}else {
				holder.tv_apk_status.setChecked(false);
			}
			if(taskinfo.getPackageName().equals(getPackageName())){
				//隐藏
				holder.tv_apk_status.setVisibility(View.INVISIBLE);
			}else{
				//显示
				holder.tv_apk_status.setVisibility(View.VISIBLE);
			}

			return convertView;
		}
		
	}
	
	private static class ViewHolder {
		ImageView iv_icon;
		TextView tv_memery_size;
		TextView tv_apkname;
		CheckBox tv_apk_status;
	}
	
	private List<TaskInfo> userTaskInfo;
	private List<TaskInfo> systemInfo;
	private List<TaskInfo> delList;
	private List<TaskInfo> delList2;
	private int totalProcesses;
	private long availMem;
	private long totalMem;
	//private SharedPreferences preferences;
	
	private void initData() {
		new Thread() {


			@Override
			public void run() {
				/*
				 * 拿到当前系统正在运行的应用所有信息
				 */
				taskInfos = TaskInfoParser.getTaskInfos(TaskManagerActivity.this);
				
				userTaskInfo = new ArrayList<TaskInfo>();
				
				systemInfo = new ArrayList<TaskInfo>();
				
				for (TaskInfo taskinfo : taskInfos) {
					if (taskinfo.isUserApp()){
						userTaskInfo.add(taskinfo);
					}else {
						systemInfo.add(taskinfo);
					}
				}
				/**
				 * runOnUiThread 不用发消息方式刷新ui
				 * 另一种是 用sendmessage 方式向handler发送消息
				 */
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						adapter = new TaskManagerAdapter();
						lv_list_process.setAdapter(adapter);
						//System.out.println("这是适配器");
					}
				});
			}
		}.start();
		
	}

	private void initUI() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_task_manager);
		ViewUtils.inject(this);
		totalProcesses = SystemInfoUtils.getProcessCount(this);
		tv_task_count.setText("总进程" + totalProcesses + "个");
		availMem = SystemInfoUtils.getAvailMem(this);
		totalMem = SystemInfoUtils.getTotalMem();
		tv_task_memory.setText("剩余/总内存：" + Formatter.formatFileSize(TaskManagerActivity.this, availMem) +
				"/" + Formatter.formatFileSize(TaskManagerActivity.this, totalMem));
		
		lv_list_process.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				//拿到当前点击对象  从adapter中的item拿到
				Object obj = lv_list_process.getItemAtPosition(position);
//				Toast.makeText(TaskManagerActivity.this, "这是点击条目", Toast.LENGTH_SHORT).show();
				/*
				 * 判断当前条目勾选状态 
				 * 如果已经是勾选 则false置为不勾选
				 */
				if(obj != null && obj instanceof TaskInfo) {
					
					TaskInfo clicTaskInfo = (TaskInfo) obj;
					ViewHolder	holder = (ViewHolder) view.getTag();
					 if(clicTaskInfo.ischecked()){
						 clicTaskInfo.setchecked(false);
						 holder.tv_apk_status.setChecked(false);
					 }else {
						 clicTaskInfo.setchecked(true);
						 holder.tv_apk_status.setChecked(true);
					 }
				}
			}
		});
//		long totalMem = 0;
//		/**
//		 * 此处无法再低版本上运行，
//		 * 解决办法，直接读取/proc/meminfo的信息获取内存信息（总大小）。adb shell 可看cat meminfo
//		 * MemTotal:         215132 kB
//		 */
//		//long totalMem = managerInfo.totalMem;
//		try {
//			FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
//			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//			String s = br.readLine().toString();
//			char[] a = s.toCharArray();
//			StringBuilder sb = new StringBuilder();
//			for (char c : a) {
//				if(c >= '0' && c <= '9' ) {
//					sb.append(c);
//				}
//			}
//			totalMem = Long.parseLong(sb.toString()) * 1024;
//			//System.out.println("内存数据数是： " + s);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	/**
	 * 全选
	 * @param view
	 */
	public void selectAll(View view) {
		boolean b = SharePreferenceUtils.getBoolean(TaskManagerActivity.this, "is_show_system", false);
		/**
		 * 设置选择系统应用隐藏 则全选不能选中
		 */
		if(b) {
			//return userTaskInfo.size() + 1 + systemInfo.size() + 1;
			for(TaskInfo taskinfo : userTaskInfo) {
				if(taskinfo.getPackageName().equals(getPackageName())){
					continue;
				}
				taskinfo.setchecked(true);
			}
			for(TaskInfo taskinfo : systemInfo) {
				taskinfo.setchecked(true);
			}
		}else {
			for(TaskInfo taskinfo : userTaskInfo) {
				if(taskinfo.getPackageName().equals(getPackageName())){
					continue;
				}
				taskinfo.setchecked(true);
			}
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * 反选择
	 * @param view
	 */
	public void selectoppsite(View view) {
		for(TaskInfo taskinfo : taskInfos) {
			taskinfo.setchecked(!taskinfo.ischecked());
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * 清理进程
	 */
	public void killProcess(View view) {
		int totalcount = 0;
		int availM = 0;
		/**
		 * 1.得到进程管理器
		 * 2.杀死进程
		 */
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		delList = new ArrayList<TaskInfo>();
		delList2 = new ArrayList<TaskInfo>();
		for(TaskInfo taskinfo : userTaskInfo) {
			if(taskinfo.ischecked()) {

				activityManager.killBackgroundProcesses(taskinfo.getPackageName());
				delList.add(taskinfo);
				totalcount ++;
				System.out.println(taskinfo.ischecked());
				availM += taskinfo.getMemerySize();
			}
		}
		for(TaskInfo taskinfo : systemInfo) {

			if(taskinfo.ischecked()) {

				activityManager.killBackgroundProcesses(taskinfo.getPackageName());
				delList2.add(taskinfo);
				totalcount ++;
				availM += taskinfo.getMemerySize();
			}
		}
		/**
		 * 迭代的时候不能删除内容
		 */
		userTaskInfo.removeAll(delList);
		systemInfo.removeAll(delList2);
		totalProcesses -= totalcount;
		tv_task_count.setText("总进程" + totalProcesses + "个");
		
		Toast.makeText(this, "共清理了" + totalcount + "个后台进程和"+
		Formatter.formatFileSize(TaskManagerActivity.this, availM) + "内存", Toast.LENGTH_SHORT).show();
		
		tv_task_memory.setText("剩余/总内存：" + Formatter.formatFileSize(TaskManagerActivity.this, availMem + availM) +
				"/" + Formatter.formatFileSize(TaskManagerActivity.this, totalMem));
		adapter.notifyDataSetChanged();
	}
	/**
	 * 设置
	 * @param view
	 */
	public void openSetting(View view) {
		Intent intent = new Intent(this,TaskManagerSettingActivity.class);
		startActivity(intent);
	}
}
