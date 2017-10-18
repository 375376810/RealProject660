package com.project.mobilesafe660.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 判断服务是否运行
 * @author 袁星明
 */
public class ServiceStatusUtils {

	public static boolean isServiceRunning(String serviceName,Context context) {
		//活动管理器
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		//获取运行的服务,参数表示需要获取的数量
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
