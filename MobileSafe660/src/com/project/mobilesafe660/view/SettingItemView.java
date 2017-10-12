package com.project.mobilesafe660.view;

import com.project.mobilesafe660.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * �Զ�����Ͽؼ�
 * @author Ԭ����
 */
public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.project.mobilesafe660";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbCheck;
	private String mdescOn;
	private String mdescOff;

	public SettingItemView(Context context) {
		super(context);
		initView();
	}
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		String title = attrs.getAttributeValue(NAMESPACE, "title");
		mdescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mdescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		setTitle(title);
	}
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	/** ��ʼ������ **/
	public void initView() {
		View child = View.inflate(getContext(), R.layout.setting_item_view, null);//��ʼ����Ͽؼ�����
		
		tvTitle = (TextView) child.findViewById(R.id.tv_title);
		tvDesc = (TextView) child.findViewById(R.id.tv_desc);
		cbCheck = (CheckBox) child.findViewById(R.id.cb_check);
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
	
	/** �ж��Ƿ�ѡ **/
	public boolean isChecked(){
		return cbCheck.isChecked();
	}
	
	/** ����ѡ��״̬ **/
	public void setChecked(boolean checked){
		cbCheck.setChecked(checked);
		//����������Ϣ
		if (checked) {
			setDesc(mdescOn);
		} else {
			setDesc(mdescOff);
		}
	}
	
}
