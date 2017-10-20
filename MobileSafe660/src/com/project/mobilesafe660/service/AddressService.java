package com.project.mobilesafe660.service;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.AddressDao;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * ��������ʾ����
 * 
 * @author Ԭ����
 */
public class AddressService extends Service {

	private TelephonyManager mTelephonyManager;
	private MyPhoneStateListener mPhoneStateListener;
	private InnerReceiver myInnerReceiver;
	private WindowManager mywindoManager;
	private View myToasView;

	private int startY;
	private int startX;
	private int screenWidth;
	private int screenHeight;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// ��������
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// �绰״̬������
		mPhoneStateListener = new MyPhoneStateListener();
		// ��������״̬
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		myInnerReceiver = new InnerReceiver();
		IntentFilter intentFilter = new IntentFilter();
		// ȥ��
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(myInnerReceiver, intentFilter);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ȡ���������
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_NONE);
		mPhoneStateListener = null;
		// ȡ��ȥ�����
		unregisterReceiver(myInnerReceiver);
		myInnerReceiver = null;
	}

	/** �绰״̬������ **/
	class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// ����
				String address = AddressDao.getAddress(incomingNumber);
				// Toast.makeText(getApplicationContext(), address,
				// Toast.LENGTH_LONG).show();
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// ժ��
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				// ����
				if (mywindoManager != null && myToasView != null) {
					// �Ӵ����Ƴ������ص���
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
			// Toast.makeText(getApplicationContext(), address,
			// Toast.LENGTH_LONG).show();
			showToast(address);
		}
	}

	/*
	 * Ϊ�˱�֤���Դ���:
	 * 1.params.flags,ɾ��FLAG_NOT_TOUCHABLE
	 * 2.params.type����ΪTYPE_PHONE;
	 * 3.��Ȩ��:<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
	 */
	private void showToast(String text) {
		mywindoManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		
		screenWidth = mywindoManager.getDefaultDisplay().getWidth();
		screenHeight = mywindoManager.getDefaultDisplay().getHeight();

		// ��ʼ������
		// myToasView = new TextView(this);
		// myToasView.setText(text);
		// myToasView.setTextColor(Color.RED);
		// myToasView.setTextSize(22);
		myToasView = View.inflate(this, R.layout.custom_toast, null);
		TextView tvAddress = (TextView) myToasView
				.findViewById(R.id.tv_address);
		tvAddress.setText(text);

		// ��ʼ�����ֲ���
		final LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				//| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		//�޸Ĵ�������ΪTYPE_PHONE
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("toast");
		// �������趨�����Ϸ���λ��,�����Ļ�,������ϵ�������Ϸ�Ϊ׼,�����趨����λ��
		params.gravity = Gravity.LEFT + Gravity.TOP;

		int style = PrefUtils.getInt("address_style", 0, this);
		// ����ͼƬid����
		int[] bgIds = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		// ���±���ͼƬ
		tvAddress.setBackgroundResource(bgIds[style]);

		// int x = params.x;//��������ĵ�xƫ��
		// �޸Ĳ���λ��
		int lastX = PrefUtils.getInt("lastX", 0, this);
		int lastY = PrefUtils.getInt("lastY", 0, this);
		params.x = lastX;
		params.y = lastY;
		myToasView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// ��ʼ���������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					// �ƶ�,��ȡ�ƶ���������
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					// ����ƫ����
					int dx = endX - startX;
					int dy = endY - startY;
					// ����ƫ��������λ��
					params.x = params.x + dx;
					params.y = params.y + dy;
					// ��ֹ�ؼ�������Ļ�߽�
					if (params.x<0) {
						params.x = 0;
					}
					if (params.x>screenWidth-myToasView.getWidth()) {
						params.x=screenWidth-myToasView.getWidth();
					}
					if (params.y<0) {
						params.y = 0;
					}
					if (params.y>screenHeight-myToasView.getHeight()-25) {
						params.y=screenHeight-myToasView.getHeight()-25;
					}
					// ���ݵ�ǰλ��,��ʾ�ı�����ʾλ��
					// �޸Ĳ���λ��
					mywindoManager.updateViewLayout(myToasView, params);
					// ���³�ʼ���������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// ̧��,���浱ǰλ��
					//PrefUtils.putInt("lastX", ivDrag.getLeft(),getApplicationContext());
					//PrefUtils.putInt("lastY", ivDrag.getRight(),getApplicationContext());
					break;
				default:
					break;
				}
				return true;

			}
		});

		mywindoManager.addView(myToasView, params);

	}

}
