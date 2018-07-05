package com.safe.a360.safemobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.service.CallSafeServiceActivity;
import com.safe.a360.safemobile.service.QueryAddressService;
import com.safe.a360.safemobile.utils.ServiceStatusUtils;
import com.safe.a360.safemobile.view.SettingClickView;
import com.safe.a360.safemobile.view.SettingItem;


/**
 * 手机设置页面
 * @author BRUCE
 *
 */
public class SettingActivity extends Activity {
	private SettingItem siUpdata;
	SharedPreferences mpref;
	private SettingItem siQuery;
	private SettingClickView styleClickView;
	final String[] items = new String[] { "卫士蓝", "金属灰", "苹果绿" , "活力橙","半透明"};
	private SettingClickView settingClickView;
	private SettingItem si_callsafe;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.seting_activity);
	
	mpref = getSharedPreferences("config", MODE_PRIVATE);
	initUpdateView();//初始化自动更新设置
	initAddressView();//初始化归属地checkbox设置
	initStyleView();//归属地提示风格窗口
	initAddressLocation();//修改归属地提示框的显示位置
	initCallSafe();//初始化黑名单。
	}
/**
 * 初始化黑名单设置。
 */
	private void initCallSafe() {
		si_callsafe = (SettingItem) findViewById(R.id.si_callsafe);
		boolean service = ServiceStatusUtils.isServiceRunning(this, "com.ncepu.mobilesafe.service.CallSafeServiceActivity");
		if(service) {
			si_callsafe.setChecked(true);
		}else {
			si_callsafe.setChecked(false);
		}
		//监听手动点击设置。
		si_callsafe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(si_callsafe.isChecked()){
					si_callsafe.setChecked(false);
					stopService(new Intent(SettingActivity.this, CallSafeServiceActivity.class));
				}else {
					si_callsafe.setChecked(true);
					startService(new Intent(SettingActivity.this, CallSafeServiceActivity.class));
				}
				
			}
		});
	}

	/**
	 * 初始化自动更新设置
	 */
	private void initUpdateView(){
		
		siUpdata = (SettingItem) findViewById(R.id.si_update);
		//拿到之前用户设置过的auto_update的值。用于onClick的监听。
		boolean autoUpdate = mpref.getBoolean("auto_update", true);
		
		if(autoUpdate) {
			siUpdata.setChecked(true);
		}else {
			siUpdata.setChecked(false);
		}
		
		siUpdata.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 判断当前的勾选状态
				if(siUpdata.isChecked()) {
					//设置为false
					siUpdata.setChecked(false);
					//更新sharepreference
					mpref.edit().putBoolean("auto_update", false).commit();
				}else {
					//设置为true
					siUpdata.setChecked(true);
					//更新sharepreference
					mpref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});	
	}
	/*
	 * 初始化归属地checkbox设置
	 */
	private void initAddressView() {
		siQuery = (SettingItem) findViewById(R.id.si_queryphone);
		//因为归属地浮动窗口写在queryAddressService服务中，所以判断服务是否已经开启了，进行自动更新开关是否开启
		boolean service = ServiceStatusUtils.isServiceRunning(this, "com.ncepu.mobilesafe.service.QueryAddressService");
		if(service) {
			siQuery.setChecked(true);
		}else {
			siQuery.setChecked(false);
		}
		//监听手动点击设置。
		siQuery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siQuery.isChecked()){
					siQuery.setChecked(false);
					stopService(new Intent(SettingActivity.this, QueryAddressService.class));
				}else {
					siQuery.setChecked(true);
					startService(new Intent(SettingActivity.this, QueryAddressService.class));
				}
				
			}
		});
	}
	/**
	 * 归属地提示风格窗口
	 */
	private void initStyleView(){
		styleClickView = (SettingClickView) findViewById(R.id.si_style);
		int style = mpref.getInt("address_style", 0);
		styleClickView.setDesc(items[style]);
		styleClickView.setTitle("归属地提示框风格");
		styleClickView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSingChoiceDialog();
			}
		});
	}
	
	/**
	 * 选择提示框弹出
	 */
	protected void showSingChoiceDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new  AlertDialog.Builder(this);
		builder.setTitle("归属地提示框风格");
		int checkedstyle = mpref.getInt("address_style", 0);
		
		builder.setSingleChoiceItems(items, checkedstyle, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			mpref.edit().putInt("address_style", which).commit();
			dialog.dismiss();
			styleClickView.setDesc(items[which]);//同时更新组合控件的ui描述
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	/**
	 * 修改归属地提示框的显示位置。
	 * initAddressLocation
	 */
	private void initAddressLocation() {
		settingClickView = (SettingClickView) findViewById(R.id.si_addresslocation);
		settingClickView.setTitle("归属地提示框显示位置");
		settingClickView.setDesc("设置归属地提示框的显示位置");
		
		settingClickView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
				
			}
		});
	}
}
