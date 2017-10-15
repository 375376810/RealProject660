package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.AddressDao;
import com.project.mobilesafe660.utils.ToastUtils;

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
					ToastUtils.showToast(getApplicationContext(), "输入内容不能为空");
				}
			}
		});
	}

}
