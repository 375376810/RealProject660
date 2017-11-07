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
 * 锁屏清理服务
 * @author 袁星明
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
		//注册广播监听屏幕关闭事件
		mReceiver = new InnerScreenOffReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
		//定时清理,用到了timer
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("定时清理功能,呵呵...");
			}
		},0, 5000);//每隔5秒执行一次
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//注销广播
		unregisterReceiver(mReceiver);
		mReceiver = null;
		//取消定时器
		mTimer.cancel();
		mTimer = null;
	}
	
	/** 锁屏广播接收者 **/
	class InnerScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ProcessInfoProvider.killAll(context);
		}
	}

	
	
}
