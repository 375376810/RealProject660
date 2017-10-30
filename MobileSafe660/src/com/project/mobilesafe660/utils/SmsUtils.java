package com.project.mobilesafe660.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

/**
 * 短信备份工具
 * @author 袁星明
 */
public class SmsUtils {
	
	/*备份*/
	public static void backup(Context context,File xmlFile, SmsBackupCallback smsBackupCallback){
		try {
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), new String[]{"address","date","type","body"}, null, null, null);
			//短信数量设置为进度最大值
			//progressDialog.setMax(cursor.getCount());
			//回调短信总数
			smsBackupCallback.preSmsBackup(cursor.getCount());
			XmlSerializer xmlSerializer = Xml.newSerializer();
			xmlSerializer.setOutput(new FileOutputStream(xmlFile),"utf-8");
			xmlSerializer.startDocument("utf-8", false);
				xmlSerializer.startTag(null, "smss");
				int progress = 0;
					while (cursor.moveToNext()) {
						xmlSerializer.startTag(null,"sms");
							xmlSerializer.startTag(null, "address");
								String address = cursor.getString(cursor.getColumnIndex("address"));
								xmlSerializer.text(address);
							xmlSerializer.endTag(null, "address");
							xmlSerializer.startTag(null, "date");
								String date = cursor.getString(cursor.getColumnIndex("date"));
								xmlSerializer.text(date);
							xmlSerializer.endTag(null, "date");
							xmlSerializer.startTag(null, "type");
								String type = cursor.getString(cursor.getColumnIndex("type"));
								xmlSerializer.text(type);
							xmlSerializer.endTag(null, "type");
							xmlSerializer.startTag(null, "body");
								String body = cursor.getString(cursor.getColumnIndex("body"));
								xmlSerializer.text(body);
							xmlSerializer.endTag(null, "body");
						xmlSerializer.endTag(null, "sms");
						progress++;
						//更新进度
						//progressDialog.setProgress(progress);
						//回调更新进度
						smsBackupCallback.onSmsBackup(progress);
						//模拟耗时操作
						//Thread.sleep(500);
					}
				xmlSerializer.endTag(null, "smss");
			xmlSerializer.endDocument();
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 短信备份的回调接口 **/
	public interface SmsBackupCallback{
		//备份前,count表示备份总数
		public void preSmsBackup(int count);
		//正在备份,progress表示备份进度
		public void onSmsBackup(int progress);
	}
	
}
