package com.safe.a360.safemobile.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.safe.a360.safemobile.dao.BlackNumberDao;

import java.lang.reflect.Method;

/**
 * 通讯卫士，号码拦截。
 * @author BRUCE
 *
 */
public class CallSafeServiceActivity extends Service {

	private BlackNumberDao dao;
	private InterceptReceiver receiver;
	private TelephonyManager telephonyManager;
	private MyPhoneStateListener listener;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dao = new BlackNumberDao(this);//初始化黑名单查询
		
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//电话监听器需要call——phone权限
		
		//初始化短信广播
		receiver = new InterceptReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
	}
	/**
	 * 继承电话监听器的PhoneStateListener类
	 * @author BRUCE
	 *
	 */
	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String mode = dao.findNumber(incomingNumber);
				if(mode.equals("1") || mode.equals("2")) {
					Uri uri = Uri.parse("content://call_log_calls");
					getContentResolver().registerContentObserver(uri, true, new MyContentObserver(incomingNumber, new Handler()));
					//android.os.ServiceManager 类被隐藏了。
					endCall();
				}
				break;
			}
		}
	}
	/**
	 * 创建自己的ContentObserver类，实现删除来电（黑名单中的号码）的记录
	 */
	private class MyContentObserver extends ContentObserver {
			String incomingNumber;
	        /**
	         * Creates a content observer.
	         *
	         * @param handler The handler to run {@link #onChange} on, or null if none.
	         * @param incomingNumber
	         */
		public MyContentObserver(String incomingNumber, Handler handler) {
			super(handler);
			getContentResolver().unregisterContentObserver(this);
			this.incomingNumber = incomingNumber;
		} 
		//数据改变时调用
		@Override
			public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			Log.i("CallLogObserver","呼叫记录数据库的内容变化了。");
				getContentResolver().unregisterContentObserver(this);
				deleteCallLog(incomingNumber);
				super.onChange(selfChange);
			}
	}
	
	
	//删掉号码
	private void deleteCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://call_log_calls");
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
	
	/**
	 * 挂断电话。
	 */
	private void endCall() {
		//在android studio中将alid文件放入aidl文件下
		try {
			//通过类加载器加载ServiceManager类
			Class<?> clazz =  getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);//通过反射拿到类中方法。
			IBinder binder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);//调用方法，invoke的receiver参数为static则为null。getService（String name）传入一个参数即TELEPHONY_SERVICE
			ITelephony.Stub.asInterface(binder).endCall();//aidl方式调用这个endcall方法
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	/**
	 * 来电广播
	 * @author BRUCE
	 *
	 */
	class InterceptReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("有短信来了，拦截短信服务开启中");
			 Object[] object = (Object[]) intent.getExtras().get("pdus");
				for (Object object2 : object) {
					SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object2);
					
					String number = smsMessage.getOriginatingAddress();
					String smsBody= smsMessage.getMessageBody();
					String mode = dao.findNumber(number);
					
					if(mode.equals("1")) {
						abortBroadcast();
					}else if(mode.equals("3")){
						abortBroadcast();
					}
					//智能拦截
					if(smsBody.contains("贷款")) {
						abortBroadcast();
					}
				}
		}
		
	}
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		super.onDestroy();
	}
}
