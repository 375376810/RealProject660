package receiver;

import com.project.mobilesafe660.utils.PrefUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 *  �����ֻ������Ĺ㲥������,���sim���Ƿ��б仯
 *  ��ҪȨ��:<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 *  �����嵥�ļ�: 
 *  <receiver
 * 		android:name="receiver.BootCompleteReceiver" > <intent-filter> <action
 * 		android:name="android.intent.action.BOOT_COMPLETED" /> </intent-filter>
 * 	</receiver>
 * 
 * @author Ԭ����
 */
public class BootCompleteReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String savedSim = PrefUtils.getString("bind_sim", null, context);
		if (!TextUtils.isEmpty(savedSim)) {
			//��ȡ��ǰsim���ͱ����ļ��е�sim���Ա�
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			//��ǰsim��
			String curentSim = tm.getSimSerialNumber();
			if (!curentSim.equals(savedSim)) {
				System.out.println("sim���Ѿ��仯,���ͱ�������");
			} 
		}
	}
}