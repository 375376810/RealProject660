package com.project.mobilesafe660.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.mobilesafe660.R;
import com.project.mobilesafe660.utils.Md5Utils;
import com.project.mobilesafe660.utils.PrefUtils;
import com.project.mobilesafe660.utils.ToastUtils;

/***
 * @author Ԭ����
 * ��ҳ��
 */
public class HomeActivity extends Activity {
	
	private GridView gvHome;
	String[] mHomeNames = new String[]{"�ֻ�����","ͨѶ��ʿ","�������","���̹���","����ͳ��","�ֻ�ɱ��","��������","�߼�����","��������"};
	int[] mImageIds = new int[] { 
			R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		gvHome.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				switch (position) {
				case 0:
					//�ֻ�����
					showSafeDialog();
					break;
				case 1:
					//ͨѶ��ʿ
					startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
					break;
				case 2:
					//�������
					startActivity(new Intent(getApplicationContext(),AppManagerActivity.class));
					break;
				case 3:
					//���̹���
					startActivity(new Intent(getApplicationContext(),ProcessManagerActivity.class));
					break;
				case 4:
					//����ͳ��
					startActivity(new Intent(getApplicationContext(),TrafficStatsActivity.class));
					break;
				case 5:
					//�ֻ�ɱ��
					startActivity(new Intent(getApplicationContext(),AntiVirusActivity.class));
					break;
				case 7:
					startActivity(new Intent(getApplicationContext(),AdvancedToolsActivity.class));
					//�߼�����
					break;
				case 8:
					//��������
					startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
					break;

				default:
					break;
				}
			}
			
		});
	}
	
	/** �ֻ��������� **/
	protected void showSafeDialog() {
		String passsword = PrefUtils.getString("password", null, this);
		//�����ļ�������Ϣ��Ϊ��
		if (!TextUtils.isEmpty(passsword)) {
			//��������
			showInputPwDialog();
		} else {
			//��������
			showSetPwdDialog();			
		}
	}

	/** ��������Ի��� **/
	private void showInputPwDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_input_pwd, null);
		dialog.setView(view, 0, 0, 0, 0);//��view���ֶ������ø�AlertDialog,��ʹ����padding��������Ϊ0
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
		//ȷ����ť����¼�
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = etPwd.getText().toString().trim();
				if (!TextUtils.isEmpty(password)) {
					String savedPwd = PrefUtils.getString("password", null, getApplicationContext());
					if (savedPwd.equals(Md5Utils.encode(password))) {
						//������ȷ
						dialog.dismiss();
						//��ת���ֻ�����ҳ��
						startActivity(new Intent(getApplicationContext(),LostAndFindActivity.class));
					} else {						
						ToastUtils.showToast(getApplicationContext(), "�������");
					}
				}else {
					ToastUtils.showToast(getApplicationContext(), "���벻��Ϊ��");
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

	/** ��������Ի��� **/
	private void showSetPwdDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_set_pwd, null);
		dialog.setView(view, 0, 0, 0, 0);//��view���ֶ������ø�AlertDialog,��ʹ����padding��������Ϊ0
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
		final EditText etPwdConfirm = (EditText) view.findViewById(R.id.et_pwd_confirm);
		//ȷ����ť����¼�
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = etPwd.getText().toString().trim();
				String pwdConfirm = etPwdConfirm.getText().toString().trim();
				if (!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(pwdConfirm)) {
					if (password.equals(pwdConfirm)) {
						//��������������config�ļ�
						PrefUtils.putString("password", Md5Utils.encode(password),getApplicationContext());
						dialog.dismiss();
						//��ת���ֻ�����ҳ��
						startActivity(new Intent(getApplicationContext(),LostAndFindActivity.class));
					} else {
						ToastUtils.showToast(getApplicationContext(), "�������벻һ��");
					}
				}else {
					ToastUtils.showToast(getApplicationContext(), "���벻��Ϊ��");
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

	class HomeAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mHomeNames.length;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.list_item_home, null);
			TextView tvName = (TextView) view.findViewById(R.id.tv_name);
			ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
			
			tvName.setText(mHomeNames[position]);
			ivIcon.setImageResource(mImageIds[position]);
			
			return view;
		}
	}
	
}
