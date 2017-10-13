package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;

import com.project.mobilesafe660.R;

/**
 * 设置向导1
 * @author 袁星明
 */
public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	
	@Override
	public void showPrevious() {
	}

	/** 下一页 **/
	@Override
	public void showNext() {
		startActivity(new Intent(this,Setup2Activity.class));
		finish();
		//两个activity之间切换动画,应该放在finish()后面运行
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}
	
	
	
}
