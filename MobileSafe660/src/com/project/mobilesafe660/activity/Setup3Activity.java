package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.mobilesafe660.R;

/**
 * 设置向导3
 * @author 袁星明
 */
public class Setup3Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	/** 上一页 **/
	public void previous(View view) {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
	}
	
	/** 下一页 **/
	public void next(View view) {
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
	}
	
}
