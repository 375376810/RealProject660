package com.project.mobilesafe660.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 对SharedPreference的封装
 * @author 袁星明
 */
public class PrefUtils {
	//设置boolean值
	public static void putBoolean(String key,Boolean value,Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	//获取boolean值
	public static Boolean getBoolean(String key,Boolean defValue,Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	//设置String值
	public static void putString(String key,String value,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	//获取String值
	public static String getString(String key,String defValue,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	//设置int值
	public static void putInt(String key,int value,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	//获取int值
	public static int getInt(String key,int defValue,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	//删除一个值
	public static void remove(String key,Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}
}
