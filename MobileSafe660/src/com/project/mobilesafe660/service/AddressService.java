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
 * 归属地显示服务
 * 
 * @author 袁星明
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
		// 监听来电
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// 电话状态监听器
		mPhoneStateListener = new MyPhoneStateListener();
		// 监听来电状态
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		myInnerReceiver = new InnerReceiver();
		IntentFilter intentFilter = new IntentFilter();
		// 去电
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(myInnerReceiver, intentFilter);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消来电监听
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_NONE);
		mPhoneStateListener = null;
		// 取消去电监听
		unregisterReceiver(myInnerReceiver);
		myInnerReceiver = null;
	}

	/** 电话状态监听器 **/
	class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// 响铃
				String address = AddressDao.getAddress(incomingNumber);
				// Toast.makeText(getApplicationContext(), address,
				// Toast.LENGTH_LONG).show();
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// 摘机
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				// 空闲
				if (mywindoManager != null && myToasView != null) {
					// 从窗口移除归属地弹窗
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
			// Toast.makeText(getApplicationContext(), address,
			// Toast.LENGTH_LONG).show();
			showToast(address);
		}
	}

	/*
	 * 为了保证可以触摸:
	 * 1.params.flags,删掉FLAG_NOT_TOUCHABLE
	 * 2.params.type更改为TYPE_PHONE;
	 * 3.加权限:<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
	 */
	private void showToast(String text) {
		mywindoManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		
		screenWidth = mywindoManager.getDefaultDisplay().getWidth();
		screenHeight = mywindoManager.getDefaultDisplay().getHeight();

		// 初始化布局
		// myToasView = new TextView(this);
		// myToasView.setText(text);
		// myToasView.setTextColor(Color.RED);
		// myToasView.setTextSize(22);
		myToasView = View.inflate(this, R.layout.custom_toast, null);
		TextView tvAddress = (TextView) myToasView
				.findViewById(R.id.tv_address);
		tvAddress.setText(text);

		// 初始化布局参数
		final LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				//| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		//修改窗口类型为TYPE_PHONE
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("toast");
		// 将重心设定到左上方的位置,这样的话,坐标体系就以左上方为准,方便设定布局位置
		params.gravity = Gravity.LEFT + Gravity.TOP;

		int style = PrefUtils.getInt("address_style", 0, this);
		// 背景图片id数组
		int[] bgIds = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		// 更新背景图片
		tvAddress.setBackgroundResource(bgIds[style]);

		// int x = params.x;//相对于重心的x偏移
		// 修改布局位置
		int lastX = PrefUtils.getInt("lastX", 0, this);
		int lastY = PrefUtils.getInt("lastY", 0, this);
		params.x = lastX;
		params.y = lastY;
		myToasView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					// 移动,获取移动后的坐标点
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					// 计算偏移量
					int dx = endX - startX;
					int dy = endY - startY;
					// 根据偏移量更新位置
					params.x = params.x + dx;
					params.y = params.y + dy;
					// 防止控件超出屏幕边界
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
					// 根据当前位置,显示文本框提示位置
					// 修改布局位置
					mywindoManager.updateViewLayout(myToasView, params);
					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// 抬起,保存当前位置
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
