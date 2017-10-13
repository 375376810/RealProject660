package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * 设置向导4
 * 
 * @author 袁星明
 */
public class Setup4Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
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
