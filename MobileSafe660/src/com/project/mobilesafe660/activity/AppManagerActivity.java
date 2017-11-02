package com.project.mobilesafe660.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.domain.AppInfo;
import com.project.mobilesafe660.engine.AppInfoProvider;
import com.project.mobilesafe660.utils.ToastUtils;

/**
 * 軟件管理
 * 
 * @author 袁星明
 * 
 */
public class AppManagerActivity extends Activity implements OnClickListener {

	private ArrayList<AppInfo> installedAppsList;
	private ListView mLvlist;
	private AppInfoAdapter mAdapter;
	private LinearLayout llLoading;
	public ArrayList<AppInfo> mUserList;
	private ArrayList<AppInfo> mSystemList;
	private TextView tvHeader;
	private PopupWindow mPopupWindow;
	private View mPopupView;
	private AnimationSet mPopupAnimSetSet;
	private AppInfo mCurrentAppInfo;//当前选择的对象
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		String sdcardSpace = getAvailableSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		String romSpace = getAvailableSpace(Environment.getDataDirectory().getAbsolutePath());
		TextView tvSdcard = (TextView) findViewById(R.id.tv_sdcard_available);
		TextView tvRom = (TextView) findViewById(R.id.tv_rom_available);
		tvSdcard.setText("sdcard可用:"+sdcardSpace);
		tvRom.setText("内部存储可用:"+romSpace);
		mLvlist = (ListView) findViewById(R.id.lv_list);
		llLoading = (LinearLayout) findViewById(R.id.ll_loading);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		mLvlist.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mUserList!=null && mSystemList!=null) {
					if (firstVisibleItem<=mUserList.size()) {
						tvHeader.setText("用户应用("+ mUserList.size() +")");
					}else {
						tvHeader.setText("系统应用("+ mSystemList.size() +")");					
					}					
				}
			}
		});
		
		mLvlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo info = mAdapter.getItem(position);
				mCurrentAppInfo = info;
				if (info != null) {
					showPopupWindow(view);					
				}
			}
		});
		
		initData();
	}
	/*展示小弹窗*/
	protected void showPopupWindow(View view) {
		if (mPopupWindow == null) {
			mPopupView = View.inflate(this, R.layout.popup_item_appinfo, null);
			mPopupWindow = new PopupWindow(mPopupView, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
			mPopupWindow.setBackgroundDrawable(new ColorDrawable());
			TextView tvUninstal = (TextView) mPopupView.findViewById(R.id.tv_uninstal);
			TextView tvLaunch = (TextView) mPopupView.findViewById(R.id.tv_launch);
			TextView tvShare = (TextView) mPopupView.findViewById(R.id.tv_share);
			tvUninstal.setOnClickListener(this);
			tvLaunch.setOnClickListener(this);
			tvShare.setOnClickListener(this);
			//弹窗动画效果
			AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
			animAlpha.setDuration(500);
			//缩放动画
			ScaleAnimation animScal = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.5f);
			animScal.setDuration(500);
			mPopupAnimSetSet = new AnimationSet(true);
			mPopupAnimSetSet.addAnimation(animAlpha);
			mPopupAnimSetSet.addAnimation(animScal);
		}
		mPopupWindow.showAsDropDown(view,50,-view.getHeight());			
		mPopupView.startAnimation(mPopupAnimSetSet);
		
	}
	/*初始化数据*/
	private void initData() {
		llLoading.setVisibility(View.VISIBLE);
		new Thread(){

			public void run() {
				installedAppsList = AppInfoProvider.getInstalledApps(getApplicationContext());
				mUserList = new ArrayList<AppInfo>();
				mSystemList = new ArrayList<AppInfo>();	
				//区分用户和系统应用分别放在两个集合
				for (AppInfo info : installedAppsList) {
					if (info.isUser) {
						mUserList.add(info);
					} else {
						mSystemList.add(info);
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						mAdapter = new AppInfoAdapter();
						mLvlist.setAdapter(mAdapter);
						llLoading.setVisibility(View.GONE);
					}
				});
			};
		}.start();
	}

	/*获取可用空间*/
	private String getAvailableSpace(String path) {
		StatFs statFs = new StatFs(path);
		//获取可用存储快数量
		long availableBlocks = statFs.getAvailableBlocks();
		long blockSize = statFs.getBlockSize();
		//可用存储空间
		long availableSize = availableBlocks * blockSize;
		//将字节数转换为带有相应单位的字符串
		return Formatter.formatFileSize(this, availableSize);
	}
	
	/**listview适配器**/
	class AppInfoAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			//return installedAppsList.size();
			return mUserList.size() + mSystemList.size() +2;
		}
		@Override
		public AppInfo getItem(int position) {
			if (position == 0 || position == mUserList.size() + 1) {
				//碰到标题栏了
				return null;
			}
			if (position<mUserList.size() + 1) {
				return mUserList.get(position-1);//减掉1个标题栏
			} else {
				return mSystemList.get(position-mUserList.size()-2);//减掉2个标题栏			
			}
		}
		//表示listview展示的布局种类数量
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		//根据当前位置展示不同的布局类型
		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == mUserList.size() + 1) {
				//返回标题栏的布局
				return 0;
			} else {
				//返回普通类型
				return 1;
			}
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//判断当前布局类型
			int type = getItemViewType(position);
			switch (type) {
			case 0://标题栏类型		
				HeaderHolder headerHolder;
				if (convertView ==null) {
					convertView = convertView.inflate(getApplicationContext(), R.layout.list_item_header, null);
					headerHolder = new HeaderHolder();
					headerHolder.tvHeader = (TextView) convertView.findViewById(R.id.tv_header);
					convertView.setTag(headerHolder);
				} else {
					headerHolder = (HeaderHolder) convertView.getTag();
				}
				if (position == 0) {
					headerHolder.tvHeader.setText("用户应用("+ mUserList.size() +")");
				} else {
					headerHolder.tvHeader.setText("系统应用" + mSystemList.size() +"");
				}
				break;
			case 1://普通类型		
				ViewHolder holder;
				if (convertView==null) {
					convertView = convertView.inflate(getApplicationContext(), R.layout.list_item_appinfo, null);
					holder = new ViewHolder();
					holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
					holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
					holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				AppInfo info = getItem(position);
				holder.tvName.setText(info.name);
				holder.ivIcon.setImageDrawable(info.icon);
				if (info.isRom) {
					holder.tvLocation.setText("手机内存");
				}else {
					holder.tvLocation.setText("外置存储卡");
				}
				break;
			}
			return convertView;
		}
	}
	
	/**viewholder提高性能**/
	static class ViewHolder {
		public TextView tvName;
		public TextView tvLocation;
		public ImageView ivIcon;
	}
	
	/**headerHolder提高性能**/
	static class HeaderHolder {
		public TextView tvHeader;
	}

	@Override
	public void onClick(View v) {
		mPopupWindow.dismiss();
		switch (v.getId()) {
		case R.id.tv_uninstal:
			unInstall();
			break;
		case R.id.tv_launch:
			launch();
			break;
		case R.id.tv_share:
			share();
			break;
		}
		
	}

	/*分享*/
	private void share() {
		//调用系统所有可以分享app列表,选择app进行分享
		Intent intent = new Intent(Intent.ACTION_SEND);
		//分享纯文本
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "给你分享一个很好用的应用,下载地址:https://play.google.com/store/apps/details?id=" +mCurrentAppInfo.packageName);
		startActivity(intent);
	}
	
	/*启动选定的应用*/
	private void launch() {
		PackageManager packageManager = getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(mCurrentAppInfo.packageName);
		if (intent!=null) {
			startActivity(intent);			
		} else {
			ToastUtils.showToast(this, "找不到启动页面");
		}
	}
	
	/*卸载选定的软件*/
	private void unInstall() {
		if(mCurrentAppInfo.isUser){
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:"+mCurrentAppInfo.packageName));
			startActivityForResult(intent,0);			
		} else {
			ToastUtils.showToast(this, "需要Root权限才可以卸载系统应用");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//删除后重新刷新界面
		initData();
	}
	
}
