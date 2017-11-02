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
 * ܛ������
 * 
 * @author Ԭ����
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
	private AppInfo mCurrentAppInfo;//��ǰѡ��Ķ���
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		String sdcardSpace = getAvailableSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		String romSpace = getAvailableSpace(Environment.getDataDirectory().getAbsolutePath());
		TextView tvSdcard = (TextView) findViewById(R.id.tv_sdcard_available);
		TextView tvRom = (TextView) findViewById(R.id.tv_rom_available);
		tvSdcard.setText("sdcard����:"+sdcardSpace);
		tvRom.setText("�ڲ��洢����:"+romSpace);
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
						tvHeader.setText("�û�Ӧ��("+ mUserList.size() +")");
					}else {
						tvHeader.setText("ϵͳӦ��("+ mSystemList.size() +")");					
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
	/*չʾС����*/
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
			//��������Ч��
			AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
			animAlpha.setDuration(500);
			//���Ŷ���
			ScaleAnimation animScal = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.5f);
			animScal.setDuration(500);
			mPopupAnimSetSet = new AnimationSet(true);
			mPopupAnimSetSet.addAnimation(animAlpha);
			mPopupAnimSetSet.addAnimation(animScal);
		}
		mPopupWindow.showAsDropDown(view,50,-view.getHeight());			
		mPopupView.startAnimation(mPopupAnimSetSet);
		
	}
	/*��ʼ������*/
	private void initData() {
		llLoading.setVisibility(View.VISIBLE);
		new Thread(){

			public void run() {
				installedAppsList = AppInfoProvider.getInstalledApps(getApplicationContext());
				mUserList = new ArrayList<AppInfo>();
				mSystemList = new ArrayList<AppInfo>();	
				//�����û���ϵͳӦ�÷ֱ������������
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

	/*��ȡ���ÿռ�*/
	private String getAvailableSpace(String path) {
		StatFs statFs = new StatFs(path);
		//��ȡ���ô洢������
		long availableBlocks = statFs.getAvailableBlocks();
		long blockSize = statFs.getBlockSize();
		//���ô洢�ռ�
		long availableSize = availableBlocks * blockSize;
		//���ֽ���ת��Ϊ������Ӧ��λ���ַ���
		return Formatter.formatFileSize(this, availableSize);
	}
	
	/**listview������**/
	class AppInfoAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			//return installedAppsList.size();
			return mUserList.size() + mSystemList.size() +2;
		}
		@Override
		public AppInfo getItem(int position) {
			if (position == 0 || position == mUserList.size() + 1) {
				//������������
				return null;
			}
			if (position<mUserList.size() + 1) {
				return mUserList.get(position-1);//����1��������
			} else {
				return mSystemList.get(position-mUserList.size()-2);//����2��������			
			}
		}
		//��ʾlistviewչʾ�Ĳ�����������
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		//���ݵ�ǰλ��չʾ��ͬ�Ĳ�������
		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == mUserList.size() + 1) {
				//���ر������Ĳ���
				return 0;
			} else {
				//������ͨ����
				return 1;
			}
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//�жϵ�ǰ��������
			int type = getItemViewType(position);
			switch (type) {
			case 0://����������		
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
					headerHolder.tvHeader.setText("�û�Ӧ��("+ mUserList.size() +")");
				} else {
					headerHolder.tvHeader.setText("ϵͳӦ��" + mSystemList.size() +"");
				}
				break;
			case 1://��ͨ����		
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
					holder.tvLocation.setText("�ֻ��ڴ�");
				}else {
					holder.tvLocation.setText("���ô洢��");
				}
				break;
			}
			return convertView;
		}
	}
	
	/**viewholder�������**/
	static class ViewHolder {
		public TextView tvName;
		public TextView tvLocation;
		public ImageView ivIcon;
	}
	
	/**headerHolder�������**/
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

	/*����*/
	private void share() {
		//����ϵͳ���п��Է���app�б�,ѡ��app���з���
		Intent intent = new Intent(Intent.ACTION_SEND);
		//�����ı�
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "�������һ���ܺ��õ�Ӧ��,���ص�ַ:https://play.google.com/store/apps/details?id=" +mCurrentAppInfo.packageName);
		startActivity(intent);
	}
	
	/*����ѡ����Ӧ��*/
	private void launch() {
		PackageManager packageManager = getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(mCurrentAppInfo.packageName);
		if (intent!=null) {
			startActivity(intent);			
		} else {
			ToastUtils.showToast(this, "�Ҳ�������ҳ��");
		}
	}
	
	/*ж��ѡ�������*/
	private void unInstall() {
		if(mCurrentAppInfo.isUser){
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:"+mCurrentAppInfo.packageName));
			startActivityForResult(intent,0);			
		} else {
			ToastUtils.showToast(this, "��ҪRootȨ�޲ſ���ж��ϵͳӦ��");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//ɾ��������ˢ�½���
		initData();
	}
	
}
