package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;

import com.project.mobilesafe660.R;

/**
 * ����ͳ��
 * @author Ԭ����
 *
 */
public class TrafficStatsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_stats);
		
		//wifi + 3g
		String totalRxBytes ="����������:" + TrafficStats.getTotalRxBytes();
		String totalTxByteString = "���ϴ�����" +TrafficStats.getTotalTxBytes();
		//�ƶ�����
		String mobileRxBytes = "�ƶ���������" + TrafficStats.getMobileRxBytes();
		String mobileTxBytes = "�ƶ��ϴ�����" + TrafficStats.getMobileTxBytes();
		
	}

}
