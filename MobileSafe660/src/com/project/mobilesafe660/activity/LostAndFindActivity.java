package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * 手机防盗页面
 * @author 袁星明
 */
public class LostAndFindActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//判断是否第一次进入
		Boolean configed = PrefUtils.getBoolean("configed", false, this);
		if (!configed) {
			//如果第一次使用,进入设置向导1页面
			startActivity(new Intent(this,Setup1Activity.class));
			finish();
		}else {
			setContentView(R.layout.activity_lost_and_find);
			TextView tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			ImageView ivLock = (ImageView) findViewById(R.id.iv_lock);
			String safePhone = PrefUtils.getString("safe_phone", "", this);
			tvSafePhone.setText(safePhone);
			boolean protect = PrefUtils.getBoolean("protect", false, this);
			if (protect) {
				ivLock.setImageResource(R.drawable.lock);
			} else {
				ivLock.setImageResource(R.drawable.unlock);
			}
		}
	}
	
	/** 重新进入向导页面点击事件 **/
	public void reSetup(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
	
}
