package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;

/**
 * �ֻ�����ҳ��
 * @author Ԭ����
 */
public class LostAndFindActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//�ж��Ƿ��һ�ν���
		Boolean configed = PrefUtils.getBoolean("configed", false, this);
		if (!configed) {
			//�����һ��ʹ��,����������1ҳ��
			startActivity(new Intent(this,Setup1Activity.class));
			finish();
		}else {
			setContentView(R.layout.activity_lost_and_find);
		}
	}
	
	/** ���½�����ҳ�����¼� **/
	public void reSetup(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
	
}
