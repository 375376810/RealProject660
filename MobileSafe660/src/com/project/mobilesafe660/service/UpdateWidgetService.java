package com.project.mobilesafe660.service;

import java.util.Timer;
import java.util.TimerTask;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.activity.HomeActivity;
import com.project.mobilesafe660.engine.ProcessInfoProvider;
import com.project.mobilesafe660.receiver.MyWidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

/**
 * 定时更新widget服务
 * 
 * @author 袁星明
 */
public class UpdateWidgetService extends Service {

	private Timer mTimer;
	private AppWidgetManager mAWM;
	private InnerScreenReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化widget管理器
		mAWM = AppWidgetManager.getInstance(this);
		
		startTimer();
		
		//监听屏幕开启和关闭的广播
		mReceiver = new InnerScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
		
	}

	private void startTimer() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateWidget();
			}
		}, 0, 5000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopTimer();
		unregisterReceiver(mReceiver);
		mReceiver = null;
	}

	private void stopTimer() {
		mTimer.cancel();
		mTimer = null;
	}

	protected void updateWidget() {
		//初始化组件
		ComponentName provider = new ComponentName(this,MyWidget.class);
		//初始化远程view
		RemoteViews views = new RemoteViews(getPackageName(),R.layout.process_widget);
		//更新textview文字
		views.setTextViewText(R.id.tv_running_num, "正在运行软件:"+ProcessInfoProvider.getRunningProcessNum(this));
		views.setTextViewText(R.id.tv_avail_memory, "可用内存:"+ Formatter.formatFileSize(this, ProcessInfoProvider.getAvailableMemory(this)));
		//设置点击事件
		Intent intent = new Intent(this,HomeActivity.class);
		//PendingIntent:延时(不确定何时运行)的Intent,是对Intent的包装
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.ll_root, pendingIntent);
		//设置一键清理点击事件
		Intent clearIntent = new Intent();
		clearIntent.setAction("com.project.mobilesafe660.ONE_KEY_CLEAR");
		PendingIntent clearPendingIntent = PendingIntent.getBroadcast(this, 0, clearIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.btn_clear, clearPendingIntent);
		//更新widget
		mAWM.updateAppWidget(provider, views);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("服務如果已经启动,再启动时会走此方法");
		return super.onStartCommand(intent, flags, startId);
	}
	
	/** 屏幕开启和关闭广播接收者 **/
	class InnerScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				//屏幕开启
				startTimer();
			} else {
				//屏幕关闭
				stopTimer();
			}
		}
	}

}
