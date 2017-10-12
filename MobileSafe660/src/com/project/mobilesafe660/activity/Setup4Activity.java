package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * 设置向导4
 * @author 袁星明
 */
public class Setup4Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
	}
	
	/** 上一页 **/
	public void previous(View view) {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
	}
	
	/** 下一页 **/
	public void next(View view) {
		PrefUtils.putBoolean("configed", true, this);
		startActivity(new Intent(this, LostAndFindActivity.class));
		finish();
	}
	
}
