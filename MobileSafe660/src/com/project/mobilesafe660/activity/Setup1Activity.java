package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.mobilesafe660.R;

/**
 * ������1
 * @author Ԭ����
 */
public class Setup1Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	
	/** ��һҳ **/
	public void next(View view) {
		startActivity(new Intent(this,Setup2Activity.class));
		finish();
	}
	
}
