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
		//��ʼ����׼
		Criteria criteria = new Criteria();
		//���þ��ȱ�׼
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//����������
		criteria.setCostAllowed(true);
		//��ȡ����ʵ�λ���ṩ��.arg0:��׼.arg1:�Ƿ����
		String bestProvider = mLocationManager.getBestProvider(criteria, true);
		myLocationListener = new MyLocationListener();
		mLocationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
		
	}
	
	/** λ�ü����� **/
	class MyLocationListener implements LocationListener {
		//λ�÷����仯ʱ����
		@Override
		public void onLocationChanged(Location location) {
			String j = "j"+location.getLongitude();//����
			String w = "w"+location.getLatitude();//γ��
			//String altitude = "����" +location.getAltitude();
			String accuracy = "accuracy" + location.getAccuracy();
			String result = j+"\n" +w+"\n"+accuracy;
			//���;�γ�ȸ���ȫ����
			String phone = PrefUtils.getString("safe_phone", "", getApplicationContext());
			SmsManager smsManager = SmsManager.getDefault();
			System.out.println(result);
			smsManager.sendTextMessage(phone, null, result, null, null);
			//������ɱ
			stopSelf();
		}

		//״̬�����ı�ʱ����
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		//�û���GPSʱ����
		@Override
		public void onProviderEnabled(String provider) {
		}

		//�û��ر�GPSʱ����
		@Override
		public void onProviderDisabled(String provider) {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//ֹͣλ�ü���
		mLocationManager.removeUpdates(myLocationListener);
		myLocationListener= null;
	}

}
