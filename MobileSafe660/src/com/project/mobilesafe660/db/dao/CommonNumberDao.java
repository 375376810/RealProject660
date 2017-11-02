package com.project.mobilesafe660.db.dao;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 常用号码查询的数据库封装
 * @author 袁星明
 */
public class CommonNumberDao {
	
	private static final String PATH = "data/data/com.project.mobilesafe660/files/commonnum.db";

	/*获取常用号码组的信息*/
	public static ArrayList<GroupInfo> getCommonNumberGroup() {
		//以只读方式打开数据库,只支持从data/data目录打开数据库
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = database.query("classlist", new String[]{"name","idx"}, null, null, null, null, null);
		ArrayList<GroupInfo> list = new ArrayList<CommonNumberDao.GroupInfo>();
		while (cursor.moveToNext()) {
			GroupInfo info = new GroupInfo();
			String name = cursor.getString(0);
			String idx = cursor.getString(1);
			info.name = name;
			info.idx = idx;
			info.children = getCommonNumberChildren(idx, database);
			list.add(info);
		}
		cursor.close();
		database.close();
		return list;
	}
	
	/*获取某个组孩子的信息*/
	public static ArrayList<ChildInfo> getCommonNumberChildren(String idx,SQLiteDatabase database) {
		Cursor cursor = database.query("table"+idx, new String[]{"number","name"}, null, null, null, null, null, null);
		ArrayList<ChildInfo> list = new ArrayList<CommonNumberDao.ChildInfo>();
		while (cursor.moveToNext()) {
			ChildInfo info = new ChildInfo();
			String number = cursor.getString(0);
			String name = cursor.getString(1);
			info.number = number;
			info.name = name;
			list.add(info);
		}
		cursor.close();
		return list;
	}
	
	public static class GroupInfo {
		public String name;
		public String idx;
		public ArrayList<ChildInfo> children;
	}
	public static class ChildInfo {
		public String number;
		public String name;
	}
	
}
