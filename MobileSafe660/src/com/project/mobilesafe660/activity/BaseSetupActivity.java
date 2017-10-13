package com.project.mobilesafe660.activity;

import com.project.mobilesafe660.utils.ToastUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/** ��תҳ����� **/
public abstract class BaseSetupActivity extends Activity {

	private GestureDetector mDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����ʶ����
		mDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					// ���ٻ���,��
					// e1:������� e2:�յ�����
					// velocityX:ˮƽ�����ٶ� velocityY:��ֱ�����ٶ�
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						//��ֱ���򻬶�����һ����Χ������
						if (Math.abs(e1.getRawY()-e2.getRawY())>200) {
							ToastUtils.showToast(getApplicationContext(), "��Ҫ��ֱ���򻬶�,�Ǻ�...");
							return true;
						}
						//�����ٶ�̫��,Ҳ������
						if (Math.abs(velocityX)<200) {
							ToastUtils.showToast(getApplicationContext(), "�����ٶ�̫����,�Ǻ�...");
							return true;							
						}
						
						// �ж����󻮻������һ�
						if (e2.getRawX() - e1.getRawX() > 100) {// ���һ�,��һҳ
							showPrevious();
							return true;
						}
						if (e1.getRawX() - e2.getRawX() > 100) {// ����,��һҳ
							showNext();
							return true;
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}
	
	/** ��һҳ��� **/
	public void previous(View view) {
		showPrevious();
	}
	/** ��һҳ��� **/
	public void next(View view) {
		showNext();
	}

	public abstract void showPrevious();
	
	public abstract void showNext();
	
	/** �����汻����ʱ,�ߴ˷��� **/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//���¼�ί�и�����ʶ��������
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
