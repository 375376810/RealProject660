package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.mobilesafe660.R;

/**
 * 高级工具模块
 * @author 袁星明
 *
 */
public class AdvancedToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_tools);
	}
	
	/** 电话归属地查询点击 **/
	public void addressQuery(View view) {
		startActivity(new Intent(this,AdressQueryActivity.class));
	}

}
