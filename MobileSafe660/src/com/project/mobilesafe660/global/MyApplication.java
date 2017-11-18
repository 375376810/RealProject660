package com.project.mobilesafe660.global;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;

/**
 * 自定义application
 * @author 袁星明
 *
 */
public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		//设置未捕获异常处理器
		Thread.setDefaultUncaughtExceptionHandler(new MyHandler());
	}

	class MyHandler implements UncaughtExceptionHandler {

		//一旦有未捕获异常就会回调此方法
		@Override
		public void uncaughtException(Thread arg0, Throwable arg1) {
			System.out.println("发现未捕获异常");
			arg1.printStackTrace();
			
			//收集崩溃日志
			try {
				PrintWriter err = new PrintWriter(Environment.getExternalStorageDirectory()+"/err660.log");
				arg1.printStackTrace(err);
				err.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			
			//停止当前进程
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		
	}
}
