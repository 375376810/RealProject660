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

/** ���������ط��� **/
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
		//���ض���
		mReceiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mReceiver, filter);
		
		//���ص绰
		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyListener();
		mTM.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
		
	}
	
	/* �������  */
	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				//�绰����
				int mode = mDao.findMode(incomingNumber);
				if (mode==1||mode==3) {
					//�Ҷϵ绰
					endCall();
					mObserver = new MyObserver(new Handler(),incomingNumber);
					//ע�����ݹ۲���
					getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, mObserver);
				}
				break;
			}
		}
	}
	
	/* ���ݹ۲���,���ڼ���ϵͳ��ʱ�������µ�ͨ����¼  */
	class MyObserver extends ContentObserver{
		private String number;
		public MyObserver(Handler handler, String incomingNumber) {
			super(handler);
			number = incomingNumber;
		}
		//���ݷ����仯ʱ,�ͻ��ߵ��˷�����
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			System.out.println("ͨ����¼�����仯��");
			deleteCallLog(number);
			//�����Ժ�һ��Ҫע��
			getContentResolver().unregisterContentObserver(mObserver);
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//ע�����ż���
		unregisterReceiver(mReceiver);
		mReceiver = null;
		//ȡ���������
		mTM.listen(mListener, PhoneStateListener.LISTEN_NONE);
		mListener = null;
	}
	
	/* ɾ��ͨ����¼ */
	public void deleteCallLog(String number) {
		getContentResolver().delete(Uri.parse("content://call_log/calls"), "number=?", new String[]{number});
	}

	/* 
	 * �Ҷϵ绰�߼� 
	 * 1.ͨ��debug�ҵ�ContextImpl
	 * 2.��getSystemService����
	 * 3.��ServiceManager
	 * 4.ITelephony.aidl
	 * ��ҪȨ��<uses-permission android:name="android.permission.CALL_PHONE" />
	 */
	public void endCall() {
		System.out.println("�Ҷϵ绰");
		//ͨ�����似���õ�ϵͳ���ط���
		try {
			Class clazz = Class.forName("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", String.class);
			//��̬��������Ҫ����,arg0Ϊnull
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
				//����140�ֽ�,��ֶ�������
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) object );
				String originatingAddress = sms.getOriginatingAddress();
				String messageBody = sms.getMessageBody();
				System.out.println("���ź���:" + originatingAddress +"��������:"+ messageBody);
				//�жϵ�ǰ�����Ƿ����ں�����,�����,������
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
