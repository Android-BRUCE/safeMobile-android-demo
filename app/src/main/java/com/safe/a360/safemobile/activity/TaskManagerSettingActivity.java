package com.safe.a360.safemobile.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.service.KillerProcessService;
import com.safe.a360.safemobile.utils.ServiceStatusUtils;
import com.safe.a360.safemobile.utils.SharePreferenceUtils;

public class TaskManagerSettingActivity extends AppCompatActivity {

	public CheckBox box2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
	}

	private void initUI() {
		setContentView(R.layout.activity_manager_setting);
		CheckBox box = (CheckBox) findViewById(R.id.cb_check);
		TextView textView = (TextView) findViewById(R.id.tv_xianshi);
		//preferences = getSharedPreferences("config",0);
		box.setChecked(SharePreferenceUtils.getBoolean(TaskManagerSettingActivity.this, "is_show_system", false));
		
		box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					SharePreferenceUtils.saveBoolean(TaskManagerSettingActivity.this, "is_show_system", true);
				}else{
					SharePreferenceUtils.saveBoolean(TaskManagerSettingActivity.this, "is_show_system", false);
				}
			}
		});
		
		
		box2 = (CheckBox) findViewById(R.id.cb_check_kill_process);
		
		box2.setChecked(SharePreferenceUtils.getBoolean(TaskManagerSettingActivity.this, "is_clean_app", false));
		final Intent intent = new Intent(this, KillerProcessService.class);
		box2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					startService(intent);
					SharePreferenceUtils.saveBoolean(TaskManagerSettingActivity.this, "is_clean_app", true);
				}else {
					stopService(intent);
					SharePreferenceUtils.saveBoolean(TaskManagerSettingActivity.this, "is_clean_app", false);
				}
			}
		});
	}
	/**
	 * onCreate->onStart->onResume->onPsuse->onStop->onRestart->onStart
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(ServiceStatusUtils.isServiceRunning(TaskManagerSettingActivity.this, "com.ncepu.mobilesafe.service.KillerProcessService")) {
			box2.setChecked(true);
		}else {
			box2.setChecked(false);
		}
	}
}
