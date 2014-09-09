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
 * ϵͳ������
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
		
		//����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		//���������
		titleName=(TextView) super.findViewById(R.id.main_title_name);
		//���ó����ǵ�����
		Typeface face=Typeface.createFromAsset(getAssets(), "font/DroidSansFallback.ttf");
		titleName.setTypeface(face);
		
		adapter=new MainUIAdapter(MainUI.this);
		preferences=this.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		
		//��ȡGridView����
		gridView=(GridView) super.findViewById(R.id.main_tab);
		gridView.setAdapter(adapter);
		
		
		
		
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				
				//Ϊ���ǵ��ֻ���������һ���޸����ƵĹ��ܣ��������أ����ֻ�������ʱ���ܹ��������ϳ������������ж�أ�ʧȥ��������
				if(position==0){
					
					//�޸��ֻ��������ֵ�����
					AlertDialog.Builder builder=new AlertDialog.Builder(MainUI.this);
					builder.setTitle("����");
					builder.setMessage("������Ҫ�޸ĵ�����");
					final EditText et=new EditText(MainUI.this);
					et.setHint("������");
					builder.setView(et);
					
					builder.setPositiveButton("ȷ��", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String name=et.getText().toString();
							if(name.equals("")){
								Toast.makeText(MainUI.this, "�������ݲ���Ϊ��", Toast.LENGTH_LONG);
							}
							else{
								//���û�������µ�����д�뵽SharePreference��config�ļ�����
								Editor editor=preferences.edit();
								editor.putString("editName", name);
								editor.commit();
								
								TextView tv=(TextView) view.findViewById(R.id.text_item);
								tv.setText(name);
								adapter.notifyDataSetChanged();
								
							}
						}
					});
					
					builder.setNegativeButton("ȡ��", new OnClickListener() {
						
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
					//�ֻ�����
					
					break;

				case 1:
					//ͨѶ��ʿ
					
					break;
				case 2:
					//�������
					Intent intent2=new Intent(MainUI.this,AppManagerActivity.class);
					startActivity(intent2);
					break;
				case 3:
					//��������
						
					break;
				case 4:
					//�������
					Intent intent4=new Intent(MainUI.this,ProgressManagerActivity.class);
					startActivity(intent4);
					break;
				case 5:
					//�ֻ�ɱ��
						
					break;
				case 6:
					//ϵͳ�Ż�
						
					break;
				case 7:
					//�߼�����
					Intent intent7=new Intent(MainUI.this,QueryZonePre.class);
					startActivity(intent7);	
					break;
				case 8:
					//��������
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