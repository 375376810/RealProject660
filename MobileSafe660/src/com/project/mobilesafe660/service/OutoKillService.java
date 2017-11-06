package com.project.mobilesafe660.service;

import com.project.mobilesafe660.engine.ProcessInfoProvider;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * 锁屏清理服务
 * @author 袁星明
 */
public class OutoKillService extends Service {

	private InnerScreenOffReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//注册广播监听屏幕关闭事件
		mReceiver = new InnerScreenOffReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//注销广播
		unregisterReceiver(mReceiver);
		mReceiver = null;
	}
	
	/** 锁屏广播接收者 **/
	class InnerScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ProcessInfoProvider.killAll(context);
		}
	}

	
	
}
