package com.project.mobilesafe660.receiver;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.service.LocationService;
import com.project.mobilesafe660.utils.PrefUtils;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

/**
 * 短信拦截
 * 配置清单文件:
 *  			<receiver android:name="receiver.SmsReceiver" >
 *           		<intent-filter
 *               		android:priority="2147483647">
 *               		<action android:name="android.provider.Telephony.SMS_RECEIVED" />
 *           		</intent-filter>
 *       		</receiver>
 * 配置权限:
 *  			<uses-permission android:name="android.permission.RECEIVE_SMS"/>
 * @author 袁星明
 */
public class SmsReceiver extends BroadcastReceiver {

	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName mDeviceAdmin;

	@Override
	public void onReceive(Context context, Intent intent) {
		mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdmin = new ComponentName(context, SuperAdminReceiver.class);
		
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		//超过140字节会分多条发送
		for (Object object : objs) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
			String originatingAddress = sms.getOriginatingAddress();
			String messageBody = sms.getMessageBody();
			System.out.println("号码:"+originatingAddress+";内容:"+messageBody);
			if ("*#alarm#*".equals(messageBody)) {
				//播放系统音乐
				System.out.println("播放报警音乐");
				//音乐文件也可以放在assets文件夹,但放在raw文件夹后可以用资源文件R引入,比较方便.
				MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
				//音量设置最大,基于系统音量的比值
				mediaPlayer.setVolume(1f, 1f);
				//单曲循环播放
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
				//中断短信广播传递.注意:4.4+版本上,无法拦截短信
				abortBroadcast();
				//所以操作数据库,直接删除数据库相关短信内容,间接拦截短信.
			} else if("*#location#*".equals(messageBody)){
				System.out.println("手机定位");
				//启动位置监听服务
				context.startService(new Intent(context, LocationService.class));
				//中断短信传递广播
				abortBroadcast();
			} else if("*#lockscreen#*".equals(messageBody)){
				System.out.println("一键锁屏");
				if (mDevicePolicyManager.isAdminActive(mDeviceAdmin)) {
					//立即锁屏
					mDevicePolicyManager.lockNow();
					//重设密码
					String pass = PrefUtils.getString("lockScreenPassword", "", context);
					mDevicePolicyManager.resetPassword(pass, 0);
					System.out.println("重设密码为:"+pass);
				} else {
					System.out.println("管理员权限未激活,无法锁屏");
				}
				//中断短信传递广播
				abortBroadcast();
			}
			else if("*#wipedata#*".equals(messageBody)){
				System.out.println("清除数据");
				if(mDevicePolicyManager.isAdminActive(mDeviceAdmin)) {
					//清除手机内存和内存卡上的所有数据.
					mDevicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);			
				} else {
					System.out.println("管理员权限未激活,无法擦除数据");
				}
				//中断短信传递广播
				abortBroadcast();
			}
		}
		
	}

}
