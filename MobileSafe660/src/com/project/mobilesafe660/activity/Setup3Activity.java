package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.ToastUtils;

/**
 * 设置向导3
 * @author 袁星明
 */
public class Setup3Activity extends BaseSetupActivity {

	private EditText etPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		etPhone = (EditText) findViewById(R.id.et_phone);
		String phone = PrefUtils.getString("safe_phone", "", this);
		etPhone.setText(phone);
	}
	
	/** 上一页 **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		//两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_previous_in, R.anim.anim_previous_out);
	}

	/** 下一页 **/
	@Override
	public void showNext() {
		//保存安全号码至本地文件,如果为空则提示用户输入
		String phone = etPhone.getText().toString().trim();
		if (!TextUtils.isEmpty(phone)) {
			PrefUtils.putString("safe_phone", phone, this);
			startActivity(new Intent(this, Setup4Activity.class));
			finish();
			//两个activity之间切换动画,应该放在finish()后面运行
			overridePendingTransition(R.anim.anim_in, R.anim.anim_out);			
		} else {
			ToastUtils.showToast(this, "安全号码不能为空!");
		}
	}
	
	/** 选择联系人点击 **/
	public void selectContact(View view) {
		startActivityForResult(new Intent(this, ContactActivity.class),0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll("-", "").replaceAll(" ", "");
			etPhone.setText(phone);			
		}
	}
	
}
