package com.safe.a360.safemobile.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.fragment.LockedFragment;
import com.safe.a360.safemobile.fragment.UnLockFragment;


public class AppLockActivity extends FragmentActivity implements OnClickListener {
	
	FrameLayout fl_content;
	TextView tv_unlock;
	TextView tv_lock;
	FragmentManager fragmentmanager;
	FragmentTransaction transaction;
	UnLockFragment unLockFragment;
	LockedFragment lockedFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initUI();
	}

	private void initUI() {
		
		setContentView(R.layout.acticity_app_lock);
		fl_content = (FrameLayout) findViewById(R.id.fl_content);
		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_lock = (TextView) findViewById(R.id.tv_lock);
		
		tv_unlock.setOnClickListener(this);
		tv_lock.setOnClickListener(this);
		/**
		 * 取得fagment管理者，并开启事务
		 */
		fragmentmanager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentmanager.beginTransaction();
		
		
		unLockFragment = new UnLockFragment();
		lockedFragment = new LockedFragment();
		/**
		 * 替换界面
		 * 1 需要替换的界面的id
		 * 2具体指某一个fragment的对象
		 */
		transaction.replace(R.id.fl_content, unLockFragment).commit();
	}
	
	@Override
	public void onClick(View v) {
		FragmentTransaction transaction = fragmentmanager.beginTransaction();
		switch (v.getId()) {
		case R.id.tv_unlock:
			tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
			tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);
			transaction.replace(R.id.fl_content, unLockFragment).commit();

			System.out.println("切换到unFragment");
			break;
		case R.id.tv_lock:
			tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
			tv_lock.setBackgroundResource(R.drawable.tab_right_default);
			transaction.replace(R.id.fl_content, lockedFragment).commit();
			System.out.println("切换到lockFragment");
			break;
		}
		//transaction.commit();
		
	}
	
}
