package com.project.mobilesafe660.domain;

import android.graphics.drawable.Drawable;

/**
 * 应用信息封装
 * @author 袁星明
 */
public class AppInfo {
	public String name;
	public String packageName;
	public Drawable icon;
	public boolean isRom;//是否安装在手机内存
	public boolean isUser;//true表示用户应用
}
