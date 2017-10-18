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
		} else {
			switch (number.length()) {
			case 3:
				address = "�����绰";
				break;
			case 4:
				address = "ģ����";
				break;
			case 5:
				address = "�ͷ��绰";
				break;
			case 7:
				address = "";
				break;
			case 8:
				address = "���ص绰";
				break;
			default:
				if (number.startsWith("0")&&number.length()>=11&&number.length()<=12) {
					//�п����ǳ�;�绰
					//�Ȳ�ѯ4λ����
					Cursor cursor = database.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1,4)});
					if (cursor.moveToFirst()) {
						//�鵽4λ����
						address = cursor.getString(0);
					}
					cursor.close();
					if ("δ֪����".equals(address)) {
						//û�в鵽4λ����,������3λ����
						cursor = database.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1,3)});
						if (cursor.moveToFirst()) {
							//�鵽3λ����
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
