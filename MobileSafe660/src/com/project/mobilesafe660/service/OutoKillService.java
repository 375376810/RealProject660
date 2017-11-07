package com.project.mobilesafe660.service;

import java.util.Timer;
import java.util.TimerTask;

import com.project.mobilesafe660.engine.ProcessInfoProvider;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * �����������
 * @author Ԭ����
 */
public class OutoKillService extends Service {

	private InnerScreenOffReceiver mReceiver;
	private Timer mTimer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//ע��㲥������Ļ�ر��¼�
		mReceiver = new InnerScreenOffReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
		//��ʱ����,�õ���timer
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("��ʱ������,�Ǻ�...");
			}
		},0, 5000);//ÿ��5��ִ��һ��
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//ע���㲥
		unregisterReceiver(mReceiver);
		mReceiver = null;
		//ȡ����ʱ��
		mTimer.cancel();
		mTimer = null;
	}
	
	/** �����㲥������ **/
	class InnerScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ProcessInfoProvider.killAll(context);
		}
	}

	
	
}
