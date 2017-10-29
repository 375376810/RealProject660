package com.project.mobilesafe660.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.project.mobilesafe660.db.dao.BlackNumberDao;

/** 黑名单拦截服务 **/
public class BlackNumberService extends Service {

	private InnerSmsReceiver mReceiver;
	private BlackNumberDao mDao;
	private TelephonyManager mTM;
	private MyListener mListener;
	private MyObserver mObserver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDao = BlackNumberDao.getInstence(this);
		//拦截短信
		mReceiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mReceiver, filter);
		
		//拦截电话
		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyListener();
		mTM.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
		
	}
	
	/* 来电监听  */
	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				//电话铃响
				int mode = mDao.findMode(incomingNumber);
				if (mode==1||mode==3) {
					//挂断电话
					endCall();
					mObserver = new MyObserver(new Handler(),incomingNumber);
					//注册内容观察者
					getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, mObserver);
				}
				break;
			}
		}
	}
	
	/* 内容观察者,用于监听系统何时增加了新的通话记录  */
	class MyObserver extends ContentObserver{
		private String number;
		public MyObserver(Handler handler, String incomingNumber) {
			super(handler);
			number = incomingNumber;
		}
		//数据发生变化时,就会走到此方法中
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			System.out.println("通话记录发生变化了");
			deleteCallLog(number);
			//用完以后一定要注销
			getContentResolver().unregisterContentObserver(mObserver);
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//注销短信监听
		unregisterReceiver(mReceiver);
		mReceiver = null;
		//取消来电监听
		mTM.listen(mListener, PhoneStateListener.LISTEN_NONE);
		mListener = null;
	}
	
	/* 删除通话记录 */
	public void deleteCallLog(String number) {
		getContentResolver().delete(Uri.parse("content://call_log/calls"), "number=?", new String[]{number});
	}

	/* 
	 * 挂断电话逻辑 
	 * 1.通过debug找到ContextImpl
	 * 2.找getSystemService方法
	 * 3.找ServiceManager
	 * 4.ITelephony.aidl
	 * 需要权限<uses-permission android:name="android.permission.CALL_PHONE" />
	 */
	public void endCall() {
		System.out.println("挂断电话");
		//通过反射技术得到系统隐藏服务
		try {
			Class clazz = Class.forName("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", String.class);
			//静态方法不需要对象,arg0为null
			IBinder binder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony service = ITelephony.Stub.asInterface(binder);
			service.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	class InnerSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				//超过140字节,会分多条发送
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) object );
				String originatingAddress = sms.getOriginatingAddress();
				String messageBody = sms.getMessageBody();
				System.out.println("短信号码:" + originatingAddress +"短信内容:"+ messageBody);
				//判断当前号码是否属于黑名单,如果是,就拦截
				if (mDao.find(originatingAddress)) {
					int mode = mDao.findMode(originatingAddress);
					if(mode>1) {
						abortBroadcast();
					}
				}
			}
		}
		
	}
	
}
