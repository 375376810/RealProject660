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
 * ������3
 * @author Ԭ����
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
	
	/** ��һҳ **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		//����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_previous_in, R.anim.anim_previous_out);
	}

	/** ��һҳ **/
	@Override
	public void showNext() {
		//���氲ȫ�����������ļ�,���Ϊ������ʾ�û�����
		String phone = etPhone.getText().toString().trim();
		if (!TextUtils.isEmpty(phone)) {
			PrefUtils.putString("safe_phone", phone, this);
			startActivity(new Intent(this, Setup4Activity.class));
			finish();
			//����activity֮���л�����,Ӧ�÷���finish()��������
			overridePendingTransition(R.anim.anim_in, R.anim.anim_out);			
		} else {
			ToastUtils.showToast(this, "��ȫ���벻��Ϊ��!");
		}
	}
	
	/** ѡ����ϵ�˵�� **/
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
