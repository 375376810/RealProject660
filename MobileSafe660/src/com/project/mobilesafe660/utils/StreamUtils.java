package com.project.mobilesafe660.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	/** 将流数据转换为String **/
	public static String streamToString(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = in.read(buffer))!=-1) {
			out.write(buffer,0,len);
		}
		in.close();
		out.close();
		return out.toString();
	}

}
