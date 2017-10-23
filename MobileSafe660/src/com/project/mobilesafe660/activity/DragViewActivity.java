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
	long[] mHits = new long[2];//���鳤�Ⱦ��Ƕ������

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
		
		//���ݵ�ǰλ��,��ʾ�ı�����ʾλ��
		if (lastY>mScreenHeight/2) {
			//�·�
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		} else {
			//�Ϸ�
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}
		
		//�˷���������oncreate������ʹ��,��Ϊ���ֻ�û�п�ʼ����
		//ivDrag.layout(lastX, lastY, lastX+ivDrag.getWidth(), lastY+ivDrag.getHeight());
		//ϵͳ���ƽ�������:1).measure�������2).layout(�趨λ��)3).draw(����).
		//����������������oncreate��������֮��ŵ���.
		
		//ͨ���޸Ĳ��ֲ���,�趨λ��(���ֵĸ��ؼ���˭,�ͻ�ȡ˭�Ĳ��ֲ���)
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
		//��ʱ�޸Ĳ��ֲ���,ͨ���޸Ĳ��ֲ���,ʵ�������趨λ�ù���
		params.topMargin = lastY;
		params.leftMargin = lastX;
		
		
		//�����¼�
		ivDrag.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//��ʼ���������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//�ƶ�,��ȡ�ƶ���������
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					//����ƫ����
					int dx = endX - startX;
					int dy = endY -startY;
					//����ƫ��������λ��
					int l = ivDrag.getLeft() + dx;
					int t = ivDrag.getTop() + dy;
					int r = ivDrag.getRight() + dx;
					int b = ivDrag.getBottom() + dy;
					//��ֹ�ؼ�������Ļ�߽�
					if(l<0 || r>mScreenWidth) {
						return true;
					}
					//��ȥ״̬���ĸ߶�25dp����
					if(t<0 || b>mScreenHeight-25) {
						return true;
					}
					
					//���ݵ�ǰλ��,��ʾ�ı�����ʾλ��
					if (t>mScreenHeight/2) {
						//�·�
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					} else {
						//�Ϸ�
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}
					//�޸Ĳ���λ��
					ivDrag.layout(l, t, r, b);
					//���³�ʼ���������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					//̧��,���浱ǰλ��
					PrefUtils.putInt("lastX", ivDrag.getLeft(), getApplicationContext());
					PrefUtils.putInt("lastY", ivDrag.getRight(), getApplicationContext());
					break;
				default:
					break;
				}
				//return true;
				return false;//��ͬʱ����onTouch��onClickʱ,��Ϊfalse���Ա�֤�������¼�����Ӧ.
			}
		});
		
		ivDrag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.arraycopy(mHits, 1, mHits,0, mHits.length-1);
				mHits[mHits.length-1] = SystemClock.uptimeMillis();//�ֻ�����ʱ��
				if(SystemClock.uptimeMillis()-mHits[0]<500) {
					//˫������
					ivDrag.layout(mScreenWidth/2-ivDrag.getWidth()/2, ivDrag.getTop(), mScreenWidth/2+ivDrag.getWidth()/2, ivDrag.getBottom());
					
				}
			}
		});
		
	}

}
