package com.project.mobilesafe660.db.dao;

import java.util.ArrayList;

import com.project.mobilesafe660.db.AppLockOpenHelper;
import com.project.mobilesafe660.db.BlackNumberOpenHelper;
import com.project.mobilesafe660.domain.BlackNumberInfo;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 程序锁增删改查封装
 * crud
 * @author 袁星明
 */
public class AppLockDao {
	//3.声明一个对象
	private static AppLockDao sInstence = null;
	private AppLockOpenHelper mHelper;
	private Context mContext;
	
	//1.私有的构造方法
	private AppLockDao(Context context){
		mHelper = new AppLockOpenHelper(context);
		mContext = context;
	}
	//2.公开方法获取对象
	public static AppLockDao getInstence(Context context) {
		if (sInstence == null) {
			synchronized (AppLockDao.class) {
				if (sInstence == null) {
					sInstence = new AppLockDao(context);					
				}
			}
		}
		return sInstence;
	}
	
	/*增加已加锁*/
	public void add(String packageName) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("package", packageName);
		database.insert("applock", null, values);
		database.close();
		//用内容提供者给内容观察者发通知数据库发生变化
		mContext.getContentResolver().notifyChange(Uri.parse("content://com.project.mobilesafe660/change"), null);		
	}
	
	/*删除已加锁*/
	public void delete(String packageName) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		//database.delete("blacknumber", "number=?", new String[]{number});
		database.delete("applock", "package=?", new String[]{packageName});
		database.close();
		//通知数据库发生变化
		mContext.getContentResolver().notifyChange(Uri.parse("content://com.project.mobilesafe660/change"), null);
	}
	
	
	/*查询是否已加锁*/
	public boolean find(String packageName) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("applock", new String[]{"package"}, "package=?", new String[]{packageName}, null, null, null);
		boolean exist = false;
		if (cursor.moveToFirst()) {
			exist = true;
		}
		cursor.close();
		database.close();
		return exist;
	}
	
	
	/*查询所有已加锁app*/
	public ArrayList<String> findAll() {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("applock", new String[]{"package"}, null, null, null, null, null);
		ArrayList<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String packageName = cursor.getString(0);
			list.add(packageName);
		}
		cursor.close();
		database.close();
		return list;
	}
	
}
