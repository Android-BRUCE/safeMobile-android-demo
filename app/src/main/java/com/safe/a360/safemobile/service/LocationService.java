package com.safe.a360.safemobile.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * 获取地址坐标
 * @author BRUCE
 *
 */
public class LocationService extends Service {
	
	private SharedPreferences mPerf;
	private LocationManager location;
	private LocationListener listener;
	@Override
	public IBinder onBind(Intent intent) {
		return null;	
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mPerf = getSharedPreferences("config", MODE_PRIVATE);
		
		location = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);//是否允许付费。比如使用网络时。
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String locationstring =  location.getBestProvider(criteria, true);//获取最佳的位置提供者
		
		listener = new MyLocationListener();
		location.requestLocationUpdates(locationstring, 0, 0, listener);
	}
	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			System.out.println("get location!");
			
			// 将获取的经纬度保存在sp中
			mPerf.edit().putString("location","j:" + location.getLongitude() + "; w:"
									+ location.getLatitude()).commit();
			stopSelf();//停掉service			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		location.removeUpdates(listener);// 当activity销毁时,停止更新位置, 节省电量
	}
	
	

}
