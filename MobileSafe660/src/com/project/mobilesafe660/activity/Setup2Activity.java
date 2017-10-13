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
 * 设置向导2
 * @author 袁星明
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
					//初始化电话管理器
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					//获取sim卡序列号
					String simSerialNumber = tm.getSimSerialNumber();
					//保存
					PrefUtils.putString("bind_sim", simSerialNumber, getApplicationContext());
				}
			}
		});
	}
	
	/** 跳转到上一页 **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		//两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_previous_in, R.anim.anim_previous_out);
	}
	
	/** 跳转到下一页 **/
	@Override
	public void showNext() {
		//判断是否绑定了sim卡,只有绑定sim卡才能进行下一步
		String bindSim = PrefUtils.getString("bind_sim", null, getApplicationContext());
		if (TextUtils.isEmpty(bindSim)) {
			ToastUtils.showToast(getApplicationContext(), "必须绑定sim卡");
			return;
		} 
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		//两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}
	
}
