package com.hanqing.activity;

import com.example.secureapp.R;
import com.hanqing.adapter.MainUIAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 系统主界面
 * @author baohanqing
 *
 */

public class MainUI extends Activity{

	TextView titleName;
	GridView gridView;

	private MainUIAdapter adapter;
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		//主界面标题
		titleName=(TextView) super.findViewById(R.id.main_title_name);
		//设置成我们的字体
		Typeface face=Typeface.createFromAsset(getAssets(), "font/DroidSansFallback.ttf");
		titleName.setTypeface(face);
		
		adapter=new MainUIAdapter(MainUI.this);
		preferences=this.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		
		//获取GridView对象
		gridView=(GridView) super.findViewById(R.id.main_tab);
		gridView.setAdapter(adapter);
		
		
		
		
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				
				//为我们的手机防盗增加一个修改名称的功能，便于隐藏，在手机被盗的时候，能够不被辨认出来而导致软件卸载，失去防盗功能
				if(position==0){
					
					//修改手机防盗部分的名称
					AlertDialog.Builder builder=new AlertDialog.Builder(MainUI.this);
					builder.setTitle("设置");
					builder.setMessage("请输入要修改的名字");
					final EditText et=new EditText(MainUI.this);
					et.setHint("新名称");
					builder.setView(et);
					
					builder.setPositiveButton("确定", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String name=et.getText().toString();
							if(name.equals("")){
								Toast.makeText(MainUI.this, "输入内容不能为空", Toast.LENGTH_LONG);
							}
							else{
								//把用户输入的新的名字写入到SharePreference的config文件当中
								Editor editor=preferences.edit();
								editor.putString("editName", name);
								editor.commit();
								
								TextView tv=(TextView) view.findViewById(R.id.text_item);
								tv.setText(name);
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
				
				return false;
				
				}
			
			});
		
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				switch (position) {
				case 0:
					//手机防盗
					
					break;

				case 1:
					//通讯卫士
					
					break;
				case 2:
					//软件管理
					Intent intent2=new Intent(MainUI.this,AppManagerActivity.class);
					startActivity(intent2);
					break;
				case 3:
					//流量管理
						
					break;
				case 4:
					//任务管理
					Intent intent4=new Intent(MainUI.this,ProgressManagerActivity.class);
					startActivity(intent4);
					break;
				case 5:
					//手机杀毒
						
					break;
				case 6:
					//系统优化
						
					break;
				case 7:
					//高级工具
					Intent intent7=new Intent(MainUI.this,QueryZonePre.class);
					startActivity(intent7);	
					break;
				case 8:
					//设置中心
					Intent intent8=new Intent(MainUI.this,SettingMainActivity.class);
					startActivity(intent8);	
					break;	
					
				default:
					break;
				}
				
			}
		});
		
	}
}