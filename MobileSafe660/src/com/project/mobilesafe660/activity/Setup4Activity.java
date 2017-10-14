package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * 设置向导4
 * 
 * @author 袁星明
 */
public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cbProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
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
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("防盗保护已经开启");
					PrefUtils.putBoolean("protect", true, getApplicationContext());
				} else {
					cbProtect.setText("您没有开启防盗保护");
					PrefUtils.putBoolean("protect", false, getApplicationContext());
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
		PrefUtils.putBoolean("configed", true, this);
		startActivity(new Intent(this, LostAndFindActivity.class));
		finish();
		// 两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

}
