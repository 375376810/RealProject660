package com.project.mobilesafe660.service;

import com.project.mobilesafe660.utils.PrefUtils;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {

	private LocationManager mLocationManager;
	private MyLocationListener myLocationListener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		//初始化标准
		Criteria criteria = new Criteria();
		//设置精度标准
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//允许花费流量
		criteria.setCostAllowed(true);
		//获取最合适的位置提供者.arg0:标准.arg1:是否可用
		String bestProvider = mLocationManager.getBestProvider(criteria, true);
		myLocationListener = new MyLocationListener();
		mLocationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
		
	}
	
	/** 位置监听器 **/
	class MyLocationListener implements LocationListener {
		//位置发生变化时调用
		@Override
		public void onLocationChanged(Location location) {
			String j = "j"+location.getLongitude();//经度
			String w = "w"+location.getLatitude();//纬度
			//String altitude = "海拔" +location.getAltitude();
			String accuracy = "accuracy" + location.getAccuracy();
			String result = j+"\n" +w+"\n"+accuracy;
			//发送经纬度给安全号码
			String phone = PrefUtils.getString("safe_phone", "", getApplicationContext());
			SmsManager smsManager = SmsManager.getDefault();
			System.out.println(result);
			smsManager.sendTextMessage(phone, null, result, null, null);
			//服务自杀
			stopSelf();
		}

		//状态发生改变时调用
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		//用户打开GPS时调用
		@Override
		public void onProviderEnabled(String provider) {
		}

		//用户关闭GPS时调用
		@Override
		public void onProviderDisabled(String provider) {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//停止位置监听
		mLocationManager.removeUpdates(myLocationListener);
		myLocationListener= null;
	}

}
