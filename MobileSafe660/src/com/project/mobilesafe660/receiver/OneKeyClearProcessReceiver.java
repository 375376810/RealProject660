package com.project.mobilesafe660.receiver;

import com.project.mobilesafe660.engine.ProcessInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * һ��������̹㲥������
 * @author Ԭ����
 */
public class OneKeyClearProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ProcessInfoProvider.killAll(context);
	}

}
