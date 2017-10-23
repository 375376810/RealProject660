package com.project.mobilesafe660.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.BlackNumberDao;
import com.project.mobilesafe660.domain.BlackNumberInfo;
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
	private int mTotalCount;//黑名单总个数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		mDao = BlackNumberDao.getInstence(this);
		mTotalCount = mDao.getTotalCount();
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
						if (mList.size()<mTotalCount) {
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
			
			BlackNumberInfo info = getItem(position);
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
			default:
				break;
			}
			
			return view;
		}
		
	}
	
	static class ViewHolder {
		TextView tvNumber;
		TextView tvMode ;
		ImageView ivDelete ;
	}

}
