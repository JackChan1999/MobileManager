package com.google.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

	private SharedPreferences mPref;
	private LocationManager lm;
	
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);//是否允许收费，网络费用
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置精度
		
		String bestProvider = lm.getBestProvider(criteria, true);
		listener = new MyLocationListener();
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		lm.removeUpdates(listener);
	}

	class MyLocationListener implements LocationListener {

		@Override
		// 位置改变
		public void onLocationChanged(Location location) {
			mPref.edit()
					.putString(
							"location",
							"j:" + location.getLongitude() + "w:"
									+ location.getLatitude()).commit();
			stopSelf();//停止服务
		}

		@Override
		// 位置提供者发生变化
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		// 用户打开gps
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		// 用户关闭gps
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}
}
