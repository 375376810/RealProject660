package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;

import com.project.mobilesafe660.R;

/**
 * ������3
 * @author Ԭ����
 */
public class Setup3Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	/** ��һҳ **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		//����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_previous_in, R.anim.anim_previous_out);
	}

	/** ��һҳ **/
	@Override
	public void showNext() {
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
		//����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}
	
}
