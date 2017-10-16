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
 * ������4
 * 
 * @author Ԭ����
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
		// ��ʼ�����Թ�����
		mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		// ��ʼ�����
		mDeviceAdmin = new ComponentName(this, SuperAdminReceiver.class);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// �жϸû����Ƿ��ѻ�ȡ��������ԱȨ��
				if (mDevicePolicyManager.isAdminActive(mDeviceAdmin)) {
					tvStateSuperAdmin.setText("�Ѽ���");
					ivStateSuperAdmin.setImageResource(R.drawable.activate_state_on);
				} else {
					tvStateSuperAdmin.setText("δ����");
					ivStateSuperAdmin.setImageResource(R.drawable.activate_state_off);
				}
			}
		};

		//ʵʱˢ�¹���ԱȨ��״̬
		refreshState();
		cbProtect = (CheckBox) findViewById(R.id.cb_protect);
		Boolean protect = PrefUtils.getBoolean("protect", false, this);
		if (protect) {
			cbProtect.setChecked(true);
			cbProtect.setText("���������Ѿ�����");
		} else {
			cbProtect.setChecked(false);
			cbProtect.setText("��û�п�����������");
		}
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("���������Ѿ�����");
					PrefUtils.putBoolean("protect", true,
							getApplicationContext());
				} else {
					cbProtect.setText("��û�п�����������");
					PrefUtils.putBoolean("protect", false,
							getApplicationContext());
				}
			}
		});
	}

	/** ��һҳ **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// ����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_previous_in,
				R.anim.anim_previous_out);
	}

	/** ��һҳ **/
	@Override
	public void showNext() {
		flag= false;
		PrefUtils.putBoolean("configed", true, this);
		String lockScreenPassword = etLockPassword.getText().toString().trim();
		if (TextUtils.isEmpty(lockScreenPassword)) {
			System.out.println("��������Ϊ��");
		}
		PrefUtils.putString("lockScreenPassword", lockScreenPassword, this);
		
		startActivity(new Intent(this, LostAndFindActivity.class));
		finish();
		// ����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	/** һ�����������Ա��� **/
	public void activeSuperAdmin(View view) {
		// ��ת��ϵͳ����ҳ�漤��
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"��������ԱȨ�޿���ʵ��Զ��ɾ������,Զ�������ȹ���");
		startActivity(intent);
	}

	/** ȡ�����������Ա��� **/
	public void uninstalSuperAdmin(View view) {
		// �Ƴ���������ԱȨ��
		mDevicePolicyManager.removeActiveAdmin(mDeviceAdmin);
		// ����ϵͳж��ҳ�����ж��
		//Intent intent = new Intent(Intent.ACTION_DELETE);
		//intent.addCategory(Intent.CATEGORY_DEFAULT);
		//intent.setData(Uri.parse("package:" + getPackageName()));
		//startActivity(intent);
	}
	
	/** ʵʱ������ԱȨ��״̬ **/
	public void refreshState() {
		new Thread(){
			public void run() {
				while (flag) {
					try {
						mHandler.sendEmptyMessage(0);
						System.out.println("����������������������������������������...");
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
}
