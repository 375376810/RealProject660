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
 * ��ʱ����widget����
 * 
 * @author Ԭ����
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
		//��ʼ��widget������
		mAWM = AppWidgetManager.getInstance(this);
		
		startTimer();
		
		//������Ļ�����͹رյĹ㲥
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
		//��ʼ�����
		ComponentName provider = new ComponentName(this,MyWidget.class);
		//��ʼ��Զ��view
		RemoteViews views = new RemoteViews(getPackageName(),R.layout.process_widget);
		//����textview����
		views.setTextViewText(R.id.tv_running_num, "�����������:"+ProcessInfoProvider.getRunningProcessNum(this));
		views.setTextViewText(R.id.tv_avail_memory, "�����ڴ�:"+ Formatter.formatFileSize(this, ProcessInfoProvider.getAvailableMemory(this)));
		//���õ���¼�
		Intent intent = new Intent(this,HomeActivity.class);
		//PendingIntent:��ʱ(��ȷ����ʱ����)��Intent,�Ƕ�Intent�İ�װ
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.ll_root, pendingIntent);
		//����һ���������¼�
		Intent clearIntent = new Intent();
		clearIntent.setAction("com.project.mobilesafe660.ONE_KEY_CLEAR");
		PendingIntent clearPendingIntent = PendingIntent.getBroadcast(this, 0, clearIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.btn_clear, clearPendingIntent);
		//����widget
		mAWM.updateAppWidget(provider, views);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("��������Ѿ�����,������ʱ���ߴ˷���");
		return super.onStartCommand(intent, flags, startId);
	}
	
	/** ��Ļ�����͹رչ㲥������ **/
	class InnerScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				//��Ļ����
				startTimer();
			} else {
				//��Ļ�ر�
				stopTimer();
			}
		}
	}

}
