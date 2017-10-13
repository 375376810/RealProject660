package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.ToastUtils;
import com.project.mobilesafe660.view.SettingItemView;

/**
 * ������2
 * @author Ԭ����
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivBind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		sivBind = (SettingItemView) findViewById(R.id.siv_bind);
		String bindSim = PrefUtils.getString("bind_sim", null, getApplicationContext());
		if (TextUtils.isEmpty(bindSim)) {
			sivBind.setChecked(false);
		} else {
			sivBind.setChecked(true);
		}
		
		sivBind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (sivBind.isChecked()) {
					sivBind.setChecked(false);
					PrefUtils.remove("bind_sim", getApplicationContext());
				} else {
					sivBind.setChecked(true);
					//��ʼ���绰������
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					//��ȡsim�����к�
					String simSerialNumber = tm.getSimSerialNumber();
					//����
					PrefUtils.putString("bind_sim", simSerialNumber, getApplicationContext());
				}
			}
		});
	}
	
	/** ��ת����һҳ **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		//����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_previous_in, R.anim.anim_previous_out);
	}
	
	/** ��ת����һҳ **/
	@Override
	public void showNext() {
		//�ж��Ƿ����sim��,ֻ�а�sim�����ܽ�����һ��
		String bindSim = PrefUtils.getString("bind_sim", null, getApplicationContext());
		if (TextUtils.isEmpty(bindSim)) {
			ToastUtils.showToast(getApplicationContext(), "�����sim��");
			return;
		} 
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		//����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}
	
}
