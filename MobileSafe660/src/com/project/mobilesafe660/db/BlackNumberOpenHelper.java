package com.project.mobilesafe660.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 黑名单数据库
 * @author 袁星明
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper {
	
	public BlackNumberOpenHelper(Context context) {
		super(context, "blacknumber.db", null, 1);
	}


	//数据库第一次创建
	@Override
	public void onCreate(SQLiteDatabase db) {
		//字段:_id,number(电话号码),mode(拦截模式):1.拦截电话2.拦截短信3.拦截全部
		String sql = "create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode integer)";
		db.execSQL(sql);
	}

	//数据库更新时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
