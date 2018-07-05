package com.safe.a360.safemobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safe.a360.safemobile.R;


public class SettingClickView extends RelativeLayout {
	
	private TextView tvTitle;
	private TextView tvDesc;
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);

		//chushihua
		initView();
	}
	public SettingClickView(Context context){
		super(context);
		initView();
	}
	
	
	private void initView() {
		//将自定义好的布局文件设置给当前的SettingItem作为当前SettingItem的孩子
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		//cbStatus = (CheckBox) findViewById(R.id.cb_status);	
	}
	
	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}
}
