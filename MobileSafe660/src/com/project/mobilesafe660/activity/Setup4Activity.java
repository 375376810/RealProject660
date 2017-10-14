package com.project.mobilesafe660.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * ������4
 * 
 * @author Ԭ����
 */
public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cbProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cbProtect = (CheckBox) findViewById(R.id.cb_protect);
		Boolean protect = PrefUtils.getBoolean("protect", false, this);
		if (protect) {
			cbProtect.setChecked(true);
			cbProtect.setText("���������Ѿ�����");
		} else {
			cbProtect.setChecked(false);
			cbProtect.setText("��û�п�����������");
		}
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("���������Ѿ�����");
					PrefUtils.putBoolean("protect", true, getApplicationContext());
				} else {
					cbProtect.setText("��û�п�����������");
					PrefUtils.putBoolean("protect", false, getApplicationContext());
				}
			}
		});
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
