package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.view.SettingItemView;

public class SettingsActivity extends Activity {

	private SettingItemView sivUpdate;
	//private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		//sp = getSharedPreferences("config",MODE_PRIVATE);
		
		initUpdate();
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
