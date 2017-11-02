package com.project.mobilesafe660.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.db.dao.CommonNumberDao;
import com.project.mobilesafe660.db.dao.CommonNumberDao.ChildInfo;
import com.project.mobilesafe660.db.dao.CommonNumberDao.GroupInfo;

public class CommonNumberActivity extends Activity {

	private ExpandableListView elvList;
	private CommonNumberAdapter mAdapter;
	private ArrayList<GroupInfo> mCommonNumberGroups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_number);
		elvList = (ExpandableListView) findViewById(R.id.elv_list);
		
		//�����ݿ��ж�ȡ������Ϣ
		mCommonNumberGroups = CommonNumberDao.getCommonNumberGroup();
		
		
		mAdapter = new CommonNumberAdapter();
		elvList.setAdapter(mAdapter);
		elvList.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				ChildInfo child = mAdapter.getChild(groupPosition, childPosition);
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+child.number));
				startActivity(intent);
				return false;
			}
		});		
	}
	
	class CommonNumberAdapter extends BaseExpandableListAdapter {

		//�����������
		@Override
		public int getGroupCount() {
			return mCommonNumberGroups.size();
		}

		//����ÿ����ĺ�������
		@Override
		public int getChildrenCount(int groupPosition) {
			return mCommonNumberGroups.get(groupPosition).children.size();
		}

		//���ص�ǰ�������
		@Override
		public GroupInfo getGroup(int groupPosition) {
			return mCommonNumberGroups.get(groupPosition);
		}

		//���ص�ǰ�ĺ��Ӷ���
		@Override
		public ChildInfo getChild(int groupPosition, int childPosition) {
			return mCommonNumberGroups.get(groupPosition).children.get(childPosition);
		}

		//������id
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		//���غ���id
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		//id�Ƿ��ȶ�
		@Override
		public boolean hasStableIds() {
			return false;
		}

		//������Ĳ���
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView view = new TextView(getApplicationContext());
			view.setTextColor(Color.RED);
			view.setTextSize(20);
			GroupInfo group = getGroup(groupPosition);
			view.setText("       "+group.name);
			return view;
		}

		//���غ��ӵĲ���
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView view = new TextView(getApplicationContext());
			view.setTextColor(Color.BLACK);
			view.setTextSize(18);
			ChildInfo child = getChild(groupPosition, childPosition);
			view.setText(child.name + "\n"+ child.number);
			return view;
		}

		//�����Ƿ���Ե��
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
	}

}
