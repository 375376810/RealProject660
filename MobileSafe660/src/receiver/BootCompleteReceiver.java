package receiver;

import com.project.mobilesafe660.utils.PrefUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 *  开启手机重启的广播接收者,检测sim卡是否有变化
 *  需要权限:<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 *  配置清单文件: 
 *  <receiver
 * 		android:name="receiver.BootCompleteReceiver" > <intent-filter> <action
 * 		android:name="android.intent.action.BOOT_COMPLETED" /> </intent-filter>
 * 	</receiver>
 * 
 * @author 袁星明
 */
public class BootCompleteReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String savedSim = PrefUtils.getString("bind_sim", null, context);
		if (!TextUtils.isEmpty(savedSim)) {
			//获取当前sim卡和本地文件中的sim卡对比
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			//当前sim卡
			String curentSim = tm.getSimSerialNumber();
			if (!curentSim.equals(savedSim)) {
				System.out.println("sim卡已经变化,发送报警短信");
			} 
		}
	}
}
