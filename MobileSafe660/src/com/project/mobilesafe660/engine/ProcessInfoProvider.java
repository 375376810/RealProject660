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
 * ���������Ϣ�ṩ��
 * @author Ԭ����
 */
public class ProcessInfoProvider {

	/*��ȡ�����еĽ�������*/
	public static int getRunningProcessNum(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		return runningAppProcesses.size();
	}
	
	/*��ȡ�ڴ���Ϣ*/
	public static long getAvailableMemory(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}
	
	/*��ȡ���ڴ�*/
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
			return Long.parseLong(total)*1024;//ת�����ֽ�
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/*��ȡ�����еĽ���*/
	public static ArrayList<ProcessInfo> getRunningProcesse(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		PackageManager packageManager = context.getPackageManager();
		ArrayList<ProcessInfo> list = new ArrayList<ProcessInfo>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			ProcessInfo info = new ProcessInfo();
			String packageName = runningAppProcessInfo.processName;//����
			info.packageName = packageName;
			int pid = runningAppProcessInfo.pid;//����id
			//���ݽ���id�����ڴ���Ϣ
			android.os.Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{pid});
			long memory = processMemoryInfo[0].getTotalPrivateDirty()*1024;//��ȡ��ǰ����ռ���ڴ��С
			info.memory = memory;
			try {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
				String name = applicationInfo.loadLabel(packageManager).toString();
				Drawable icon = applicationInfo.loadIcon(packageManager);
				int flags = applicationInfo.flags;
				if ((flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM) {
					//ϵͳ����
					info.isUser = false;
				} else {
					//�û�����
					info.isUser = true;
				}
				
				info.name = name;
				info.icon = icon;
				
			} catch (NameNotFoundException e) {
				//ĳЩϵͳ����û�����ƺ�ͼ��,���ߴ��쳣
				info.name = info.packageName;
				info.icon = context.getResources().getDrawable(R.drawable.btn_star);
				info.isUser = false;
				e.printStackTrace();
			}
			list.add(info);
		}
		return list;
	}
	
	/*�����̨���з���*/
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
