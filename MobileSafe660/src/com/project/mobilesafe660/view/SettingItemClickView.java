package com.project.mobilesafe660.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.mobilesafe660.R;

/**
 * 自定义组合控件
 * 1.写一个类继承RlativeLayout(ViewGroup)
 * 2.写布局文件
 * 3.将布局添加到RlativeLayout中(InitView方法)
 * 4.增加api
 * 5.自定义属性(I.values/attrs.xml;II.声明命名空间;III.在自定义view中配置属性;IV.在自定义view中加载属性值)
 * @author 袁星明
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

	/** 初始化布局 **/
	public void initView() {
		//初始化组合控件布局
		View child = View.inflate(getContext(), R.layout.setting_item_click_view, null);		
		tvTitle = (TextView) child.findViewById(R.id.tv_title);
		tvDesc = (TextView) child.findViewById(R.id.tv_desc);
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
	
}
