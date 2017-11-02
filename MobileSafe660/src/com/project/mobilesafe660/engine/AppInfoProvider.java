package com.project.mobilesafe660.engine;

import java.util.ArrayList;
import java.util.List;

import com.project.mobilesafe660.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * 应用信息提供者
 * @author 袁星明
 */
public class AppInfoProvider {

	/**获取已安装应用
	 * @return **/
	public static ArrayList<AppInfo> getInstalledApps(Context context) {
		PackageManager packageManager = context.getPackageManager();
		//获取所有已安装的包
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		ArrayList<AppInfo> list = new ArrayList<AppInfo>();
		for (PackageInfo packageInfo : installedPackages) {
			AppInfo info = new AppInfo();
			//包名
			String packageName = packageInfo.packageName;
			//应用信息
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			//应用名称
			String loadLabelName = applicationInfo.loadLabel(packageManager).toString();
			//应用图标
			Drawable loadIcon = applicationInfo.loadIcon(packageManager);
			//封装
			info.name = loadLabelName;
			info.packageName = packageName;
			info.icon = loadIcon;
			//状态机,通过01状态来表示是否具备某些属性和功能
			int flags = applicationInfo.flags;//获取应用标记
			if ((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
				//安装在sdcard
				info.isRom = false;
			} else {
				//安装在手机内存
				info.isRom = true;
			}
			if ((flags&ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				//系统应用
				info.isUser = false;
			} else {
				//用户应用
				info.isUser = true;
			}
			list.add(info);
		}
		return list;
		
	}
}
