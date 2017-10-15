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
 * @author 袁星明
 *  闪屏页面
 */
public class SplashActivity extends Activity {

	private static final int CODE_UPDATE_DIALOG = 1;
	private static final int CODE_ENTER_HOME = 2;
	private static final int CODE_URL_ERROR = 3;
	private static final int CODE_NETWORK_ERROR = 4;
	private static final int CODE_JSON_ERROR = 5;
	
	private TextView tvName;
	private TextView tvProgress;
	private String mVersionName;//版本名
	private int mVersionCode;
	private String mDes;//版本描述
	private String mUrl;//下载链接
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
				Toast.makeText(getApplicationContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
				break;
			case CODE_NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(getApplicationContext(), "URL解析异常", Toast.LENGTH_SHORT).show();
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
		tvName.setText("版本名:" + getVersionName());
		tvProgress = (TextView)findViewById(R.id.tv_progress);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		//SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean autoUpdate = PrefUtils.getBoolean("auto_update", true,this);
		if (autoUpdate) {
			//需要更新才检查服务器更新
			checkVersionName();			
		} else {
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		//设置闪屏界面渐进变色效果
		AlphaAnimation animation = new AlphaAnimation(0.2f,1);
		animation.setDuration(2000);
		rlRoot.startAnimation(animation);
		//拷贝归属地db
		copyDb("address.db");
	}

	/** 检查服务器版本信息 **/
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
						//判断版本
						if (getVersionCode() < mVersionCode) {
							//System.out.println("有更新");
							message.what = CODE_UPDATE_DIALOG;
						} else {
							message.what = CODE_ENTER_HOME;
							//System.out.println("无更新");
						}
					}
				} catch (MalformedURLException e) {
					//url异常
					e.printStackTrace();
					message.what = CODE_URL_ERROR;
					enterHome();
				} catch (IOException e) {
					//网络异常
					e.printStackTrace();
					message.what = CODE_NETWORK_ERROR;
					enterHome();
				} catch (JSONException e) {
					//json解析异常
					e.printStackTrace();
					message.what = CODE_JSON_ERROR;
					enterHome();
				} finally {
					//强制等待一段时间,以展现公司logo
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

	/** 升级弹窗 **/
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("发现新版本:"+ mVersionName);
		builder.setMessage(mDes);
		//builder.setCancelable(false);//不可取消
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downloadApk();
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		//用户点返回键时直接进入主页面,不更新
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		builder.show();
	}

	/** 从服务器下载更新包**/
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
					//下载进度
					int percent = (int) (current * 100 / total);
					//System.out.println("下载进度:" + percent +"%");
					tvProgress.setText("下载进度:" + percent +"%");
				};
				
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					//下载成功
					String path = responseInfo.result.getAbsolutePath();
					System.out.println("下载成功:" +path);
					Toast.makeText(getApplicationContext(), "成功下载文件:"+path, Toast.LENGTH_SHORT).show();
					//下载成功后跳转到系统安装界面
					Intent intent = new Intent();
					intent.setAction(intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}
				
				@Override
				public void onFailure(HttpException err, String msg) {
					//下载失败
					err.printStackTrace();
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			Toast.makeText(getApplicationContext(), "没有找到sdcard", Toast.LENGTH_SHORT).show();
		}
	}

	/** 获取版本名 **/
	private String getVersionName() {
		// 包管理器
		PackageManager pm = this.getPackageManager();
		try {
			// 根据包名获取相关信息
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/** 获取版本号 **/
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
	
	/** 跳转到主页面 **/
	private void enterHome() {
		startActivity(new Intent(this,HomeActivity.class));
		finish();
	}

	/** 用户点击取消时调用 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
	}

	/** 将address.db文件拷贝至 data/data/目录 **/
	private void copyDb(String DbName) {
		InputStream in = null;
		FileOutputStream out = null;
		//data/data包名
		File filesDir = getFilesDir();
		File targetFile = new File(filesDir,DbName);
		//先判断文件是否已存在
		if (targetFile.exists()) {
			System.out.println("数据库"+DbName+"已经存在,无需拷贝");
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
		System.out.println("成功将"+DbName+"文件拷贝至data/data目录");
	}
	
}
