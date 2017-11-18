package com.project.mobilesafe660.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.project.mobilesafe660.R;

/**
 * ���б�ǩ���ܵ�activity
 * 
 * @author Ԭ����
 * 
 */
public class CacheTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cache_tab);
		
		TabHost tabHost = getTabHost();
		
		//��ʼ��һ����ǩ
		TabSpec tab1 = tabHost.newTabSpec("clean_cache");
		tab1.setIndicator("��������");//ϵͳĬ����ʽ
		tab1.setContent(new Intent(this,CleanCacheActivity.class));//ҳǩ�������ת����������ҳ��
		
		//��ʼ��һ����ǩ
		TabSpec tab2 = tabHost.newTabSpec("sdcard_cache");
		tab2.setIndicator("sdcard����");//ϵͳĬ����ʽ
		tab2.setContent(new Intent(this,SdcardCacheActivity.class));//ҳǩ�������ת����������ҳ��
		
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		
	}
}
