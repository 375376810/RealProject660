package com.project.mobilesafe660.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���������ݿ�
 * @author Ԭ����
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper {
	
	public BlackNumberOpenHelper(Context context) {
		super(context, "blacknumber.db", null, 1);
	}


	//���ݿ��һ�δ���
	@Override
	public void onCreate(SQLiteDatabase db) {
		//�ֶ�:_id,number(�绰����),mode(����ģʽ):1.���ص绰2.���ض���3.����ȫ��
		String sql = "create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode integer)";
		db.execSQL(sql);
	}

	//���ݿ����ʱ����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
