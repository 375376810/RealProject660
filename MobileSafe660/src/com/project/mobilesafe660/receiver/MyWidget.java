package com.project.mobilesafe660.receiver;

import com.project.mobilesafe660.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * ����С�������
 * 
 * @author Ԭ���� ����: 1.д��̳�AppWidgetProvider 2.���嵥�ļ���ע�� 3.�����ļ� 4.widget�����ļ�
 */
public class MyWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		System.out.println("widget������onReceive.................");
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		System.out.println("С������һ�����ʱ����onEnabled");
		// ������ʱ���µķ���
		context.startService(new Intent(context, UpdateWidgetService.class));
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		System.out.println("����widget����һ��ʱ�����onUpdate");
		// ������ʱ���µķ���
		context.startService(new Intent(context, UpdateWidgetService.class));
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		System.out.println("��widget���Ƴ�ʱonDeleted");
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		System.out.println("���һ��widget���Ƴ�ʱonDisabled");
		//ֹͣ��ʱ���·���
		context.stopService(new Intent(context,UpdateWidgetService.class));
	}

}
