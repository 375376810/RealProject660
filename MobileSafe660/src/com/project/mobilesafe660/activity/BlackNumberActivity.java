package com.project.mobilesafe660.activity;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.BlackNumberDao;
import com.project.mobilesafe660.domain.BlackNumberInfo;
import com.project.mobilesafe660.utils.Md5Utils;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.ToastUtils;

/**
 * 黑名单管理
 * 开发流程:
 * 1.布局文件
 * 2.封装黑名单数据库
 * 3.listView填充数据
 * 4.listView优化
 * 5.在子线程加载数据
 * 6.异步加载黑名单数据
 * 7.分页加载(1.使用limit语句查询一页数据2.展示第一页数据3.监听滑动到底部的事件4.到底后加载下一页数据5.判断是否到最后一页,给用户提示)
 * @author 袁星明
 *
 */
public class BlackNumberActivity extends Activity {

	private ListView lvList;
	private BlackNumberDao mDao;
	private ArrayList<BlackNumberInfo> mList;
	private BlackNumberAdapter mAdapter;
	private ProgressBar bpLoading;
	private int mIndex;//记录分页查询的位置
	private boolean isLoading;//标记当前是否正在加载
	//private int mTotalCount;//黑名单总个数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		mDao = BlackNumberDao.getInstence(this);
		lvList = (ListView) findViewById(R.id.lv_black_number);
		//listView滑动监听
		lvList.setOnScrollListener(new OnScrollListener() {
			//滑动状态发生变化
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//滑动空闲
				if (scrollState == SCROLL_STATE_IDLE) {
					//判断是否到底部
					//判断当前屏幕展示的最后一条是否是集合中的最后一个元素,如果是,就到底部了
					int position = lvList.getLastVisiblePosition();
					if (position >= mList.size()-1 && !isLoading) {
						System.out.println("到底了");
						//判断是否到最后一页
						//获取数据库中数据的总个数,和当前集合中的个数进行对比
						int totalCount = mDao.getTotalCount();
						if (mList.size()<totalCount) {
							//重新加载,下一页数据
							initData();							
						} else {
							ToastUtils.showToast(getApplicationContext(), "没有更多数据了");
						}
						
					}
				}
			}
			//滑动过程中回调
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
		bpLoading = (ProgressBar) findViewById(R.id.pb_loading);
		initData();
	}
	
	private void initData() {
		isLoading = true;//表示正在加载
		bpLoading.setVisibility(View.VISIBLE);
		new Thread(){

			@Override
			public void run() {
				//mList = mDao.findAll();
				//查询1页数据
				if (mList == null) {
					mList = mDao.findPart(mIndex);					
				} else {
					//在原有的集合中追加20条数据
					ArrayList<BlackNumberInfo> partList = mDao.findPart(mIndex);
					mList.addAll(partList);
				}
				//运行在主线程
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mAdapter == null) {
							mAdapter = new BlackNumberAdapter();
							//给listView设置数据,会导致数据从第0个开始展示
							lvList.setAdapter(mAdapter);								
						} else {
							//基于当前数据刷新Adapter,实现当前在哪个item位置,就展现在哪个item位置
							mAdapter.notifyDataSetChanged();							
						}
						bpLoading.setVisibility(View.GONE);
						//加载结束
						isLoading = false;
						//mIndex += 20;
						//更新分页位置.由于分页位置的增长和mList集合的size属性相同,直接赋值 
						mIndex = mList.size();
					}
				});
			}
		}.start();
	}
	
	public class BlackNumberAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public BlackNumberInfo getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			//listview重用,使用convertView,保证view不会创建多次而造成内存溢出.
			if (convertView==null) {
				view = View.inflate(getApplicationContext(), R.layout.list_item_black_number, null);
				
				TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
				TextView tvMode = (TextView) view.findViewById(R.id.tv_mode);
				ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
				
				//用viewHolder保存findviewbyid找到的值
				holder = new ViewHolder();
				holder.ivDelete = ivDelete;
				holder.tvMode = tvMode;
				holder.tvNumber = tvNumber;
				
				//将holder对象和view绑定在一起.
				view.setTag(holder);
				System.out.println("初始化view");
			} else {
				view = convertView;
				//从convertView中取出holder对象
				holder = (ViewHolder) view.getTag();
				System.out.println("重用view");
			}
			
			final BlackNumberInfo info = getItem(position);
			holder.tvNumber.setText(info.number);
			switch (info.mode) {
			case 1:
				holder.tvMode.setText("拦截电话");
				break;
			case 2:
				holder.tvMode.setText("拦截短信");
				break;
			case 3:
				holder.tvMode.setText("拦截全部");
				break;
			}
			//给删除按钮添加点击事件
			holder.ivDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//1.从数据库删除2.从集合删除3.刷新listView
					mDao.delete(info.number);
					mList.remove(info);
					mAdapter.notifyDataSetChanged();
				}
			});
			
			return view;
		}
		
	}
	
	static class ViewHolder {
		TextView tvNumber;
		TextView tvMode ;
		ImageView ivDelete ;
	}
	
	/*添加黑名单*/
	public void addBlackNumber(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_add_black_number, null);
		dialog.setView(view, 0, 0, 0, 0);//将view布局对象设置给AlertDialog,并使布局padding上下左右为0
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etBlackNumber = (EditText) view.findViewById(R.id.et_black_number);
		final RadioGroup rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		//确定按钮点击事件
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = etBlackNumber.getText().toString().trim();
				if (!TextUtils.isEmpty(phone)) {
					//获取当前被勾选的radiobutton的id
					int id = rgGroup.getCheckedRadioButtonId();
					int mode = 1;
					switch (id) {
					case R.id.rb_phone:
						mode = 1;
						break;
					case R.id.rb_sms:
						mode = 2;
						break;
					case R.id.rb_all:
						mode = 3;
						break;
					}
					//添加到数据库
					mDao.add(phone, mode);
					//给集合添加元素
					BlackNumberInfo addInfo = new BlackNumberInfo();
					addInfo.number = phone;
					addInfo.mode = mode;
					mList.add(0, addInfo);
					//刷新界面
					mAdapter.notifyDataSetChanged();
					dialog.dismiss();
				}else {
					ToastUtils.showToast(getApplicationContext(), "输入内容不能为空");
				}
			}
		});
		//取消按钮点击事件
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	
	}

}
