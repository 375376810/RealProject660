package com.project.mobilesafe660.global;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;

/**
 * �Զ���application
 * @author Ԭ����
 *
 */
public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		//����δ�����쳣������
		Thread.setDefaultUncaughtExceptionHandler(new MyHandler());
	}

	class MyHandler implements UncaughtExceptionHandler {

		//һ����δ�����쳣�ͻ�ص��˷���
		@Override
		public void uncaughtException(Thread arg0, Throwable arg1) {
			System.out.println("����δ�����쳣");
			arg1.printStackTrace();
			
			//�ռ�������־
			try {
				PrintWriter err = new PrintWriter(Environment.getExternalStorageDirectory()+"/err660.log");
				arg1.printStackTrace(err);
				err.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			
			//ֹͣ��ǰ����
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		
	}
}
