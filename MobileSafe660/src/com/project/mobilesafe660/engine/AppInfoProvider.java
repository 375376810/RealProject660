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
 * Ӧ����Ϣ�ṩ��
 * @author Ԭ����
 */
public class AppInfoProvider {

	/**��ȡ�Ѱ�װӦ��
	 * @return **/
	public static ArrayList<AppInfo> getInstalledApps(Context context) {
		PackageManager packageManager = context.getPackageManager();
		//��ȡ�����Ѱ�װ�İ�
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		ArrayList<AppInfo> list = new ArrayList<AppInfo>();
		for (PackageInfo packageInfo : installedPackages) {
			AppInfo info = new AppInfo();
			//����
			String packageName = packageInfo.packageName;
			//Ӧ����Ϣ
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			//Ӧ������
			String loadLabelName = applicationInfo.loadLabel(packageManager).toString();
			//Ӧ��ͼ��
			Drawable loadIcon = applicationInfo.loadIcon(packageManager);
			//��װ
			info.name = loadLabelName;
			info.packageName = packageName;
			info.icon = loadIcon;
			//״̬��,ͨ��01״̬����ʾ�Ƿ�߱�ĳЩ���Ժ͹���
			int flags = applicationInfo.flags;//��ȡӦ�ñ��
			if ((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
				//��װ��sdcard
				info.isRom = false;
			} else {
				//��װ���ֻ��ڴ�
				info.isRom = true;
			}
			if ((flags&ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				//ϵͳӦ��
				info.isUser = false;
			} else {
				//�û�Ӧ��
				info.isUser = true;
			}
			list.add(info);
		}
		return list;
		
	}
}
