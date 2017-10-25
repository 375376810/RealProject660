package com.project.mobilesafe660.db.dao;

import java.util.ArrayList;

import com.project.mobilesafe660.db.BlackNumberOpenHelper;
import com.project.mobilesafe660.domain.BlackNumberInfo;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ���ݿ��ɾ�Ĳ��װ
 * crud
 * @author Ԭ����
 */
public class BlackNumberDao {
	//3.����һ������
	private static BlackNumberDao sInstence = null;
	private BlackNumberOpenHelper mHelper;
	//1.˽�еĹ��췽��
	private BlackNumberDao(Context context){
		mHelper = new BlackNumberOpenHelper(context);
	}
	//2.����������ȡ����
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
	
	/*���Ӻ�����*/
	public void add(String number ,int mode) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		database.insert("blacknumber", null, values);
		database.close();
	}
	
	/*ɾ��������*/
	public void delete(String number) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		//database.delete("blacknumber", "number=?", new String[]{number});
		database.delete("blacknumber", null, null);
		database.close();
	}
	
	/*��������ģʽ*/
	public void update(String number,int mode) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		database.update("blacknumber", values, "number=?", new String[]{number});
		database.close();
	}
	
	/*��ѯ�������Ƿ����*/
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
	
	/*���ݵ绰�����ѯ����ģʽ*/
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
	
	/*��ѯ��������*/
	public ArrayList<BlackNumberInfo> findAll() {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("blacknumber", new String[]{"number","mode"}, null, null, null, null, null);
		ArrayList<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			int mode = cursor.getInt(1);
			info.mode = mode;
			info.number = number;
			list.add(info);
		}
		cursor.close();
		database.close();
		return list;
	}
	
	/*��ҳ��ѯ����*/
	public ArrayList<BlackNumberInfo> findPart(int index) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		//��ҳ��ѯ20������
		Cursor cursor = database.rawQuery("select number, mode from blacknumber order by _id desc limit ?,20", new String[]{index+""});
		
		ArrayList<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			int mode = cursor.getInt(1);
			info.mode = mode;
			info.number = number;
			list.add(info);
		}
		cursor.close();
		database.close();
		return list;
	}
	
	/*��ȡ�������ܸ���*/
	public int getTotalCount() {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select count(*) from blacknumber", null);
		int count = -1;
		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		cursor.close();
		database.close();
		return count;
	}

	
}
