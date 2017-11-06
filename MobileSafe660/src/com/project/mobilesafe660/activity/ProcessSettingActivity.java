package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.service.OutoKillService;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.ServiceStatusUtils;

public class ProcessSettingActivity extends Activity {

	private CheckBox cbShowSystem;
	private CheckBox cbOutoKill;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_setting);
		cbShowSystem = (CheckBox) findViewById(R.id.cb_show_system);
		cbOutoKill = (CheckBox) findViewById(R.id.cb_outo_kill);
		
		Boolean showSystemProcess = PrefUtils.getBoolean("show_system_process", true, this);
		if (showSystemProcess) {
			cbShowSystem.setChecked(true);
			cbShowSystem.setText("��ʾϵͳ����");			
		} else {
			cbShowSystem.setChecked(false);
			cbShowSystem.setText("����ʾϵͳ����");						
		}
		cbShowSystem.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					cbShowSystem.setText("��ʾϵͳ����");
					PrefUtils.putBoolean("show_system_process", true, getApplicationContext());
				} else {
					cbShowSystem.setText("����ʾϵͳ����");					
					PrefUtils.putBoolean("show_system_process", false, getApplicationContext());
				}
				
			}
		});
		
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning("com.project.mobilesafe660.service.OutoKillService", this);
		if (serviceRunning) {
			cbOutoKill.setChecked(true);
			cbOutoKill.setText("���������ѿ���");
		} else {
			cbOutoKill.setChecked(false);
			cbOutoKill.setText("���������ѹر�");			
		}
		
		cbOutoKill.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Intent service = new Intent(getApplicationContext(),OutoKillService.class);
				if (isChecked) {
					cbOutoKill.setText("���������ѿ���");
					startService(service);
				} else {
					cbOutoKill.setText("���������ѹر�");					
					stopService(service);
				}
			}
		});
		
	}

}
