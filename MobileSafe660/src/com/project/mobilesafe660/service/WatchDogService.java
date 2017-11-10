package com.project.mobilesafe660.service;

import java.util.ArrayList;
import java.util.List;

import com.project.mobilesafe660.activity.EnterPwdActivity;
import com.project.mobilesafe660.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * ���Ź�����
 * @author Ԭ����
 */
public class WatchDogService extends Service {

	private ActivityManager mAm;
	private boolean isRunning = true;
	private AppLockDao mDao;
	private MyReceiver mReceiver;
	private String mSkipPackage;
	private ArrayList<String> mLockList;
	private MyContentObserver mObserver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mAm = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		mDao = AppLockDao.getInstence(this);
		mLockList = mDao.findAll();
		
		new Thread(){
			public void run() {
				while (isRunning) {
					//��ȡ��ǰ��Ļչʾҳ��
					//��ȡ��ǰ���е�����ջ,����һ�����µ�����ջ
					List<RunningTaskInfo> runningTasks = mAm.getRunningTasks(1);
					//��ȡջ��activity���ڵİ���
					String packageName = runningTasks.get(0).topActivity.getPackageName();
					//if (mDao.find(packageName) && !packageName.equals(mSkipPackage)) {
					if(mLockList.contains(packageName) && !packageName.equals(mSkipPackage)){
						//��ת��������ҳ��
						Intent intent = new Intent(getApplicationContext(),EnterPwdActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						//���������ݸ���������ҳ��
						intent.putExtra("package", packageName);
						startActivity(intent);
					}
					//������Ϣ
					SystemClock.sleep(333);
					//System.out.println(packageName);
				}
			};
		}.start();
		
		//ע��㲥������(������������֤)
		mReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.project.mobilesafe660.SKIP_CHECK");
		registerReceiver(mReceiver, filter);
		
		mObserver = new MyContentObserver(null);
		//�����ݹ۲��߼��������ṩ�ߵ������Ƿ����仯
		getContentResolver().registerContentObserver((Uri.parse("content://com.project.mobilesafe660/change")),true, mObserver);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
		unregisterReceiver(mReceiver);
		mReceiver = null;
		getContentResolver().unregisterContentObserver(mObserver);
		mObserver = null;
	}
	
	/** �㲥������**/
	class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			mSkipPackage = intent.getStringExtra("package");
		}
	}
	
	/** ���ݹ۲��� **/
	class MyContentObserver extends ContentObserver {
		public MyContentObserver(Handler handler) {
			super(handler);
		}		
		//���ݿⷢ���仯ʱ��ص��˷���
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//������ȡ���ݿ�����
			mLockList = mDao.findAll();
		}
	}
	
}
