package com.project.mobilesafe660.db.dao;

import com.project.mobilesafe660.db.BlackNumberOpenHelper;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库更删改查封装
 * crud
 * @author 袁星明
 */
public class BlackNumberDao {
	//3.声明一个对象
	private static BlackNumberDao sInstence = null;
	private BlackNumberOpenHelper mHelper;
	//1.私有的构造方法
	private BlackNumberDao(Context context){
		mHelper = new BlackNumberOpenHelper(context);
	}
	//2.公开方法获取对象
	public static BlackNumberDao getInstence(Context context) {
		if (sInstence == null) {
			synchronized (BlackNumberDao.class) {
				if (sInstence == null) {
					sInstence = new BlackNumberDao(context);					
				}
			}
		}
		return sInstence;
	}
	
	/*增加黑名单*/
	public void add(String number ,int mode) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		database.insert("blacknumber", null, values);
		database.close();
	}
	
	/*删除黑名单*/
	public void delete(String number) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		database.delete("blacknumber", "number=?", new String[]{number});
		database.close();
	}
	
	/*更新拦截模式*/
	public void update(String number,int mode) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		database.update("blacknumber", values, "number=?", new String[]{number});
		database.close();
	}
	
	/*查询黑名单是否存在*/
	public boolean find(String number) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("blacknumber", new String[]{"number","mode"}, "number=?", new String[]{number}, null, null, null);
		boolean exist = false;
		if (cursor.moveToFirst()) {
			exist = true;
		}
		cursor.close();
		database.close();
		return exist;
	}
	
	/*根据电话号码查询拦截模式*/
	public int findMode(String number) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
		int mode = -1;
		if (cursor.moveToFirst()) {
			mode = cursor.getInt(0);
		}
		cursor.close();
		database.close();
		return mode;
	}
	
	/*查询所有数据*/
	public void findAll() {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("blacknumber", new String[]{"number","mode"}, null, null, null, null, null);
		while (cursor.moveToNext()) {
			String number = cursor.getString(0);
			int mode = cursor.getInt(1);
		}
		cursor.close();
		database.close();
	}

	
}
