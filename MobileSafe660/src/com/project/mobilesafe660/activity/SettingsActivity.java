package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.service.AddressService;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.ServiceStatusUtils;
import com.project.mobilesafe660.view.SettingItemView;

public class SettingsActivity extends Activity {

	private SettingItemView sivUpdate;
	//private SharedPreferences sp;
	private SettingItemView sivAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		//sp = getSharedPreferences("config",MODE_PRIVATE);
		
		initUpdate();
		initAddress();
	}

	//归属地显示设置
	private void initAddress() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		//判断服务是否运行,运行时才勾选,否则不勾选
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning("com.project.mobilesafe660.service.AddressService", this);
		sivAddress.setChecked(serviceRunning);
		sivAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent service = new Intent(getApplicationContext(),AddressService.class);
				
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					//关闭归属地显示服务
					stopService(service);
				}else {
					sivAddress.setChecked(true);
					//开启归属地显示服务
					startService(service);
				}
			}
		});
	}

	/** 初始化自动更新设置 **/
	private void initUpdate() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		//sivUpdate.setTitle("自动更新设置");
		
		boolean autoUpdate = PrefUtils.getBoolean("auto_update",true,this);
//		if (autoUpdate) {
//			//sivUpdate.setDesc("自动更新已开启");
//			sivUpdate.setChecked(true);
//		}else {
//			//sivUpdate.setDesc("自动更新已关闭");
//			sivUpdate.setChecked(false);
//		}
		sivUpdate.setChecked(autoUpdate);
		
		sivUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					//sivUpdate.setDesc("自动更新已关闭");
					//sp.edit().putBoolean("auto_update", false).commit();
					PrefUtils.putBoolean("auto_update", false, getApplicationContext());
				}else {
					sivUpdate.setChecked(true);
					//sivUpdate.setDesc("自动更新已开启");
					//sp.edit().putBoolean("auto_update", true).commit();
					PrefUtils.putBoolean("auto_update", true, getApplicationContext());
				}
			}
		});
	}

}
