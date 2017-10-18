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
 * 归属地显示服务
 * @author 袁星明
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
		//监听来电
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//电话状态监听器
		mPhoneStateListener = new MyPhoneStateListener();
		//监听来电状态
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		myInnerReceiver = new InnerReceiver();
		IntentFilter intentFilter = new IntentFilter();
		//去电
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(myInnerReceiver, intentFilter);
		
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//取消来电监听
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		mPhoneStateListener = null;
		//取消去电监听
		unregisterReceiver(myInnerReceiver);
		myInnerReceiver=null;
	}
	
	/** 电话状态监听器 **/
	class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				//响铃
				String address = AddressDao.getAddress(incomingNumber);
				//Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//摘机
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				//空闲
				if (mywindoManager!=null&&myToasView!=null) {
					//从窗口移除归属地弹窗 
					mywindoManager.removeView(myToasView);
				}
				break;
			default:
				break;
			}
		}
	}
	
	/** 监听去电广播 **/
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
		
		//初始化布局
		//myToasView = new TextView(this);
		//myToasView.setText(text);
		//myToasView.setTextColor(Color.RED);
		//myToasView.setTextSize(22);
		myToasView = View.inflate(this, R.layout.custom_toast, null);
		TextView tvAddress = (TextView) myToasView.findViewById(R.id.tv_address);
		tvAddress.setText(text);
		
		//初始化布局参数
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
