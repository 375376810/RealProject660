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
	//private int mTotalCount;//�������ܸ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		mDao = BlackNumberDao.getInstence(this);
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
						int totalCount = mDao.getTotalCount();
						if (mList.size()<totalCount) {
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
			
			final BlackNumberInfo info = getItem(position);
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
			}
			//��ɾ����ť��ӵ���¼�
			holder.ivDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//1.�����ݿ�ɾ��2.�Ӽ���ɾ��3.ˢ��listView
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
	
	/*��Ӻ�����*/
	public void addBlackNumber(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_add_black_number, null);
		dialog.setView(view, 0, 0, 0, 0);//��view���ֶ������ø�AlertDialog,��ʹ����padding��������Ϊ0
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etBlackNumber = (EditText) view.findViewById(R.id.et_black_number);
		final RadioGroup rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		//ȷ����ť����¼�
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = etBlackNumber.getText().toString().trim();
				if (!TextUtils.isEmpty(phone)) {
					//��ȡ��ǰ����ѡ��radiobutton��id
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
					//��ӵ����ݿ�
					mDao.add(phone, mode);
					//���������Ԫ��
					BlackNumberInfo addInfo = new BlackNumberInfo();
					addInfo.number = phone;
					addInfo.mode = mode;
					mList.add(0, addInfo);
					//ˢ�½���
					mAdapter.notifyDataSetChanged();
					dialog.dismiss();
				}else {
					ToastUtils.showToast(getApplicationContext(), "�������ݲ���Ϊ��");
				}
			}
		});
		//ȡ����ť����¼�
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	
	}

}
