package com.project.mobilesafe660.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * �жϷ����Ƿ�����
 * @author Ԭ����
 */
public class ServiceStatusUtils {

	public static boolean isServiceRunning(String serviceName,Context context) {
		//�������
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		//��ȡ���еķ���,������ʾ��Ҫ��ȡ������
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();
			if (className.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}
}
