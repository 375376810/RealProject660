package com.project.mobilesafe660.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.SmsUtils;
import com.project.mobilesafe660.utils.SmsUtils.SmsBackupCallback;
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
			final ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在备份短信");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.show();
			new Thread(){
				public void run() {
					File xmlFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/sms660.xml");
					//SmsUtils.backup(getApplicationContext(), xmlFile,progressDialog);
					SmsUtils.backup(getApplicationContext(), xmlFile, new SmsBackupCallback() {
						@Override
						public void preSmsBackup(int count) {
							progressDialog.setMax(count);
						}
						@Override
						public void onSmsBackup(int progress) {
							progressDialog.setProgress(progress);
						}
					});
					progressDialog.dismiss();		
				};
			}.start();
			ToastUtils.showToast(getApplicationContext(), "备份完成");
		}else {
			ToastUtils.showToast(this , "sdcard不存在!");
		}
	}
	
	/*常用号码查询点击*/
	public void commonNumberQuery(View view) {
		startActivity(new Intent(this,CommonNumberActivity.class));
	}

}
