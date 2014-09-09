package com.hanqing.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.secureapp.R;
import com.hanqing.domain.ContractInfo;
import com.hanqing.service.ContractInfoService;

public class SelectContactsActivity extends Activity{

	ListView listView;
	
	List<ContractInfo> contacts; 
	
	private LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contacts);
		
		//��ȡ������ϵ�˵Ķ����������绰
		contacts=new ContractInfoService(this).getContractsInfo();
		
		 inflater = LayoutInflater.from(this);
		
		listView=(ListView) super.findViewById(R.id.list);
		
		//���ü���ĸ߶�
		listView.setDividerHeight(1);
		
		listView.setAdapter(new SelectContactAdapter());
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String number=contacts.get(position).getPhone();
				
				
				//��������ظ�FangDaoGuideActivity3
				Intent intent=new Intent(SelectContactsActivity.this,FangDaoGuideActivity3.class);
				intent.putExtra("number", number);
				setResult(101, intent);
				finish();
			}
		});

	}

	
	
	private class SelectContactAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return contacts.size();
		}

		@Override
		public Object getItem(int position) {
			return contacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ContractInfo info=contacts.get(position);
			MainViews views;
			View view;
			/*
			 * convertView�൱�ڻ���һ����ֻҪ�����ж�һ�����ǲ���Null���Ϳ���֪�����view��û�л��Ƴ���
			 * ���û�л��ƹ������»��ƣ����ƹ������ǾͿ���ֱ��ʹ�û�����
			 */
			if(convertView==null){
				views=new MainViews();
				view=inflater.inflate(R.layout.contacts_list_item, null);
				views.tvName=(TextView) view.findViewById(R.id.tv_name);
				views.tvPhone=(TextView) view.findViewById(R.id.tv_phone);
				
				views.tvName.setText(info.getName());
				views.tvPhone.setText(info.getPhone());
				
				view.setTag(views);
			}
			else{
				view=convertView;
				views=(MainViews) view.getTag();
				views.tvName=(TextView) view.findViewById(R.id.tv_name);
				views.tvPhone=(TextView) view.findViewById(R.id.tv_phone);
				
				views.tvName.setText(info.getName());
				views.tvPhone.setText(info.getPhone());
				
			}
			
			return view;
			
		}
		
	}
	
	//��Ҫ���ƿؼ�����
	private class MainViews{
		TextView tvName;
		TextView tvPhone;
	}
	
	
	
}
