package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.mobilesafe660.R;

/**
 * ������2
 * @author Ԭ����
 */
public class Setup2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}
	
	/** ��һҳ **/
	public void previous(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
	
	/** ��һҳ **/
	public void next(View view) {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
	}
	
}
