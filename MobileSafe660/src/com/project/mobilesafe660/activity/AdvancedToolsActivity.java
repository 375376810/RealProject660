package com.project.mobilesafe660.activity;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.SmsUtils;
import com.project.mobilesafe660.utils.ToastUtils;

/**
 * 高级工具模块
 * @author 袁星明
 *
 */
public class AdvancedToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_tools);
	}
	
	/** 电话归属地查询点击 **/
	public void addressQuery(View view) {
		startActivity(new Intent(this,AdressQueryActivity.class));
	}
	
	/* 短信备份 */
	public void smsBackup(View view) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File xmlFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/sms660.xml");
			SmsUtils.backup(this, xmlFile);
		}else {
			ToastUtils.showToast(this , "sdcard不存在!");
		}
	}

}
