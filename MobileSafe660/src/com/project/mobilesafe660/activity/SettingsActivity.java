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
	private String[] mItems = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

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

	// 归属地弹窗位置改变
	private void initAddressLocation() {
		sicLocation = (SettingItemClickView) findViewById(R.id.sic_location);
		sicLocation.setTitle("归属地提示框位置");
		sicLocation.setDesc("设置归属地提示框的显示位置");
		sicLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳到位置修改的页面
				startActivity(new Intent(getApplicationContext(),DragViewActivity.class));
			}
		});
	}

	// 初始化归属地样式设置
	private void initAddressStyle() {
		sicStyle = (SettingItemClickView) findViewById(R.id.sic_style);
		sicStyle.setTitle("归属地提示框风格");
		// 获取保存的样式
		int style = PrefUtils.getInt("address_style", 0, this);
		sicStyle.setDesc(mItems[style]);
		sicStyle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showChooseDialog();
			}
		});
	}

	// 归属地风格选择弹窗
	protected void showChooseDialog() {
		// 获取本地文件得到保存的样式
		int style = PrefUtils.getInt("address_style", 0, this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("归属地提示框风格");
		builder.setIcon(R.drawable.ic_launcher);

		builder.setSingleChoiceItems(mItems, style,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 保存当前风格
						PrefUtils.putInt("address_style", which,
								getApplicationContext());
						dialog.dismiss();
						// 更新描述
						sicStyle.setDesc(mItems[which]);
					}
				});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	// 归属地显示设置
	private void initAddress() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		// 判断服务是否运行,运行时才勾选,否则不勾选
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
					// 关闭归属地显示服务
					stopService(service);
				} else {
					sivAddress.setChecked(true);
					// 开启归属地显示服务
					startService(service);
				}
			}
		});
	}

	/** 初始化自动更新设置 **/
	private void initUpdate() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");

		boolean autoUpdate = PrefUtils.getBoolean("auto_update", true, this);
		// if (autoUpdate) {
		// //sivUpdate.setDesc("自动更新已开启");
		// sivUpdate.setChecked(true);
		// }else {
		// //sivUpdate.setDesc("自动更新已关闭");
		// sivUpdate.setChecked(false);
		// }
		sivUpdate.setChecked(autoUpdate);

		sivUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("自动更新已关闭");
					// sp.edit().putBoolean("auto_update", false).commit();
					PrefUtils.putBoolean("auto_update", false,
							getApplicationContext());
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("自动更新已开启");
					// sp.edit().putBoolean("auto_update", true).commit();
					PrefUtils.putBoolean("auto_update", true,
							getApplicationContext());
				}
			}
		});
	}

}
