package com.project.mobilesafe660.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ��Toast���·�װ
 * @author Ԭ����
 */
public class ToastUtils {

	/** ֱ����ʾ�Ի��� **/
	public static void showToast(Context context,String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
