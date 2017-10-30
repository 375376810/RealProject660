package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.AddressDao;

public class AdressQueryActivity extends Activity {

	private EditText etNumber;
	private Button btnStart;
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adress_query);
		
		etNumber = (EditText) findViewById(R.id.et_numbers);
		btnStart = (Button) findViewById(R.id.btn_start);
		tvResult = (TextView) findViewById(R.id.tv_result);
		
		btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = etNumber.getText().toString().trim();
				if (!TextUtils.isEmpty(number)) {
					String address = AddressDao.getAddress(number);
					tvResult.setText(address);
				} else {
					//ToastUtils.showToast(getApplicationContext(), "输入内容不能为空");
					//实现抖动的动画效果
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
					etNumber.startAnimation(shake);
					//使手机震动
					vibratIt();
				}
			}
		});
		
		//添加一个文本框变化监听器
		etNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//文本发生变化
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//文本变化之前
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				//文本变化之后
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}
		});
		
	}
	
	//手机的振动器
	public void vibratIt() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		//震动一定时间
		//vibrator.vibrate(2000);
		//有节奏的震动.
		//arg0:震动模式,奇数位表示休息时间,偶数位表示震动时间.
		//arg1:重复模式,-1表示不重复,0表示从第一个位置开始重复,1表示从第二个位置开始重复
		vibrator.vibrate(new long[]{1000,2000,1500,1000}, -1);
		//震动停止
		vibrator.cancel();
	}

}
