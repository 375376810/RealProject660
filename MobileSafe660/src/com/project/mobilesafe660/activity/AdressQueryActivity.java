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
					//ToastUtils.showToast(getApplicationContext(), "�������ݲ���Ϊ��");
					//ʵ�ֶ����Ķ���Ч��
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
					etNumber.startAnimation(shake);
					//ʹ�ֻ���
					vibratIt();
				}
			}
		});
		
		//���һ���ı���仯������
		etNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//�ı������仯
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//�ı��仯֮ǰ
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				//�ı��仯֮��
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}
		});
		
	}
	
	//�ֻ�������
	public void vibratIt() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		//��һ��ʱ��
		//vibrator.vibrate(2000);
		//�н������.
		//arg0:��ģʽ,����λ��ʾ��Ϣʱ��,ż��λ��ʾ��ʱ��.
		//arg1:�ظ�ģʽ,-1��ʾ���ظ�,0��ʾ�ӵ�һ��λ�ÿ�ʼ�ظ�,1��ʾ�ӵڶ���λ�ÿ�ʼ�ظ�
		vibrator.vibrate(new long[]{1000,2000,1500,1000}, -1);
		//��ֹͣ
		vibrator.cancel();
	}

}
