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
 * @author 袁星明
 * 主页面
 */
public class HomeActivity extends Activity {
	
	private GridView gvHome;
	String[] mHomeNames = new String[]{"手机防盗","通讯卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
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
					//手机防盗
					showSafeDialog();
					break;
				case 1:
					//通讯卫士
					startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
					break;
				case 2:
					//软件管理
					startActivity(new Intent(getApplicationContext(),AppManagerActivity.class));
					break;
				case 3:
					//进程管理
					startActivity(new Intent(getApplicationContext(),ProcessManagerActivity.class));
					break;
				case 4:
					//流量统计
					startActivity(new Intent(getApplicationContext(),TrafficStatsActivity.class));
					break;
				case 5:
					//手机杀毒
					startActivity(new Intent(getApplicationContext(),AntiVirusActivity.class));
					break;
				case 7:
					startActivity(new Intent(getApplicationContext(),AdvancedToolsActivity.class));
					//高级工具
					break;
				case 8:
					//设置中心
					startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
					break;

				default:
					break;
				}
			}
			
		});
	}
	
	/** 手机防盗弹窗 **/
	protected void showSafeDialog() {
		String passsword = PrefUtils.getString("password", null, this);
		//本地文件密码信息不为空
		if (!TextUtils.isEmpty(passsword)) {
			//输入密码
			showInputPwDialog();
		} else {
			//设置密码
			showSetPwdDialog();			
		}
	}

	/** 输入密码对话框 **/
	private void showInputPwDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_input_pwd, null);
		dialog.setView(view, 0, 0, 0, 0);//将view布局对象设置给AlertDialog,并使布局padding上下左右为0
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
		//确定按钮点击事件
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = etPwd.getText().toString().trim();
				if (!TextUtils.isEmpty(password)) {
					String savedPwd = PrefUtils.getString("password", null, getApplicationContext());
					if (savedPwd.equals(Md5Utils.encode(password))) {
						//密码正确
						dialog.dismiss();
						//跳转到手机防盗页面
						startActivity(new Intent(getApplicationContext(),LostAndFindActivity.class));
					} else {						
						ToastUtils.showToast(getApplicationContext(), "密码错误");
					}
				}else {
					ToastUtils.showToast(getApplicationContext(), "密码不能为空");
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

	/** 设置密码对话框 **/
	private void showSetPwdDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_set_pwd, null);
		dialog.setView(view, 0, 0, 0, 0);//将view布局对象设置给AlertDialog,并使布局padding上下左右为0
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
		final EditText etPwdConfirm = (EditText) view.findViewById(R.id.et_pwd_confirm);
		//确定按钮点击事件
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = etPwd.getText().toString().trim();
				String pwdConfirm = etPwdConfirm.getText().toString().trim();
				if (!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(pwdConfirm)) {
					if (password.equals(pwdConfirm)) {
						//保存密码至本地config文件
						PrefUtils.putString("password", Md5Utils.encode(password),getApplicationContext());
						dialog.dismiss();
						//跳转到手机防盗页面
						startActivity(new Intent(getApplicationContext(),LostAndFindActivity.class));
					} else {
						ToastUtils.showToast(getApplicationContext(), "两次密码不一致");
					}
				}else {
					ToastUtils.showToast(getApplicationContext(), "密码不能为空");
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
