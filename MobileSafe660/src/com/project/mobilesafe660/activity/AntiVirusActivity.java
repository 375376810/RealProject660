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
	
	//��������
	private ArrayList<ScanInfo> mVirusList = new ArrayList<AntiVirusActivity.ScanInfo>();
	
	private static final int STATE_UPDATE_STATUS = 1;//����ɨ��״̬
	private static final int STATE_SCAN_FINISH = 2;//ɨ�����
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case STATE_UPDATE_STATUS:
				ScanInfo info = (ScanInfo) msg.obj;
				tvStatus.setText("����ɨ��"+info.name);
				
				//��̬������llContainer���TextView
				TextView view = new TextView(getApplicationContext());
				
				//�ж��Ƿ�Ϊ����
				if (!info.isVirus) {
					view.setText("ɨ�谲ȫ:"+ info.name);
					view.setTextColor(Color.BLACK);					
				} else {
					view.setText("���ֲ���:"+ info.name);
					view.setTextColor(Color.RED);										
				}
				
				//llContainer.addView(view);
				llContainer.addView(view,0);//��view����ڵ�һ��λ��
				break;
			case STATE_SCAN_FINISH:
				tvStatus.setText("ɨ�����");
				//ֹͣ��ǰ����
				ivScanning.clearAnimation();
				
				if (!mVirusList.isEmpty()) {
					showAlertDialog();
				} else {
					ToastUtils.showToast(getApplicationContext(), "�����ֻ��ܰ�ȫ,�����ʹ��");
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
				//չʾ"���ڳ�ʼ��16������"������
				SystemClock.sleep(1000);
				PackageManager pm = getPackageManager();
				//��ȡ�Ѱ�װapp,ĳЩӦ��ж�غ�����data/dataĿ¼����,ҲҪ���س���.
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

					//��ȡ��ǰapk�ļ��ĵ�ַ(data/apk)
					String apkPath = packageInfo.applicationInfo.sourceDir;
					//���㵱ǰapk��md5ֵ
					String md5 = Md5Utils.encodeFile(apkPath);
					
					//�жϵ�ǰapk�Ƿ�Ϊ����
					if (AntiVirusDao.isVirus(md5)) {
						//System.out.println("���ֲ���!");
						info.isVirus = true;
						
						mVirusList.add(info);
					} else {
						//System.out.println("ɨ�谲ȫ");
						info.isVirus = false;
					}
					
					progress++;
					bpProgress.setProgress(progress);
					//����ɨ��״̬
					Message msg = Message.obtain();
					msg.what = STATE_UPDATE_STATUS;
					msg.obj = info;
					mHandler.sendMessage(msg);
					SystemClock.sleep(50+random.nextInt(50));//Ϊ��������ʵ��ʹɨ��ʱ�䲻����
				}
				//ɨ�����
				mHandler.sendEmptyMessage(STATE_SCAN_FINISH);
				
			};
		}.start();
	}
	
	/** ���ֲ�����ľ��浯�� **/
	protected void showAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���ؾ���");
		builder.setMessage("����" + mVirusList.size() + "������,������������!!!");
		builder.setCancelable(false);
		builder.setPositiveButton("��������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ж�ز���
				for (ScanInfo info : mVirusList) {
					Intent intent = new Intent(Intent.ACTION_DELETE);
					intent.setData(Uri.parse("package:" + info.packageName));
					startActivity(intent);
				}
			}
		});
		builder.setNegativeButton("�Ժ���˵", null);
		builder.show();
	}

	/** ��ɨ����Ӧ�õķ�װ **/
	class ScanInfo {
		public String name;
		public boolean isVirus;
		public String packageName;
	}

}
