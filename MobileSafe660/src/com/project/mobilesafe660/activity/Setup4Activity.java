package com.project.mobilesafe660.activity;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.receiver.SuperAdminReceiver;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * 设置向导4
 * 
 * @author 袁星明
 */
@SuppressLint("HandlerLeak") public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cbProtect;
	private ComponentName mDeviceAdmin;
	private DevicePolicyManager mDevicePolicyManager;
	private Handler mHandler;
	protected boolean flag;
	private EditText etLockPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		flag = true;
		final TextView tvStateSuperAdmin = (TextView) findViewById(R.id.tv_state_superAdmin);
		final ImageView ivStateSuperAdmin = (ImageView) findViewById(R.id.iv_state_superAdmin);
		etLockPassword = (EditText) findViewById(R.id.et_lockPassword);
		// 初始化策略管理器
		mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		// 初始化组件
		mDeviceAdmin = new ComponentName(this, SuperAdminReceiver.class);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 判断该机器是否已获取超级管理员权限
				if (mDevicePolicyManager.isAdminActive(mDeviceAdmin)) {
					tvStateSuperAdmin.setText("已激活");
					ivStateSuperAdmin.setImageResource(R.drawable.activate_state_on);
				} else {
					tvStateSuperAdmin.setText("未激活");
					ivStateSuperAdmin.setImageResource(R.drawable.activate_state_off);
				}
			}
		};

		//实时刷新管理员权限状态
		refreshState();
		cbProtect = (CheckBox) findViewById(R.id.cb_protect);
		Boolean protect = PrefUtils.getBoolean("protect", false, this);
		if (protect) {
			cbProtect.setChecked(true);
			cbProtect.setText("防盗保护已经开启");
		} else {
			cbProtect.setChecked(false);
			cbProtect.setText("您没有开启防盗保护");
		}
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("防盗保护已经开启");
					PrefUtils.putBoolean("protect", true,
							getApplicationContext());
				} else {
					cbProtect.setText("您没有开启防盗保护");
					PrefUtils.putBoolean("protect", false,
							getApplicationContext());
				}
			}
		});
	}

	/** 上一页 **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_previous_in,
				R.anim.anim_previous_out);
	}

	/** 下一页 **/
	@Override
	public void showNext() {
		flag= false;
		PrefUtils.putBoolean("configed", true, this);
		String lockScreenPassword = etLockPassword.getText().toString().trim();
		if (TextUtils.isEmpty(lockScreenPassword)) {
			System.out.println("锁屏密码为空");
		}
		PrefUtils.putString("lockScreenPassword", lockScreenPassword, this);
		
		startActivity(new Intent(this, LostAndFindActivity.class));
		finish();
		// 两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	/** 一键激活超级管理员点击 **/
	public void activeSuperAdmin(View view) {
		// 跳转到系统设置页面激活
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"超级管理员权限可以实现远程删除数据,远程锁屏等功能");
		startActivity(intent);
	}

	/** 取消激活超级管理员点击 **/
	public void uninstalSuperAdmin(View view) {
		// 移除超级管理员权限
		mDevicePolicyManager.removeActiveAdmin(mDeviceAdmin);
		// 跳到系统卸载页面进行卸载
		//Intent intent = new Intent(Intent.ACTION_DELETE);
		//intent.addCategory(Intent.CATEGORY_DEFAULT);
		//intent.setData(Uri.parse("package:" + getPackageName()));
		//startActivity(intent);
	}
	
	/** 实时监测管理员权限状态 **/
	public void refreshState() {
		new Thread(){
			public void run() {
				while (flag) {
					try {
						mHandler.sendEmptyMessage(0);
						System.out.println("啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦...");
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
}
