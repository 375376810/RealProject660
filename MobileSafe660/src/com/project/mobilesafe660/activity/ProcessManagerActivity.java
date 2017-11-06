package com.project.mobilesafe660.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.activity.AppManagerActivity.HeaderHolder;
import com.project.mobilesafe660.domain.ProcessInfo;
import com.project.mobilesafe660.engine.ProcessInfoProvider;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.ToastUtils;

/**
 * 进程管理
 * @author 袁星明
 * item选中效果处理
 * 1.禁用checkBox原生点击效果
 * 2.ProcessInfo新增属性isChecked,标记是否选中
 * 3.getView中根据isChecked来更新checkbox
 * 4.item点击事件,将isChecked置为相反值,同时更新checkbox
 */
public class ProcessManagerActivity extends Activity {

	private int runningProcessNum;
	private TextView tvRunningNum;
	private long availableMemory;
	private long totalMemory;
	private TextView tvMemoInfo;
	private ListView lvList;
	private TextView tvHeader;
	private ProcessInfoAdapter mAdapter;
	private LinearLayout llLoading;
	
	private ArrayList<ProcessInfo> mUserList;//所有已安装的用户进程集合
	private ArrayList<ProcessInfo> mSystemList;//所有已安装的系统进程集合
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);
		runningProcessNum = ProcessInfoProvider.getRunningProcessNum(this);
		tvRunningNum = (TextView) findViewById(R.id.tv_running_num);
		tvMemoInfo = (TextView) findViewById(R.id.tv_memo_info);
		lvList = (ListView) findViewById(R.id.lv_list);
		llLoading = (LinearLayout) findViewById(R.id.ll_loading);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		//tvRunningNum.setText("运行中的进程:"+runningProcessNum+"个");
		tvRunningNum.setText(String.format("运行中的进程%d个",runningProcessNum));
		availableMemory = ProcessInfoProvider.getAvailableMemory(this);
		totalMemory = ProcessInfoProvider.getTotalMemory(this);
		tvMemoInfo.setText(String.format("剩余/总内存:%s/%s", Formatter.formatFileSize(this, availableMemory),Formatter.formatFileSize(this, totalMemory)));
		
		lvList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mUserList!=null && mSystemList!=null) {
					if (firstVisibleItem<=mUserList.size()) {
						tvHeader.setText("用户进程("+ mUserList.size() +")");
					}else {
						tvHeader.setText("系统进程("+ mSystemList.size() +")");					
					}					
				}
			}
		});
		
		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProcessInfo info = mAdapter.getItem(position);
				if (info!=null) {
					if (info.packageName.equals(getPackageName())) {
						//过滤掉手机卫士
						return;
					}
					info.isChecked = !info.isChecked;
					//mAdapter.notifyDataSetChanged();//全局刷新性能不好
					//局部更新checkbox
					CheckBox cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
					cbCheck.setChecked(info.isChecked);
				}
			}
		});
		
		initData();
	}

	private void initData() {
		llLoading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				ArrayList<ProcessInfo> list = ProcessInfoProvider.getRunningProcesse(getApplicationContext());
				mUserList = new ArrayList<ProcessInfo>();
				mSystemList = new ArrayList<ProcessInfo>();
				for (ProcessInfo processInfo : list) {
					if (processInfo.isUser) {
						mUserList.add(processInfo);
					} else {
						mSystemList.add(processInfo);
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						mAdapter = new ProcessInfoAdapter(); 
						lvList.setAdapter(mAdapter);
						llLoading.setVisibility(View.GONE);
					}
				});
			};
		}.start();
		
	}
	
	/**listview适配器**/
	class ProcessInfoAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			//return installedAppsList.size();
			Boolean showSystemProcess = PrefUtils.getBoolean("show_system_process", true, getApplicationContext());
			if (showSystemProcess) {
				return mUserList.size() + mSystemList.size() +2;				
			} else {
				return mUserList.size() + 1;
			}
		}
		@Override
		public ProcessInfo getItem(int position) {
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
					headerHolder.tvHeader.setText("用户进程("+ mUserList.size() +")");
				} else {
					headerHolder.tvHeader.setText("系统进程" + mSystemList.size() +"");
				}
				break;
			case 1://普通类型		
				ViewHolder holder;
				if (convertView==null) {
					convertView = convertView.inflate(getApplicationContext(), R.layout.list_item_processinfo, null);
					holder = new ViewHolder();
					holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
					holder.tvMemory = (TextView) convertView.findViewById(R.id.tv_memory);
					holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cb_check);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				ProcessInfo info = getItem(position);
				holder.tvName.setText(info.name);
				holder.ivIcon.setImageDrawable(info.icon);
				holder.tvMemory.setText(Formatter.formatFileSize(getApplicationContext(), info.memory));
				if (info.packageName.equals(getPackageName())) {
					//当前应用,不显示checkbox
					holder.cbCheck.setVisibility(View.INVISIBLE);
				} else {
					holder.cbCheck.setVisibility(View.VISIBLE);
					holder.cbCheck.setChecked(info.isChecked);
				}
				break;
			}
			return convertView;
		}
	}
	
	static class ViewHolder {
		public TextView tvName;
		public TextView tvMemory;
		public ImageView ivIcon;
		public CheckBox cbCheck;
	}
	
	//全选
	public void selectAll(View view){
		for (ProcessInfo info : mUserList) {
			if (info.packageName.equals(getPackageName())) {
				//过滤掉手机卫士
				continue;
			}
			info.isChecked = true;
		}
		for (ProcessInfo info : mSystemList) {
			info.isChecked = true;
		}
		mAdapter.notifyDataSetChanged();
	}
	//反选
	public void reverseSelect(View view){
		for (ProcessInfo info : mUserList) {
			if (info.packageName.equals(getPackageName())) {
				//过滤掉手机卫士
				continue;
			}
			info.isChecked = !info.isChecked;
		}
		for (ProcessInfo info : mSystemList) {
			info.isChecked = !info.isChecked;
		}
		mAdapter.notifyDataSetChanged();
		
		
	}
	//一键清理
	public void killAll(View view){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ArrayList<ProcessInfo> killedList = new ArrayList<ProcessInfo>();//被清理的进程集合
		long savedMemory = 0;
		for (ProcessInfo info : mUserList) {
			if (info.isChecked) {
				am.killBackgroundProcesses(info.packageName);
				//mUserList.remove(info);集合在遍历的时候不能修改元素
				killedList.add(info);
			}
		}
		for (ProcessInfo info : mSystemList) {
			if (info.isChecked) {
				am.killBackgroundProcesses(info.packageName);
				//mSystemList.remove(info);集合在遍历的时候不能修改元素
				killedList.add(info);
			}
		}
		for (ProcessInfo info : killedList) {
			if (info.isUser) {
				mUserList.remove(info);
			} else {
				mSystemList.remove(info);
			}
			savedMemory += info.memory;
		}
		mAdapter.notifyDataSetChanged();
		ToastUtils.showToast(this, String.format("帮您杀死了%d个进程,共节省了%s空间",killedList.size(),Formatter.formatFileSize(this, savedMemory)));
		//更新文本信息
		runningProcessNum += killedList.size();
		availableMemory += savedMemory;
		tvRunningNum.setText(String.format("运行中的进程%d个",runningProcessNum));
		tvMemoInfo.setText(String.format("剩余/总内存:%s/%s", Formatter.formatFileSize(this, availableMemory),Formatter.formatFileSize(this, totalMemory)));
	}
	//设置
	public void setting(View view){
		startActivityForResult(new Intent(this,ProcessSettingActivity.class),0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//从设置页面返回后的回调
		mAdapter.notifyDataSetChanged();//重新回走一次getCount方法,根据设置信息返回相应item数量
	}
	
}
