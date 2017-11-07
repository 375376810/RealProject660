package com.project.mobilesafe660.receiver;

import com.project.mobilesafe660.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * 窗口小部件组件
 * 
 * @author 袁星明 流程: 1.写类继承AppWidgetProvider 2.在清单文件中注册 3.配置文件 4.widget布局文件
 */
public class MyWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		System.out.println("widget接收者onReceive.................");
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		System.out.println("小部件第一次添加时调用onEnabled");
		// 启动定时更新的服务
		context.startService(new Intent(context, UpdateWidgetService.class));
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		System.out.println("新增widget或间隔一定时间更新onUpdate");
		// 启动定时更新的服务
		context.startService(new Intent(context, UpdateWidgetService.class));
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		System.out.println("当widget被移除时onDeleted");
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		System.out.println("最后一个widget被移除时onDisabled");
		//停止定时更新服务
		context.stopService(new Intent(context,UpdateWidgetService.class));
	}

}
