package com.project.mobilesafe660.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ����������ݿ��װ
 * @author Ԭ����
 */
public class AntiVirusDao {
	
	private static final String PATH = "data/data/com.project.mobilesafe660/files/antivirus.db";

	/** �����ݿ��ѯ�Ƿ�Ϊ�������� **/
	public static boolean isVirus(String md5) {
		String address = "δ֪����";
		//��ֻ����ʽ�����ݿ�,��Ŀ��װ���ֻ���ֻ֧�ִ�data/dataĿ¼�����ݿ�,���ܴ�assetsĿ¼����db�ļ�
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
