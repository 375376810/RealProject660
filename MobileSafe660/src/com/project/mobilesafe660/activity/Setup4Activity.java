package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * ������4
 * 
 * @author Ԭ����
 */
public class Setup4Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
	}

	/** ��һҳ **/
	@Override
	public void showPrevious() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// ����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_previous_in,
				R.anim.anim_previous_out);
	}

	/** ��һҳ **/
	@Override
	public void showNext() {
		PrefUtils.putBoolean("configed", true, this);
		startActivity(new Intent(this, LostAndFindActivity.class));
		finish();
		// ����activity֮���л�����,Ӧ�÷���finish()��������
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

}
