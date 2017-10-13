package com.project.mobilesafe660.activity;

import com.project.mobilesafe660.utils.ToastUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/** 跳转页面基类 **/
public abstract class BaseSetupActivity extends Activity {

	private GestureDetector mDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 手势识别器
		mDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					// 快速滑动,抛
					// e1:起点坐标 e2:终点坐标
					// velocityX:水平滑动速度 velocityY:竖直滑动速度
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						//竖直方向滑动超过一定范围不允许
						if (Math.abs(e1.getRawY()-e2.getRawY())>200) {
							ToastUtils.showToast(getApplicationContext(), "不要竖直方向滑动,呵呵...");
							return true;
						}
						//滑动速度太慢,也不允许
						if (Math.abs(velocityX)<200) {
							ToastUtils.showToast(getApplicationContext(), "滑动速度太慢了,呵呵...");
							return true;							
						}
						
						// 判断向左划还是向右滑
						if (e2.getRawX() - e1.getRawX() > 100) {// 向右滑,上一页
							showPrevious();
							return true;
						}
						if (e1.getRawX() - e2.getRawX() > 100) {// 向左划,下一页
							showNext();
							return true;
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}
	
	/** 上一页点击 **/
	public void previous(View view) {
		showPrevious();
	}
	/** 下一页点击 **/
	public void next(View view) {
		showNext();
	}

	public abstract void showPrevious();
	
	public abstract void showNext();
	
	/** 当界面被触摸时,走此方法 **/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//将事件委托给手势识别器处理
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
