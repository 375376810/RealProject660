package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;

import com.project.mobilesafe660.R;

/**
 * 流量统计
 * @author 袁星明
 *
 */
public class TrafficStatsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_stats);
		
		//wifi + 3g
		String totalRxBytes ="总下载流量:" + TrafficStats.getTotalRxBytes();
		String totalTxByteString = "总上传流量" +TrafficStats.getTotalTxBytes();
		//移动网络
		String mobileRxBytes = "移动下载流量" + TrafficStats.getMobileRxBytes();
		String mobileTxBytes = "移动上传流量" + TrafficStats.getMobileTxBytes();
		
	}

}
