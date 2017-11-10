package com.project.mobilesafe660.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 程序锁数据库创建
 * @author 袁星明
 */
public class AppLockOpenHelper extends SQLiteOpenHelper {
	
	public AppLockOpenHelper(Context context) {
		super(context, "applock.db", null, 1);
	}


	//数据库第一次创建
	@Override
	public void onCreate(SQLiteDatabase db) {
		//字段:_id,package:已加锁包名
		String sql = "create table applock (_id integer primary key autoincrement,package varchar(50))";
		db.execSQL(sql);
	}

	//数据库更新时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
