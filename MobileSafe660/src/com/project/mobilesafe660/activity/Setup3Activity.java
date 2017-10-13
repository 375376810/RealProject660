package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;

import com.project.mobilesafe660.R;

/**
 * 设置向导3
 * @author 袁星明
 */
public class Setup3Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	/** 上一页 **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		//两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_previous_in, R.anim.anim_previous_out);
	}

	/** 下一页 **/
	@Override
	public void showNext() {
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
		//两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}
	
}
