package com.hanqing.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secureapp.R;
import com.hanqing.dao.BlackNumberDao;
import com.hanqing.service.AddressService;

/**
 * 黑名单的页面
 * 添加、修改黑名单
 * @author baohanqing
 *
 */
public class BlackNumberActivity extends Activity{


	
	private Button but_manager;
	private ListView num_list;
	
	private BlackNumberDao numberDao;
	
	private NumberAdapter adapter;
	
	//获取所有联系人的List
	private List<String> numbers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.black_number_layout);
		
		but_manager=(Button) super.findViewById(R.id.but_man);
		num_list=(ListView) super.findViewById(R.id.number_list);
		
		numberDao=new BlackNumberDao(BlackNumberActivity.this);
		
		numbers=numberDao.findAll();
		
		adapter=new NumberAdapter();
		
		num_list.setAdapter(adapter);
		
		//为listView注册一个上下文菜单
		registerForContextMenu(num_list);
		
		Intent addressService=new Intent(BlackNumberActivity.this,AddressService.class);
		startService(addressService);
		
		//添加按钮，监听事件
		but_manager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				addNumber();
				
			}
		});
	}

	
	
	//创建上下文菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater=getMenuInflater();
		
		inflater.inflate(R.menu.black_menu, menu);
		
	}

	
	//当菜单被选中的时候调用方法
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		//拿到点击菜单的信息
		AdapterContextMenuInfo info=(AdapterContextMenuInfo) item.getMenuInfo();
		
		switch(item.getItemId()){
			
			//修改黑名单号码
			case R.id.change_num:
				String oldNumber=numbers.get((int) info.id);
				updateNumber(oldNumber);
				numbers=numberDao.findAll();
				adapter.notifyDataSetChanged();
				break;
			
			//删除黑名单号码
			case R.id.delete_num:
				String number=numbers.get((int) info.id);
				
				Log.e("删除number", number+"");
				
				numberDao.delete(number);
				
				numbers=numberDao.findAll();
				adapter.notifyDataSetChanged();
				break;
				
			default:
				break;
		
		}
		
		
		
		return super.onContextItemSelected(item);
	}


	public void addNumber(){
		
		AlertDialog.Builder builder=new AlertDialog.Builder(BlackNumberActivity.this);
		builder.setTitle("添加黑名单");
		
		final EditText et_num=new EditText(BlackNumberActivity.this);
		et_num.setHint("请输入要添加的黑名单号码");
		et_num.setInputType(InputType.TYPE_CLASS_PHONE);
		
		builder.setView(et_num);
		
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String newNum=et_num.getText().toString().trim();
				
				if(newNum.equals("")){
					Toast.makeText(BlackNumberActivity.this, "请输入要添加的号码", Toast.LENGTH_LONG).show();
				}
				else{
					numberDao.add(newNum);
					numbers=numberDao.findAll();
					
					adapter.notifyDataSetChanged();
					
				}
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
			}
		});
		
		
		builder.create().show();
		
	}

	
	//更新黑名单
	private void updateNumber(final String oldNumber){
		
		AlertDialog.Builder builder=new AlertDialog.Builder(BlackNumberActivity.this);
		builder.setTitle("更新黑名单");
		
		final EditText et_number=new EditText(BlackNumberActivity.this);
		//只能输入数字类型
		et_number.setInputType(InputType.TYPE_CLASS_PHONE);
		et_number.setHint("请输入新号码");
		
		builder.setView(et_number);
		
		builder.setPositiveButton("修改", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String number=et_number.getText().toString().trim();
				if(number.equals("")){
					Toast.makeText(BlackNumberActivity.this, "添加号码不能为空！", Toast.LENGTH_LONG).show();
				}
				else{
					numberDao.update(oldNumber, number);
					numbers=numberDao.findAll();
					adapter.notifyDataSetChanged();
					
				}
				
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
			}
		});
		
		builder.create().show();
		
	}
	
	
	//为电话的ListView提供数据
	private class NumberAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			return numbers.size();
		}

		@Override
		public Object getItem(int position) {
			
			return numbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView==null){
				
				View view=View.inflate(BlackNumberActivity.this, R.layout.black_number_list_item, null);
				TextView tv_phone_num=(TextView) view.findViewById(R.id.tv_item_phone_num);
				
				Log.e("numbers", numbers+"d "+tv_phone_num);
				
				tv_phone_num.setText(numbers.get(position));
				
				return view;
				
			}
			else{
				
				TextView tv_phone_num=(TextView) convertView.findViewById(R.id.tv_item_phone_num);
				tv_phone_num.setText(numbers.get(position));
				
				return convertView;
				
				
			}
		}
	}
}















