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
 * �߼�����ģ��
 * @author Ԭ����
 *
 */
public class AdvancedToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_tools);
	}
	
	/** �绰�����ز�ѯ��� **/
	public void addressQuery(View view) {
		startActivity(new Intent(this,AdressQueryActivity.class));
	}
	
	/* ���ű��� */
	public void smsBackup(View view) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			final ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڱ��ݶ���");
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
			ToastUtils.showToast(getApplicationContext(), "�������");
		}else {
			ToastUtils.showToast(this , "sdcard������!");
		}
	}
	
	/*���ú����ѯ���*/
	public void commonNumberQuery(View view) {
		startActivity(new Intent(this,CommonNumberActivity.class));
	}

}
