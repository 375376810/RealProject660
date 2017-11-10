package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.ToastUtils;

/**
 * 程序锁输入密码页面
 * @author 袁星明
 */
public class EnterPwdActivity extends Activity {

	private TextView tvName;
	private ImageView ivIcon;
	private EditText et_pwd;
	private Button btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pwd);
		
		tvName = (TextView) findViewById(R.id.tv_name);
		ivIcon = (ImageView) findViewById(R.id.iv_icon);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		btnOk = (Button) findViewById(R.id.btn_ok);
		
		Intent intent = getIntent();
		//获取当前加锁应用的包名
		final String packageName = intent.getStringExtra("package");
		//根据包名获取信息
		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
			String name = applicationInfo.loadLabel(pm).toString();
			Drawable icon = applicationInfo.loadIcon(pm);
			tvName.setText(name);
			ivIcon.setImageDrawable(icon);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pwd = et_pwd.getText().toString().trim();
				if (!TextUtils.isEmpty(pwd)) {
					if (pwd.equals("123")) {
						//通知看门狗,跳过当前包名验证
						Intent broadCast = new Intent();
						broadCast.setAction("com.project.mobilesafe660.SKIP_CHECK");
						//传递包名
						broadCast.putExtra("package", packageName);
						sendBroadcast(broadCast);
						finish();
					} else {
						ToastUtils.showToast(getApplicationContext(), "密码错误,123");						
					}
				} else {
					ToastUtils.showToast(getApplicationContext(), "输入内容不能为空");
				}
			}
		});
		
	}
	
	///拦截物理返回键
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		//跳到桌面
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		finish();
	}
	
	///拦截物理键
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

}
