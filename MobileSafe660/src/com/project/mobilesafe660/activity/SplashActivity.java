package com.project.mobilesafe660.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.StreamUtils;

/***
 * @author Ԭ����
 *  ����ҳ��
 */
public class SplashActivity extends Activity {

	private static final int CODE_UPDATE_DIALOG = 1;
	private static final int CODE_ENTER_HOME = 2;
	private static final int CODE_URL_ERROR = 3;
	private static final int CODE_NETWORK_ERROR = 4;
	private static final int CODE_JSON_ERROR = 5;
	
	private TextView tvName;
	private TextView tvProgress;
	private String mVersionName;//�汾��
	private int mVersionCode;
	private String mDes;//�汾����
	private String mUrl;//��������
	private RelativeLayout rlRoot;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(getApplicationContext(), "���ݽ����쳣", Toast.LENGTH_SHORT).show();
				break;
			case CODE_NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "�����쳣", Toast.LENGTH_SHORT).show();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(getApplicationContext(), "URL�����쳣", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvName.setText("�汾��:" + getVersionName());
		tvProgress = (TextView)findViewById(R.id.tv_progress);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		//SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean autoUpdate = PrefUtils.getBoolean("auto_update", true,this);
		if (autoUpdate) {
			//��Ҫ���²ż�����������
			checkVersionName();			
		} else {
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		//�����������潥����ɫЧ��
		AlphaAnimation animation = new AlphaAnimation(0.2f,1);
		animation.setDuration(2000);
		rlRoot.startAnimation(animation);
		//����������db
		copyDb("address.db");
	}

	/** ���������汾��Ϣ **/
	private void checkVersionName() {
		new Thread() {
			@Override
			public void run() {
				Message message = Message.obtain();
				long timeStart = System.currentTimeMillis();
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL("http://10.0.2.2:8080/update.json").openConnection();
					connection.setConnectTimeout(2000);
					connection.setReadTimeout(2000);
					connection.setRequestMethod("GET");
					connection.connect();
					int responseCode = connection.getResponseCode();
					if (responseCode == 200) {
						InputStream in = connection.getInputStream();
						String result = StreamUtils.streamToString(in);
						//System.out.println("result"+result);
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDes = jo.getString("des");
						mUrl = jo.getString("url");
						//�жϰ汾
						if (getVersionCode() < mVersionCode) {
							//System.out.println("�и���");
							message.what = CODE_UPDATE_DIALOG;
						} else {
							message.what = CODE_ENTER_HOME;
							//System.out.println("�޸���");
						}
					}
				} catch (MalformedURLException e) {
					//url�쳣
					e.printStackTrace();
					message.what = CODE_URL_ERROR;
					enterHome();
				} catch (IOException e) {
					//�����쳣
					e.printStackTrace();
					message.what = CODE_NETWORK_ERROR;
					enterHome();
				} catch (JSONException e) {
					//json�����쳣
					e.printStackTrace();
					message.what = CODE_JSON_ERROR;
					enterHome();
				} finally {
					//ǿ�Ƶȴ�һ��ʱ��,��չ�ֹ�˾logo
					try {
						long timeEnd = System.currentTimeMillis();
						long timeUsed = timeEnd - timeStart;
						if (timeUsed < 2000) {
							Thread.sleep(2000-timeUsed);							
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (connection!=null) {
						connection.disconnect();
					}
					mHandler.sendMessage(message);
				}

			};
		}.start();
	}

	/** �������� **/
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("�����°汾:"+ mVersionName);
		builder.setMessage(mDes);
		//builder.setCancelable(false);//����ȡ��
		builder.setPositiveButton("��������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downloadApk();
			}
		});
		builder.setNegativeButton("�´���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		//�û��㷵�ؼ�ʱֱ�ӽ�����ҳ��,������
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		builder.show();
	}

	/** �ӷ��������ظ��°�**/
	protected void downloadApk() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			tvProgress.setVisibility(View.VISIBLE);
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MobileSafe.apk";
			//Xutils
			HttpUtils xUtils = new HttpUtils();
			xUtils.download(mUrl, path, new RequestCallBack<File>() {
				
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					//���ؽ���
					int percent = (int) (current * 100 / total);
					//System.out.println("���ؽ���:" + percent +"%");
					tvProgress.setText("���ؽ���:" + percent +"%");
				};
				
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					//���سɹ�
					String path = responseInfo.result.getAbsolutePath();
					System.out.println("���سɹ�:" +path);
					Toast.makeText(getApplicationContext(), "�ɹ������ļ�:"+path, Toast.LENGTH_SHORT).show();
					//���سɹ�����ת��ϵͳ��װ����
					Intent intent = new Intent();
					intent.setAction(intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}
				
				@Override
				public void onFailure(HttpException err, String msg) {
					//����ʧ��
					err.printStackTrace();
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			Toast.makeText(getApplicationContext(), "û���ҵ�sdcard", Toast.LENGTH_SHORT).show();
		}
	}

	/** ��ȡ�汾�� **/
	private String getVersionName() {
		// ��������
		PackageManager pm = this.getPackageManager();
		try {
			// ���ݰ�����ȡ�����Ϣ
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/** ��ȡ�汾�� **/
	private int getVersionCode() {
		PackageManager pm = this.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(),0);
			int versionCode = info.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/** ��ת����ҳ�� **/
	private void enterHome() {
		startActivity(new Intent(this,HomeActivity.class));
		finish();
	}

	/** �û����ȡ��ʱ���� **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
	}

	/** ��address.db�ļ������� data/data/Ŀ¼ **/
	private void copyDb(String DbName) {
		InputStream in = null;
		FileOutputStream out = null;
		//data/data����
		File filesDir = getFilesDir();
		File targetFile = new File(filesDir,DbName);
		//���ж��ļ��Ƿ��Ѵ���
		if (targetFile.exists()) {
			System.out.println("���ݿ�"+DbName+"�Ѿ�����,���追��");
			return;
		}
		try {
			AssetManager assets = getAssets();
			in = assets.open(DbName);
			out = new FileOutputStream(targetFile);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer))!=-1) {
				out.write(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("�ɹ���"+DbName+"�ļ�������data/dataĿ¼");
	}
	
}
