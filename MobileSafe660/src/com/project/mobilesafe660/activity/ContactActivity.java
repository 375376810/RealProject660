package com.project.mobilesafe660.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.mobilesafe660.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class ContactActivity extends Activity {

    private ListView lvContact;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        
        lvContact = (ListView) findViewById(R.id.lv_contact);
        
        final ArrayList<HashMap<String, String>> list = readContact();
        
        lvContact.setAdapter(new SimpleAdapter(this, list, R.layout.list_item_contact, new String[]{"name","phone"}, new int[]{R.id.tv_name,R.id.tv_phone}));
        
        lvContact.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> map = list.get(position);
				String phone = map.get("phone");
				//���绰������Ϣ����intent���ٻش����ϼ�ҳ��
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0, data);
				finish();
			}
		});
        		
    }

	/** ��ȡ��ϵ������ 
	 * @return **/
	private ArrayList<HashMap<String,String>> readContact() {
		//raw_contacts,data,mimetypes
		//1.�ȴ�raw_contacts�ж�ȡ��ϵ�˵�contact_id
		//2.����contact_id,��data�в����ϵ�������Ϣ
		//3.����mimetype_id,��ѯmimetypes��,�õ���Ϣ����(����,����)
		//����Ȩ��<uses-permission android:name="android.permission.READ_CONTACTS"/>
		Cursor rawContactCursor = getContentResolver().query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"}, null, null, null);
		//������в�ѯ������ϵ�˵���Ϣ
		ArrayList<HashMap<String, String>> list= new ArrayList<HashMap<String,String>>();
		//����������ϵ��
		while (rawContactCursor.moveToNext()) {
			String contactId = rawContactCursor.getString(0);
			//ϵͳ��ѯ�Ĳ���data�����,����view_data�����ͼ,��ͼ�Ƕ���������,data+mimetypes
			Cursor dataCursor = getContentResolver().query(Uri.parse("content://com.android.contacts/data"), new String[]{"data1","mimetype"}, "raw_contact_id=?", new String[]{contactId}, null);
			//����ĳ����ϵ�˵���Ϣ
			HashMap<String, String> map = new HashMap<String, String>();
			//����������ϵ�˵������ֶ�
			while (dataCursor.moveToNext()) {
				String data = dataCursor.getString(0);
				String type = dataCursor.getString(1);
				if("vnd.android.cursor.item/phone_v2".equals(type)) {
					//�绰����
					map.put("phone", data);
				} else if ("vnd.android.cursor.item/name".equals(type)) {
					//����
					map.put("name", data);
				}
			}
			dataCursor.close();
			//����������
			if(!TextUtils.isEmpty(map.get("name"))&&!TextUtils.isEmpty(map.get("phone"))){
				list.add(map);
			}
		}
		rawContactCursor.close();
		
		return list;
	}
}
