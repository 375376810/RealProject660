package com.project.mobilesafe660.utils;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.integer;

public class Md5Utils {

	/** ��md5�㷨�������� **/
	public static String encode(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : bytes) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			String md5 = sb.toString();
			return md5;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** �����ļ�md5�㷨 **/
	public static String encodeFile(String filePath) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			FileInputStream in = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer))!=-1) {
				digest.update(buffer,0,len);
			}
			byte[] bytes = digest.digest();
			//ת��Ϊ32λ�ַ���
			StringBuffer sb = new StringBuffer();
			for (byte b : bytes) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			String md5 = sb.toString();
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
