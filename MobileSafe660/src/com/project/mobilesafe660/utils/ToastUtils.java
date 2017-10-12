package com.project.mobilesafe660.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 对Toast重新封装
 * @author 袁星明
 */
public class ToastUtils {

	/** 直接显示对话框 **/
	public static void showToast(Context context,String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
