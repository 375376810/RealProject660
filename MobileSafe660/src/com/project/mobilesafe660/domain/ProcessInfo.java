package com.project.mobilesafe660.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程信息封装
 * @author 袁星明
 */
public class ProcessInfo {
	public String name;
	public String packageName;
	public Drawable icon;
	public long memory;
	public boolean isUser;
	public boolean isChecked;//表示当前item是否被勾选
}
