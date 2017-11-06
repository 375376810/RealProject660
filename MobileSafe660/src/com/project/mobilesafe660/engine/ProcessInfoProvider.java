package com.project.mobilesafe660.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.project.mobilesafe660.domain.ProcessInfo;

/**
 * 进程相关信息提供者
 * @author 袁星明
 */
public class ProcessInfoProvider {

	/*获取运行中的进程数量*/
	public static int getRunningProcessNum(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		return runningAppProcesses.size();
	}
	
	/*获取内存信息*/
	public static long getAvailableMemory(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}
	
	/*获取总内存*/
	public static long getTotalMemory(Context context) {
		//ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//MemoryInfo memoryInfo = new MemoryInfo();
		//activityManager.getMemoryInfo(memoryInfo);
		//return memoryInfo.totalMem;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"));
			String readLine = reader.readLine();
			char[] charArray = readLine.toCharArray();
			StringBuffer stringBuffer = new StringBuffer();
			for (char c : charArray) {
				if (c>'0' && c<'9') {
					stringBuffer.append(c);
				}
			}
			String total = stringBuffer.toString();
			return Long.parseLong(total)*1024;//转换成字节
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/*获取运行中的进程*/
	public static ArrayList<ProcessInfo> getRunningProcesse(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		PackageManager packageManager = context.getPackageManager();
		ArrayList<ProcessInfo> list = new ArrayList<ProcessInfo>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			ProcessInfo info = new ProcessInfo();
			String packageName = runningAppProcessInfo.processName;//包名
			info.packageName = packageName;
			int pid = runningAppProcessInfo.pid;//进程id
			//根据进程id返回内存信息
			android.os.Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{pid});
			long memory = processMemoryInfo[0].getTotalPrivateDirty()*1024;//获取当前进程占用内存大小
			info.memory = memory;
			try {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
				String name = applicationInfo.loadLabel(packageManager).toString();
				Drawable icon = applicationInfo.loadIcon(packageManager);
				int flags = applicationInfo.flags;
				if ((flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM) {
					//系统进程
					info.isUser = false;
				} else {
					//用户进程
					info.isUser = true;
				}
				
				info.name = name;
				info.icon = icon;
				
			} catch (NameNotFoundException e) {
				//某些系统进程没有名称和图标,会走次异常
				info.name = info.packageName;
				info.icon = context.getResources().getDrawable(R.drawable.btn_star);
				info.isUser = false;
				e.printStackTrace();
			}
			list.add(info);
		}
		return list;
	}
	
	/*清理后台所有方法*/
	public static void killAll(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			String packageName = runningAppProcessInfo.processName;
			if (packageName.equals(context.getPackageName())) {
				continue;
			}
			am.killBackgroundProcesses(packageName);			
		}
	}
	
	
}
