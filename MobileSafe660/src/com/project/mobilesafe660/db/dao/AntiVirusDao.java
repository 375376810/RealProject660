package com.project.mobilesafe660.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 病毒库的数据库封装
 * @author 袁星明
 */
public class AntiVirusDao {
	
	private static final String PATH = "data/data/com.project.mobilesafe660/files/antivirus.db";

	/** 从数据库查询是否为病毒程序 **/
	public static boolean isVirus(String md5) {
		String address = "未知号码";
		//以只读方式打开数据库,项目安装到手机后只支持从data/data目录打开数据库,不能从assets目录访问db文件
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = database.rawQuery("select * from datable where md5=?", new String[]{md5});;
		boolean isVirus;
		if (cursor.moveToFirst()) {
			isVirus = true;
		} else {
			isVirus = false;
		}
		
		cursor.close();
		database.close();
		return isVirus;
	}
}
