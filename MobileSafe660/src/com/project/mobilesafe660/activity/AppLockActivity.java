package com.project.mobilesafe660.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.AppLockDao;
import com.project.mobilesafe660.domain.AppInfo;
import com.project.mobilesafe660.engine.AppInfoProvider;

/**
 * ������
 * 
 * @author Ԭ���� ˼·: 1.ʹactivityʵ��onClickListener�ӿ�
 *         2.button.setOnclickListener(this) 3.��дonClick(View view)����
 *         4.��oncLick(View view)��������switch case�ж�
 */
public class AppLockActivity extends Activity implements OnClickListener {

	private Button btnUnlock;
	private Button btnLock;
	private LinearLayout llUnlock;
	private LinearLayout llLock;
	private ArrayList<AppInfo> mUnLockList;
	private ArrayList<AppInfo> mLockList;
	private AppLockAdapter mUnlockAdapter;
	private AppLockAdapter mLockAdapter;
	private ListView lvUnlock;
	private ListView lvLock;
	private AppLockDao mDao;
	private TextView tvUnlock;
	private TextView tvLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock);
		mDao = AppLockDao.getInstence(this);

		btnUnlock = (Button) findViewById(R.id.btn_unlock);
		btnLock = (Button) findViewById(R.id.btn_lock);
		btnUnlock.setOnClickListener(this);
		btnLock.setOnClickListener(this);
		llUnlock = (LinearLayout) findViewById(R.id.ll_unlock);
		llLock = (LinearLayout) findViewById(R.id.ll_lock);
		lvUnlock = (ListView) findViewById(R.id.lv_unlock);
		lvLock = (ListView) findViewById(R.id.lv_lock);
		tvUnlock = (TextView) findViewById(R.id.tv_unlock);
		tvLock = (TextView) findViewById(R.id.tv_lock);

		initData();
	}

	private void initData() {
		new Thread() {
			public void run() {
				ArrayList<AppInfo> list = AppInfoProvider
						.getInstalledApps(getApplicationContext());
				mLockList = new ArrayList<AppInfo>();
				mUnLockList = new ArrayList<AppInfo>();
				for (AppInfo appInfo : list) {
					if (mDao.find(appInfo.packageName)) {
						mLockList.add(appInfo);
					} else {
						mUnLockList.add(appInfo);
					}
				}

				runOnUiThread(new Runnable() {
					public void run() {
						// δ������������
						mUnlockAdapter = new AppLockAdapter(false);
						lvUnlock.setAdapter(mUnlockAdapter);
						// �Ѽ�����������
						mLockAdapter = new AppLockAdapter(true);
						lvLock.setAdapter(mLockAdapter);
					}
				});
			};
		}.start();
	}

	/* �����Ѽ���δ�������� */
	private void updataLockNum() {
		tvUnlock.setText("δ�������:" + mUnLockList.size() + "��");
		tvLock.setText("�Ѽ������:" + mLockList.size() + "��");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_unlock:
			llUnlock.setVisibility(View.VISIBLE);
			llLock.setVisibility(View.GONE);
			btnUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
			btnLock.setBackgroundResource(R.drawable.tab_right_default);
			break;
		case R.id.btn_lock:
			llUnlock.setVisibility(View.GONE);
			llLock.setVisibility(View.VISIBLE);
			btnUnlock.setBackgroundResource(R.drawable.tab_left_default);
			btnLock.setBackgroundResource(R.drawable.tab_right_pressed);
			break;
		}
	}

	/** ������listview������ **/
	class AppLockAdapter extends BaseAdapter {

		boolean isLock;
		private TranslateAnimation animLeft;
		private TranslateAnimation animRight;

		public AppLockAdapter(boolean isLock) {
			this.isLock = isLock;
			//���ƶ���
			animLeft = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			animLeft.setDuration(500);
			// ���ƶ���
			animRight = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			animRight.setDuration(500);
		}

		@Override
		public int getCount() {
			// ÿ�θ��¶����ߴ˷���,������������µļ�������
			updataLockNum();
			if (isLock) {
				return mLockList.size();
			} else {
				return mUnLockList.size();
			}
		}

		@Override
		public AppInfo getItem(int position) {
			if (isLock) {
				return mLockList.get(position);
			} else {
				return mUnLockList.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.list_item_applock, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.ivLock = (ImageView) convertView
						.findViewById(R.id.iv_lock);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final View view = convertView;
			final AppInfo info = getItem(position);
			holder.tvName.setText(info.name);
			holder.ivIcon.setImageDrawable(info.icon);
			holder.ivLock.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isLock) {
						
						animRight.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}
							@Override
							public void onAnimationRepeat(Animation animation) {
							}
							@Override
							public void onAnimationEnd(Animation animation) {
								// 1.���ݿ�����app
								// 2.�Ѽ����������Ӷ���
								// 3.δ�������ϼ��ٶ���
								// 4.ˢ��listview
								mDao.add(info.packageName);
								mLockList.add(info);
								mUnLockList.remove(info);
								mUnlockAdapter.notifyDataSetChanged();
								mLockAdapter.notifyDataSetChanged();
							}
						});
						view.startAnimation(animRight);
					} else {
						
						animLeft.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}
							@Override
							public void onAnimationRepeat(Animation animation) {
							}
							@Override
							public void onAnimationEnd(Animation animation) {
								// 1.�����ݿ�ɾ��
								// 2.�Ѽ�������ɾ������
								// 3.δ�����������Ӷ���
								// 4.ˢ��listview
								mDao.delete(info.packageName);
								mLockList.remove(info);
								mUnLockList.add(info);
								mUnlockAdapter.notifyDataSetChanged();
								mLockAdapter.notifyDataSetChanged();								
							}
						});
						view.startAnimation(animLeft);
					}
				}
			});

			if (isLock) {
				holder.ivLock.setImageResource(R.drawable.unlock);
			} else {
				holder.ivLock.setImageResource(R.drawable.lock);
			}

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvName;
		public ImageView ivLock;
		public ImageView ivIcon;
	}

}
