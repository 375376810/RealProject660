package com.project.mobilesafe660.view;

import com.project.mobilesafe660.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义组合控件
 * @author 袁星明
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

	/** 初始化布局 **/
	public void initView() {
		View child = View.inflate(getContext(), R.layout.setting_item_view, null);//初始化组合控件布局
		
		tvTitle = (TextView) child.findViewById(R.id.tv_title);
		tvDesc = (TextView) child.findViewById(R.id.tv_desc);
		cbCheck = (CheckBox) child.findViewById(R.id.cb_check);
		this.addView(child);//将布局添加给当前的Relativelayout对象
	}
	
	/** 设置标题 **/
	public void setTitle(String title) {
		tvTitle.setText(title);
	}
	
	/** 设置描述 **/
	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}
	
	/** 判断是否勾选 **/
	public boolean isChecked(){
		return cbCheck.isChecked();
	}
	
	/** 设置选中状态 **/
	public void setChecked(boolean checked){
		cbCheck.setChecked(checked);
		//更新描述信息
		if (checked) {
			setDesc(mdescOn);
		} else {
			setDesc(mdescOff);
		}
	}
	
}
