package com.project.mobilesafe660.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 归属地查询的数据库封装
 * @author 袁星明
 */
public class AddressDao {
	
	private static final String PATH = "data/data/com.project.mobilesafe660/files/address.db";

	/** 根据电话号码返回归属地 **/
	public static String getAddress(String number) {
		String address = "未知号码";
		//以只读方式打开数据库,只支持从data/data目录打开数据库
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		//要先判断手机号码是否有效
		//1 + [3-8] + 9位数字
		//正则表达式:^1[3-8]\d{9}$
		if (number.matches("^1[3-8]\\d{9}$")) {
			//开始查询,手机号码前7位.
			Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id = ?)", new String[]{number.substring(0,7)});
			if (cursor.moveToFirst()) {
				address = cursor.getString(0);
			}
			cursor.close();
		} else {
			switch (number.length()) {
			case 3:
				address = "报警电话";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				address = "客服电话";
				break;
			case 7:
				address = "";
				break;
			case 8:
				address = "本地电话";
				break;
			default:
				if (number.startsWith("0")&&number.length()>=11&&number.length()<=12) {
					//有可能是长途电话
					//先查询4位区号
					Cursor cursor = database.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1,4)});
					if (cursor.moveToFirst()) {
						//查到4位区号
						address = cursor.getString(0);
					}
					cursor.close();
					if ("未知号码".equals(address)) {
						//没有查到4位区号,继续查3位区号
						cursor = database.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1,3)});
						if (cursor.moveToFirst()) {
							//查到3位区号
							address = cursor.getString(0);
						}
						cursor.close();
					}
				}
				break;
			}
		}
		database.close();
		return address;
	}
}
