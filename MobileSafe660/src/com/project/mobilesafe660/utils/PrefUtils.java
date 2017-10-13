package com.project.mobilesafe660.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ��SharedPreference�ķ�װ
 * @author Ԭ����
 */
public class PrefUtils {
	//����booleanֵ
	public static void putBoolean(String key,Boolean value,Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	//��ȡbooleanֵ
	public static Boolean getBoolean(String key,Boolean defValue,Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	//����Stringֵ
	public static void putString(String key,String value,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	//��ȡStringֵ
	public static String getString(String key,String defValue,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	//����intֵ
	public static void putInt(String key,int value,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	//��ȡintֵ
	public static int getInt(String key,int defValue,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	//ɾ��һ��ֵ
	public static void remove(String key,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}
}
