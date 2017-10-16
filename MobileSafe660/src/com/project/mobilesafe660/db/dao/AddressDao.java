package com.project.mobilesafe660.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * �����ز�ѯ�����ݿ��װ
 * @author Ԭ����
 */
public class AddressDao {
	
	private static final String PATH = "data/data/com.project.mobilesafe660/files/address.db";

	/** ���ݵ绰���뷵�ع����� **/
	public static String getAddress(String number) {
		String address = "δ֪����";
		//��ֻ����ʽ�����ݿ�,ֻ֧�ִ�data/dataĿ¼�����ݿ�
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		//Ҫ���ж��ֻ������Ƿ���Ч
		//1 + [3-8] + 9λ����
		//������ʽ:^1[3-8]\d{9}$
		if (number.matches("^1[3-8]\\d{9}$")) {
			//��ʼ��ѯ,�ֻ�����ǰ7λ.
			Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id = ?)", new String[]{number.substring(0,7)});
			if (cursor.moveToFirst()) {
				address = cursor.getString(0);
			}
			cursor.close();
		}
		database.close();
		return address;
	}
}
