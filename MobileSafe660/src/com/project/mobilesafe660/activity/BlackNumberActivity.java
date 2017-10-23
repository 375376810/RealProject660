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
 * ����������
 * ��������:
 * 1.�����ļ�
 * 2.��װ���������ݿ�
 * 3.listView�������
 * 4.listView�Ż�
 * 5.�����̼߳�������
 * 6.�첽���غ���������
 * 7.��ҳ����(1.ʹ��limit����ѯһҳ����2.չʾ��һҳ����3.�����������ײ����¼�4.���׺������һҳ����5.�ж��Ƿ����һҳ,���û���ʾ)
 * @author Ԭ����
 *
 */
public class BlackNumberActivity extends Activity {

	private ListView lvList;
	private BlackNumberDao mDao;
	private ArrayList<BlackNumberInfo> mList;
	private BlackNumberAdapter mAdapter;
	private ProgressBar bpLoading;
	private int mIndex;//��¼��ҳ��ѯ��λ��
	private boolean isLoading;//��ǵ�ǰ�Ƿ����ڼ���
	private int mTotalCount;//�������ܸ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		mDao = BlackNumberDao.getInstence(this);
		mTotalCount = mDao.getTotalCount();
		lvList = (ListView) findViewById(R.id.lv_black_number);
		//listView��������
		lvList.setOnScrollListener(new OnScrollListener() {
			//����״̬�����仯
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//��������
				if (scrollState == SCROLL_STATE_IDLE) {
					//�ж��Ƿ񵽵ײ�
					//�жϵ�ǰ��Ļչʾ�����һ���Ƿ��Ǽ����е����һ��Ԫ��,�����,�͵��ײ���
					int position = lvList.getLastVisiblePosition();
					if (position >= mList.size()-1 && !isLoading) {
						System.out.println("������");
						//�ж��Ƿ����һҳ
						//��ȡ���ݿ������ݵ��ܸ���,�͵�ǰ�����еĸ������жԱ�
						if (mList.size()<mTotalCount) {
							//���¼���,��һҳ����
							initData();							
						} else {
							ToastUtils.showToast(getApplicationContext(), "û�и���������");
						}
						
					}
				}
			}
			//���������лص�
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
		bpLoading = (ProgressBar) findViewById(R.id.pb_loading);
		initData();
	}
	
	private void initData() {
		isLoading = true;//��ʾ���ڼ���
		bpLoading.setVisibility(View.VISIBLE);
		new Thread(){

			@Override
			public void run() {
				//mList = mDao.findAll();
				//��ѯ1ҳ����
				if (mList == null) {
					mList = mDao.findPart(mIndex);					
				} else {
					//��ԭ�еļ�����׷��20������
					ArrayList<BlackNumberInfo> partList = mDao.findPart(mIndex);
					mList.addAll(partList);
				}
				//���������߳�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mAdapter == null) {
							mAdapter = new BlackNumberAdapter();
							//��listView��������,�ᵼ�����ݴӵ�0����ʼչʾ
							lvList.setAdapter(mAdapter);								
						} else {
							//���ڵ�ǰ����ˢ��Adapter,ʵ�ֵ�ǰ���ĸ�itemλ��,��չ�����ĸ�itemλ��
							mAdapter.notifyDataSetChanged();							
						}
						bpLoading.setVisibility(View.GONE);
						//���ؽ���
						isLoading = false;
						//mIndex += 20;
						//���·�ҳλ��.���ڷ�ҳλ�õ�������mList���ϵ�size������ͬ,ֱ�Ӹ�ֵ 
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
			//listview����,ʹ��convertView,��֤view���ᴴ����ζ�����ڴ����.
			if (convertView==null) {
				view = View.inflate(getApplicationContext(), R.layout.list_item_black_number, null);
				
				TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
				TextView tvMode = (TextView) view.findViewById(R.id.tv_mode);
				ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
				
				//��viewHolder����findviewbyid�ҵ���ֵ
				holder = new ViewHolder();
				holder.ivDelete = ivDelete;
				holder.tvMode = tvMode;
				holder.tvNumber = tvNumber;
				
				//��holder�����view����һ��.
				view.setTag(holder);
				System.out.println("��ʼ��view");
			} else {
				view = convertView;
				//��convertView��ȡ��holder����
				holder = (ViewHolder) view.getTag();
				System.out.println("����view");
			}
			
			BlackNumberInfo info = getItem(position);
			holder.tvNumber.setText(info.number);
			switch (info.mode) {
			case 1:
				holder.tvMode.setText("���ص绰");
				break;
			case 2:
				holder.tvMode.setText("���ض���");
				break;
			case 3:
				holder.tvMode.setText("����ȫ��");
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
