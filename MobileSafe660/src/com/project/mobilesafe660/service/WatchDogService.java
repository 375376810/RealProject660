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
 * 看门狗服务
 * @author 袁星明
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
					//获取当前屏幕展示页面
					//获取当前运行的任务栈,返回一条最新的任务栈
					List<RunningTaskInfo> runningTasks = mAm.getRunningTasks(1);
					//获取栈顶activity所在的包名
					String packageName = runningTasks.get(0).topActivity.getPackageName();
					//if (mDao.find(packageName) && !packageName.equals(mSkipPackage)) {
					if(mLockList.contains(packageName) && !packageName.equals(mSkipPackage)){
						//跳转输入密码页面
						Intent intent = new Intent(getApplicationContext(),EnterPwdActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						//将包名传递给输入密码页面
						intent.putExtra("package", packageName);
						startActivity(intent);
					}
					//短暂休息
					SystemClock.sleep(333);
					//System.out.println(packageName);
				}
			};
		}.start();
		
		//注册广播接收者(跳过程序锁验证)
		mReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.project.mobilesafe660.SKIP_CHECK");
		registerReceiver(mReceiver, filter);
		
		mObserver = new MyContentObserver(null);
		//用内容观察者监听内容提供者的数据是否发生变化
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
	
	/** 广播接收者**/
	class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			mSkipPackage = intent.getStringExtra("package");
		}
	}
	
	/** 内容观察者 **/
	class MyContentObserver extends ContentObserver {
		public MyContentObserver(Handler handler) {
			super(handler);
		}		
		//数据库发生变化时会回调此方法
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//重新拉取数据库数据
			mLockList = mDao.findAll();
		}
	}
	
}
