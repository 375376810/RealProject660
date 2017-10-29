package com.project.mobilesafe660.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
	public static void backup(Context context,File xmlFile){
		try {
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), new String[]{"address","date","type","body"}, null, null, null);
			XmlSerializer xmlSerializer = Xml.newSerializer();
			xmlSerializer.setOutput(new FileOutputStream(xmlFile),"utf-8");
			xmlSerializer.startDocument("utf-8", false);
				xmlSerializer.startTag(null, "smss");
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
					}
				xmlSerializer.endTag(null, "smss");
			xmlSerializer.endDocument();
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
