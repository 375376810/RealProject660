package com.project.mobilesafe660.activity;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;

import com.project.mobilesafe660.R;

public class SdcardCacheActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sdcard_cache);
		//1.��ȡsdcard�������ݿ�,��ȡ����Ŀ¼
		//2.�жϸ�Ŀ¼�Ƿ��ڱ��ش���
		//3.�������,��ɾ�����ļ���
		//�ݹ�ɾ���ļ����������ļ�
		File file = new File("");
		file.exists();
		File[] listFiles = file.listFiles();
		file.delete();
	}

}
