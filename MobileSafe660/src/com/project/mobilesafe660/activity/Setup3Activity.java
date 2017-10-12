package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.mobilesafe660.R;

/**
 * ������3
 * @author Ԭ����
 */
public class Setup3Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	/** ��һҳ **/
	public void previous(View view) {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
	}
	
	/** ��һҳ **/
	public void next(View view) {
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
	}
	
}
