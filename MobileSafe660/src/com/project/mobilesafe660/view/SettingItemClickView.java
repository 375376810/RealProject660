package com.project.mobilesafe660.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.mobilesafe660.R;

/**
 * �Զ�����Ͽؼ�
 * 1.дһ����̳�RlativeLayout(ViewGroup)
 * 2.д�����ļ�
 * 3.��������ӵ�RlativeLayout��(InitView����)
 * 4.����api
 * 5.�Զ�������(I.values/attrs.xml;II.���������ռ�;III.���Զ���view����������;IV.���Զ���view�м�������ֵ)
 * @author Ԭ����
 */
public class SettingItemClickView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;

	public SettingItemClickView(Context context) {
		super(context);
		initView();
	}
	public SettingItemClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	public SettingItemClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	/** ��ʼ������ **/
	public void initView() {
		//��ʼ����Ͽؼ�����
		View child = View.inflate(getContext(), R.layout.setting_item_click_view, null);		
		tvTitle = (TextView) child.findViewById(R.id.tv_title);
		tvDesc = (TextView) child.findViewById(R.id.tv_desc);
		this.addView(child);//��������Ӹ���ǰ��Relativelayout����
	}
	
	/** ���ñ��� **/
	public void setTitle(String title) {
		tvTitle.setText(title);
	}
	
	/** �������� **/
	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}
	
}
