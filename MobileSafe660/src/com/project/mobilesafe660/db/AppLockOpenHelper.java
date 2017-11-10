package com.project.mobilesafe660.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���������ݿⴴ��
 * @author Ԭ����
 */
public class AppLockOpenHelper extends SQLiteOpenHelper {
	
	public AppLockOpenHelper(Context context) {
		super(context, "applock.db", null, 1);
	}


	//���ݿ��һ�δ���
	@Override
	public void onCreate(SQLiteDatabase db) {
		//�ֶ�:_id,package:�Ѽ�������
		String sql = "create table applock (_id integer primary key autoincrement,package varchar(50))";
		db.execSQL(sql);
	}

	//���ݿ����ʱ����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
