package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
			TextView tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			ImageView ivLock = (ImageView) findViewById(R.id.iv_lock);
			String safePhone = PrefUtils.getString("safe_phone", "", this);
			tvSafePhone.setText(safePhone);
			boolean protect = PrefUtils.getBoolean("protect", false, this);
			if (protect) {
				ivLock.setImageResource(R.drawable.lock);
			} else {
				ivLock.setImageResource(R.drawable.unlock);
			}
		}
	}
	
	/** ���½�����ҳ�����¼� **/
	public void reSetup(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
	
}
