package com.project.mobilesafe660.receiver;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.service.LocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

/**
 * ��������
 * �����嵥�ļ�:
 *  			<receiver android:name="receiver.SmsReceiver" >
 *           		<intent-filter
 *               		android:priority="2147483647">
 *               		<action android:name="android.provider.Telephony.SMS_RECEIVED" />
 *           		</intent-filter>
 *       		</receiver>
 * ����Ȩ��:
 *  			<uses-permission android:name="android.permission.RECEIVE_SMS"/>
 * @author Ԭ����
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		//����140�ֽڻ�ֶ�������
		for (Object object : objs) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
			String originatingAddress = sms.getOriginatingAddress();
			String messageBody = sms.getMessageBody();
			System.out.println("����:"+originatingAddress+";����:"+messageBody);
			if ("*#alarm#*".equals(messageBody)) {
				//����ϵͳ����
				System.out.println("���ű�������");
				//�����ļ�Ҳ���Է���assets�ļ���,������raw�ļ��к��������Դ�ļ�R����,�ȽϷ���.
				MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
				//�����������,����ϵͳ�����ı�ֵ
				mediaPlayer.setVolume(1f, 1f);
				//����ѭ������
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
				//�ж϶��Ź㲥����.ע��:4.4+�汾��,�޷����ض���
				abortBroadcast();
				//���Բ������ݿ�,ֱ��ɾ�����ݿ���ض�������,������ض���.
			} else if("*#location#*".equals(messageBody)){
				System.out.println("�ֻ���λ");
				//����λ�ü�������
				context.startService(new Intent(context, LocationService.class));
				//�ж϶��Ŵ��ݹ㲥
				abortBroadcast();
			} else if("*#lockscreen#*".equals(messageBody)){
				System.out.println("һ������");
				//TODO
				//�ж϶��Ŵ��ݹ㲥
				abortBroadcast();
			}
			else if("*#wipedata#*".equals(messageBody)){
				System.out.println("�������");
				//TODO
				//�ж϶��Ŵ��ݹ㲥
				abortBroadcast();
			}
		}
		
	}

}
