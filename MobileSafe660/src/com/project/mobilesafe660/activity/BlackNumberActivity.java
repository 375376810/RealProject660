package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.BlackNumberDao;

public class BlackNumberActivity extends Activity {

	private ListView lvList;
	private BlackNumberDao mDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		mDao = BlackNumberDao.getInstence(this);
		lvList = (ListView) findViewById(R.id.lv_black_number);
		
	}

}
