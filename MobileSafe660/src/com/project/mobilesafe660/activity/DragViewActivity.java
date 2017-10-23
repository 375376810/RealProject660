package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;

public class DragViewActivity extends Activity {

	private ImageView ivDrag;
	private int startY;
	private int startX;
	private int mScreenWidth;
	private int mScreenHeight;
	private TextView tvTop;
	private TextView tvBottom;
	long[] mHits = new long[2];//数组长度就是多击次数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		mScreenWidth = wm.getDefaultDisplay().getWidth();
		mScreenHeight = wm.getDefaultDisplay().getHeight();
		
		ivDrag = (ImageView) findViewById(R.id.iv_drag);
		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		
		int lastX = PrefUtils.getInt("lastX", 0, this);
		int lastY = PrefUtils.getInt("lastY", 0, this);
		
		//根据当前位置,显示文本框提示位置
		if (lastY>mScreenHeight/2) {
			//下方
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		} else {
			//上方
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}
		
		//此方法不能在oncreate方法中使用,因为布局还没有开始绘制
		//ivDrag.layout(lastX, lastY, lastX+ivDrag.getWidth(), lastY+ivDrag.getHeight());
		//系统绘制界面流程:1).measure测量宽高2).layout(设定位置)3).draw(绘制).
		//这三个方法必须在oncreate方法结束之后才调用.
		
		//通过修改布局参数,设定位置(布局的父控件是谁,就获取谁的布局参数)
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
		//临时修改布局参数,通过修改布局参数,实现重新设定位置功能
		params.topMargin = lastY;
		params.leftMargin = lastX;
		
		
		//触摸事件
		ivDrag.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//移动,获取移动后的坐标点
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					//计算偏移量
					int dx = endX - startX;
					int dy = endY -startY;
					//根据偏移量更新位置
					int l = ivDrag.getLeft() + dx;
					int t = ivDrag.getTop() + dy;
					int r = ivDrag.getRight() + dx;
					int b = ivDrag.getBottom() + dy;
					//防止控件超出屏幕边界
					if(l<0 || r>mScreenWidth) {
						return true;
					}
					//减去状态栏的高度25dp左右
					if(t<0 || b>mScreenHeight-25) {
						return true;
					}
					
					//根据当前位置,显示文本框提示位置
					if (t>mScreenHeight/2) {
						//下方
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					} else {
						//上方
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}
					//修改布局位置
					ivDrag.layout(l, t, r, b);
					//重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					//抬起,保存当前位置
					PrefUtils.putInt("lastX", ivDrag.getLeft(), getApplicationContext());
					PrefUtils.putInt("lastY", ivDrag.getRight(), getApplicationContext());
					break;
				default:
					break;
				}
				//return true;
				return false;//当同时设置onTouch和onClick时,设为false可以保证这两个事件都响应.
			}
		});
		
		ivDrag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.arraycopy(mHits, 1, mHits,0, mHits.length-1);
				mHits[mHits.length-1] = SystemClock.uptimeMillis();//手机开机时间
				if(SystemClock.uptimeMillis()-mHits[0]<500) {
					//双击居中
					ivDrag.layout(mScreenWidth/2-ivDrag.getWidth()/2, ivDrag.getTop(), mScreenWidth/2+ivDrag.getWidth()/2, ivDrag.getBottom());
					
				}
			}
		});
		
	}

}
