package com.safe.a360.safemobile.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.utils.SmsUtils;

/**
 * 高级工具页面
 * 
 * @author Bruce
 * 
 */
public class AToolsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	Handler handle = new Handler() {
    	public void handleMessage(Message msg) {
    		
    	}
    };
	private ProgressDialog pd;
	//private ProgressBar pd2;
	/**
	 * 归属地查询
	 * 
	 * @param view
	 */
	public void numberAddressQuery(View view) {
		startActivity(new Intent(this, AddressActivity.class));
	}
	
	/**
	 * 备份短信
	 * @param view
	 */
	public void backUpSms(View view){
		pd = new ProgressDialog(AToolsActivity.this);
		pd.setTitle("短信备份");
		pd.setMessage("正在备份请稍等... ...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();

      //  pd2 = new ProgressBar(AToolsActivity.this);
       // pd2.set

		//还可以在xml中加入prograssbar，并很方便的展示、
		Thread th = new Thread() {
			@Override
			public void run() {
				
				boolean result = SmsUtils.backUp(AToolsActivity.this,new SmsUtils.backUpCallBackSms() {

					@Override
					public void onBackUpSms(int process) {
						pd.setProgress(process);
					}
					
					@Override
					public void before(int count) {
						pd.setMax(count);
						
					}
				});
				
				//Message msg = Message.obtain();
				//handle.sendMessage(msg);//这个发送的消息方法只管发送，由系统完成从消息队列取出消息。
				
				if(result) {
					Looper.prepare();//主动从消息队列中取出消息
					Toast.makeText(AToolsActivity.this, "备份成功", Toast.LENGTH_SHORT).show();//不能再子线程中显示
					Looper.loop();
				}else {
					Looper.prepare();
					Toast.makeText(AToolsActivity.this, "备份失败", Toast.LENGTH_SHORT).show();
					Looper.loop();
				}
				pd.dismiss();
			}
		};
		th.start();
	}
	
	/*
	 * 程序锁
	 * @param
	 */
	public void appLock(View view){
		Intent intent = new Intent(this,AppLockActivity.class);
		startActivity(intent);
	}
}
