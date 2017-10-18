package com.project.mobilesafe660.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.AddressDao;

/**
 * ��������ʾ����
 * @author Ԭ����
 */
public class AddressService extends Service {

	private TelephonyManager mTelephonyManager;
	private MyPhoneStateListener mPhoneStateListener;
	private InnerReceiver myInnerReceiver;
	private WindowManager mywindoManager;
	private View myToasView;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//��������
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//�绰״̬������
		mPhoneStateListener = new MyPhoneStateListener();
		//��������״̬
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		myInnerReceiver = new InnerReceiver();
		IntentFilter intentFilter = new IntentFilter();
		//ȥ��
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(myInnerReceiver, intentFilter);
		
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//ȡ���������
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		mPhoneStateListener = null;
		//ȡ��ȥ�����
		unregisterReceiver(myInnerReceiver);
		myInnerReceiver=null;
	}
	
	/** �绰״̬������ **/
	class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				//����
				String address = AddressDao.getAddress(incomingNumber);
				//Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//ժ��
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				//����
				if (mywindoManager!=null&&myToasView!=null) {
					//�Ӵ����Ƴ������ص��� 
					mywindoManager.removeView(myToasView);
				}
				break;
			default:
				break;
			}
		}
	}
	
	/** ����ȥ��㲥 **/
	class InnerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			String address = AddressDao.getAddress(number);
			//Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
			showToast(address);
		}
	}
	
	private void showToast(String text) {
		mywindoManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		
		//��ʼ������
		//myToasView = new TextView(this);
		//myToasView.setText(text);
		//myToasView.setTextColor(Color.RED);
		//myToasView.setTextSize(22);
		myToasView = View.inflate(this, R.layout.custom_toast, null);
		TextView tvAddress = (TextView) myToasView.findViewById(R.id.tv_address);
		tvAddress.setText(text);
		
		//��ʼ�����ֲ���
		LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("toast");
		
		mywindoManager.addView(myToasView, params);
		
	}

}
