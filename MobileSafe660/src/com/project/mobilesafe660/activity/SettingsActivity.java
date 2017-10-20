package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.service.AddressService;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.ServiceStatusUtils;
import com.project.mobilesafe660.view.SettingItemClickView;
import com.project.mobilesafe660.view.SettingItemView;

public class SettingsActivity extends Activity {

	private SettingItemView sivUpdate;
	// private SharedPreferences sp;
	private SettingItemView sivAddress;
	private SettingItemClickView sicStyle;
	private SettingItemClickView sicLocation;
	private String[] mItems = new String[] { "��͸��", "������", "��ʿ��", "������", "ƻ����" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// sp = getSharedPreferences("config",MODE_PRIVATE);

		initUpdate();
		initAddress();
		initAddressStyle();
		initAddressLocation();
	}

	// �����ص���λ�øı�
	private void initAddressLocation() {
		sicLocation = (SettingItemClickView) findViewById(R.id.sic_location);
		sicLocation.setTitle("��������ʾ��λ��");
		sicLocation.setDesc("���ù�������ʾ�����ʾλ��");
		sicLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//����λ���޸ĵ�ҳ��
				startActivity(new Intent(getApplicationContext(),DragViewActivity.class));
			}
		});
	}

	// ��ʼ����������ʽ����
	private void initAddressStyle() {
		sicStyle = (SettingItemClickView) findViewById(R.id.sic_style);
		sicStyle.setTitle("��������ʾ����");
		// ��ȡ�������ʽ
		int style = PrefUtils.getInt("address_style", 0, this);
		sicStyle.setDesc(mItems[style]);
		sicStyle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showChooseDialog();
			}
		});
	}

	// �����ط��ѡ�񵯴�
	protected void showChooseDialog() {
		// ��ȡ�����ļ��õ��������ʽ
		int style = PrefUtils.getInt("address_style", 0, this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��������ʾ����");
		builder.setIcon(R.drawable.ic_launcher);

		builder.setSingleChoiceItems(mItems, style,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// ���浱ǰ���
						PrefUtils.putInt("address_style", which,
								getApplicationContext());
						dialog.dismiss();
						// ��������
						sicStyle.setDesc(mItems[which]);
					}
				});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	// ��������ʾ����
	private void initAddress() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		// �жϷ����Ƿ�����,����ʱ�Ź�ѡ,���򲻹�ѡ
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(
				"com.project.mobilesafe660.service.AddressService", this);
		sivAddress.setChecked(serviceRunning);
		sivAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent service = new Intent(getApplicationContext(),
						AddressService.class);

				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					// �رչ�������ʾ����
					stopService(service);
				} else {
					sivAddress.setChecked(true);
					// ������������ʾ����
					startService(service);
				}
			}
		});
	}

	/** ��ʼ���Զ��������� **/
	private void initUpdate() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("�Զ���������");

		boolean autoUpdate = PrefUtils.getBoolean("auto_update", true, this);
		// if (autoUpdate) {
		// //sivUpdate.setDesc("�Զ������ѿ���");
		// sivUpdate.setChecked(true);
		// }else {
		// //sivUpdate.setDesc("�Զ������ѹر�");
		// sivUpdate.setChecked(false);
		// }
		sivUpdate.setChecked(autoUpdate);

		sivUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("�Զ������ѹر�");
					// sp.edit().putBoolean("auto_update", false).commit();
					PrefUtils.putBoolean("auto_update", false,
							getApplicationContext());
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("�Զ������ѿ���");
					// sp.edit().putBoolean("auto_update", true).commit();
					PrefUtils.putBoolean("auto_update", true,
							getApplicationContext());
				}
			}
		});
	}

}
