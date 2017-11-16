package com.project.mobilesafe660.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.AntiVirusDao;
import com.project.mobilesafe660.utils.Md5Utils;
import com.project.mobilesafe660.utils.ToastUtils;

public class AntiVirusActivity extends Activity {

	private ImageView ivScanning;
	private TextView tvStatus;
	private ProgressBar bpProgress;
	private LinearLayout llContainer;
	
	//病毒集合
	private ArrayList<ScanInfo> mVirusList = new ArrayList<AntiVirusActivity.ScanInfo>();
	
	private static final int STATE_UPDATE_STATUS = 1;//更新扫描状态
	private static final int STATE_SCAN_FINISH = 2;//扫描结束
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case STATE_UPDATE_STATUS:
				ScanInfo info = (ScanInfo) msg.obj;
				tvStatus.setText("正在扫描"+info.name);
				
				//动态给容器llContainer添加TextView
				TextView view = new TextView(getApplicationContext());
				
				//判断是否为病毒
				if (!info.isVirus) {
					view.setText("扫描安全:"+ info.name);
					view.setTextColor(Color.BLACK);					
				} else {
					view.setText("发现病毒:"+ info.name);
					view.setTextColor(Color.RED);										
				}
				
				//llContainer.addView(view);
				llContainer.addView(view,0);//将view添加在第一个位置
				break;
			case STATE_SCAN_FINISH:
				tvStatus.setText("扫描完毕");
				//停止当前动画
				ivScanning.clearAnimation();
				
				if (!mVirusList.isEmpty()) {
					showAlertDialog();
				} else {
					ToastUtils.showToast(getApplicationContext(), "您的手机很安全,请放心使用");
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		
		ivScanning = (ImageView) findViewById(R.id.iv_scanning);
		tvStatus = (TextView) findViewById(R.id.tv_status);
		bpProgress = (ProgressBar) findViewById(R.id.pb_progress);
		llContainer = (LinearLayout) findViewById(R.id.ll_container);
		
		RotateAnimation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(2000);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setInterpolator(new LinearInterpolator());
		ivScanning.startAnimation(anim);
		
		new Thread(){
			public void run() {
//				for (int i = 0; i < 100; i++) {
//					bpProgress.setProgress(i);
//					SystemClock.sleep(100);
//				}
				//展示"正在初始化16核引擎"的文字
				SystemClock.sleep(1000);
				PackageManager pm = getPackageManager();
				//获取已安装app,某些应用卸载后会残留data/data目录数据,也要加载出来.
				List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
				bpProgress.setMax(installedPackages.size());
				int progress = 0;
				Random random = new Random();
				for (PackageInfo packageInfo : installedPackages) {
					ScanInfo info = new ScanInfo();
					
					String packageName =packageInfo.packageName;
					String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
					
					info.packageName = packageName;
					info.name = appName;

					//获取当前apk文件的地址(data/apk)
					String apkPath = packageInfo.applicationInfo.sourceDir;
					//计算当前apk的md5值
					String md5 = Md5Utils.encodeFile(apkPath);
					
					//判断当前apk是否为病毒
					if (AntiVirusDao.isVirus(md5)) {
						//System.out.println("发现病毒!");
						info.isVirus = true;
						
						mVirusList.add(info);
					} else {
						//System.out.println("扫描安全");
						info.isVirus = false;
					}
					
					progress++;
					bpProgress.setProgress(progress);
					//更新扫描状态
					Message msg = Message.obtain();
					msg.what = STATE_UPDATE_STATUS;
					msg.obj = info;
					mHandler.sendMessage(msg);
					SystemClock.sleep(50+random.nextInt(50));//为了增加真实性使扫描时间不均匀
				}
				//扫描完毕
				mHandler.sendEmptyMessage(STATE_SCAN_FINISH);
				
			};
		}.start();
	}
	
	/** 发现病毒后的警告弹窗 **/
	protected void showAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("严重警告");
		builder.setMessage("发现" + mVirusList.size() + "个病毒,建议立即处理!!!");
		builder.setCancelable(false);
		builder.setPositiveButton("立即处理", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//卸载病毒
				for (ScanInfo info : mVirusList) {
					Intent intent = new Intent(Intent.ACTION_DELETE);
					intent.setData(Uri.parse("package:" + info.packageName));
					startActivity(intent);
				}
			}
		});
		builder.setNegativeButton("以后再说", null);
		builder.show();
	}

	/** 对扫描后的应用的封装 **/
	class ScanInfo {
		public String name;
		public boolean isVirus;
		public String packageName;
	}

}
