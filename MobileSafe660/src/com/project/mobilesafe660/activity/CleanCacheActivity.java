package com.project.mobilesafe660.activity;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.content.pm.IPackageDataObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.mobilesafe660.R;

/**
 * 缓存清理
 * @author 袁星明
 */
public class CleanCacheActivity extends Activity {

	private ProgressBar pbProgress;
	private TextView tvStatus;
	private LinearLayout llContainer;
	private PackageManager mPm;
	
	private static final int STATE_UPDATE_STATUS = 1;
	private static final int STATE_SCAN_FINISH = 2;
	private static final int STATE_FIND_CACHE = 3;
	

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case STATE_UPDATE_STATUS:
				String name = (String) msg.obj;
				tvStatus.setText("正在扫描:" + name);
				break;
			case STATE_SCAN_FINISH:
				tvStatus.setText("扫描完毕");
				break;
			case STATE_FIND_CACHE:
				final CacheInfo info = (CacheInfo) msg.obj;
				View view = View.inflate(getApplicationContext(), R.layout.list_item_cacheinfo, null);
				TextView tvName = (TextView) view.findViewById(R.id.tv_name);
				ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
				TextView tvCacheSize = (TextView) view.findViewById(R.id.tv_cache_size);
				ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
				
				tvName.setText(info.name);
				ivIcon.setImageDrawable(info.icon);
				tvCacheSize.setText(Formatter.formatFileSize(getApplicationContext(), info.cacheSize));
				
				ivDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//清理单个缓存,需要跳到系统页面清理
						Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
						intent.setData(Uri.parse("package:" + info.packageName));
						startActivity(intent);
					}
				});
				
				llContainer.addView(view,0);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		
		pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
		tvStatus = (TextView) findViewById(R.id.tv_status);
		llContainer = (LinearLayout) findViewById(R.id.ll_container);
		
		mPm = getPackageManager();
		new Thread(){
			public void run() {
				List<PackageInfo> installedPackages = mPm.getInstalledPackages(0);
				pbProgress.setMax(installedPackages.size());
				int progress = 0;
				for (PackageInfo packageInfo : installedPackages) {
					String packageName = packageInfo.packageName;
					try {
						Method method = mPm.getClass().getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
						method.invoke(mPm, packageName,new MyObserver());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					String name = packageInfo.applicationInfo.loadLabel(mPm).toString();
					progress ++;
					pbProgress.setProgress(progress);
					Message msg = Message.obtain();
					msg.what = STATE_UPDATE_STATUS;
					msg.obj = name;
					mHandler.sendMessage(msg);
					SystemClock.sleep(33);
				}
				mHandler.sendEmptyMessage(STATE_SCAN_FINISH);
			};
		}.start();
		
	}
	
	/*立即清理*/
	public void clearCache(View view) {
		//freeStorageAndNotify 向系统所要足够大的空间,那么系统就会删除所有缓存文件来凑空间,从而达到间接清理缓存的目的.
		//LRU least recently used最近最少使用
		try {
			Method method = mPm.getClass().getMethod("freeStorageAndNotify", long.class,IPackageDataObserver.class);
			method.invoke(mPm, Long.MAX_VALUE,new IPackageDataObserver.Stub() {
				@Override
				public void onRemoveCompleted(String packageName, boolean succeeded)
						throws RemoteException {
					System.out.println("succeeded:"+succeeded);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	class MyObserver extends IPackageStatsObserver.Stub {

		//此方法在子线程运行
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cacheSize = pStats.cacheSize;
			if (cacheSize>0) {
				//有缓存
				CacheInfo info = new CacheInfo();
				info.packageName = pStats.packageName;
				info.cacheSize = cacheSize;
				//通过包名获取应用信息
				ApplicationInfo applicationInfo;
				try {
					applicationInfo = mPm.getApplicationInfo(info.packageName, 0);
					info.name = applicationInfo.loadLabel(mPm).toString();
					info.icon = applicationInfo.loadIcon(mPm);
					
					Message msg = Message.obtain();
					msg.obj = info;
					msg.what = STATE_FIND_CACHE;
					mHandler.sendMessage(msg);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	class CacheInfo{
		public String name;
		public String packageName;
		public Drawable icon;
		public long cacheSize;
	}

}
