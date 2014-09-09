package com.hanqing.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secureapp.R;
import com.hanqing.service.AddressService;
import com.hanqing.service.BackupSmsService;
import com.hanqing.service.DownloadVersionTask;
import com.hanqing.service.SmsService;

/**
 * ��ҳ��Ϊ��ѯ������ҳ���ǰһ��ҳ��
 * �����ҳ���������ת����ѯ��ҳ��
 * @author baohanqing
 *
 */
public class QueryZonePre extends Activity implements OnClickListener{

	//�ֻ���������ز�ѯ
	private TextView tv_location_search;
	
	//�Ƿ����ֻ������ز�ѯ����
	private CheckBox cb_is_selected;
	
	//��������ʾ���
	private TextView tv_location_style;
	
	//��������ʾλ��
	private TextView tv_location_postion;
	
	//�ֻ���������ز�ѯ�ķ���
	Intent serviceIntent=null;
	
	//������ʾ����״̬
	private TextView tv_service_state;
	
	//����������
	private TextView tv_balck_number;
	
	//���ű���
	private TextView tv_sms_backup;
	
	//���Żָ�
	private TextView tv_msg_recover;

	//������
	private TextView tv_app_lock;
	
	SharedPreferences sp;
	
	
	private ProgressDialog pd;
	private Handler handler;
	
	private static final int SUCCESS=1;
	private static final int ERROR=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_zone_pre);
		
		serviceIntent=new Intent(this, AddressService.class);
		
		tv_location_search=(TextView) super.findViewById(R.id.tv_location_search);
		
		cb_is_selected=(CheckBox) super.findViewById(R.id.cb_is_selected);
		
		tv_location_style=(TextView) super.findViewById(R.id.tv_location_style);
		
		tv_location_postion=(TextView) super.findViewById(R.id.tv_location_postion);
		
		tv_service_state=(TextView) super.findViewById(R.id.tv_service_state);
		
		tv_balck_number=(TextView) super.findViewById(R.id.tv_balck_number);
		
		tv_location_search.setOnClickListener(this);
		
		tv_sms_backup=(TextView) super.findViewById(R.id.tv_msg_backup);
		
		tv_msg_recover=(TextView) super.findViewById(R.id.tv_msg_recover);
		
		tv_app_lock=(TextView) super.findViewById(R.id.tv_app_lock);
		
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		
		
		
		tv_balck_number.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(QueryZonePre.this,BlackNumberActivity.class);
				startActivity(intent);
				
			}
		});
		
		
		tv_location_style.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectStyle();
				
			}
		});
		
		tv_location_postion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ��������ʾλ��
				Intent intent=new Intent(QueryZonePre.this,DragViewActivity.class);
				startActivity(intent);
				
			}
		});
		
		cb_is_selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					//����������ʾ����
					startService(serviceIntent);
					tv_service_state.setText("������ʾ�ѿ���");
				}
				else{
					stopService(serviceIntent);
					tv_service_state.setText("������ʾ�ѹر�");
					
				}
			}
		});
		
		
		
		//���ű���
		tv_sms_backup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(QueryZonePre.this,BackupSmsService.class);
				startService(intent);
			}
		});
		
		
		
		//���Żָ�
		tv_msg_recover.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				restore();
			}
		});
		
		
		//������
		tv_app_lock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(QueryZonePre.this,AppLockActivity.class);
				startActivity(intent);
			}
		});
		
		
		
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
				switch(msg.what){
					case ERROR:
						Toast.makeText(QueryZonePre.this, "�������ݿ�ʧ�ܣ��������磡", Toast.LENGTH_LONG).show();
						break;
					case SUCCESS:
						Toast.makeText(QueryZonePre.this, "���ݿ����سɹ�", Toast.LENGTH_LONG).show();
						break;
					default:
						break;
						
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
			case R.id.tv_location_search:
				//������ݿ����
				if(isDBExist()){
					Intent intent=new Intent(this,QueryZone.class);
					startActivity(intent);
				}
				else{
					//��ʾ�û��������ݿ�
					pd=new ProgressDialog(this);
					pd.setMessage("�����������ݿ�...");
					pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					pd.setCancelable(false);
					pd.show();
					
					//�¿�һ���߳̿�ʼ����
					new Thread(){

						@Override
						public void run() {
							
							String path=getResources().getString(R.string.serverdb);
							
							File dir=new File(Environment.getExternalStorageDirectory(),"/SecureApp/db");
							
							Log.e("dir", Environment.getExternalStorageDirectory()+"/AecureApp/db");
							
							if(!dir.exists()){
								dir.mkdirs();
							}
							
							String dbPath=Environment.getExternalStorageDirectory()+"/SecureApp/db/number_location.db";
							
							try {
								DownloadVersionTask.getFile(path, dbPath, pd);
								pd.dismiss();
								
								Message msg=new Message();
								msg.what=SUCCESS;
								handler.sendMessage(msg);
								
							} catch (Exception e) {
								e.printStackTrace();
								pd.dismiss();
								Message msg=new Message();
								msg.what=ERROR;
								handler.sendMessage(msg);
							}
							
						}
					}.start();
				}
				break;
			default:
				break;
		}
		
	}
	
	//�ж����ݿ��Ƿ����
	private boolean isDBExist(){
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			
			File file=new File(Environment.getExternalStorageDirectory()+"/SecureApp/db/number_location.db");
			if(file.exists()){
				return true;
			}
		}
		return false;
	}

	
	
	//��������ʾ�������÷���
	private void selectStyle(){
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("��������ʾ���");
		String[] items=new String[]{"��͸��","������","ƻ����","��ȸ��","������"};
		
		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Editor editor=sp.edit();
				editor.putInt("background", which);
				editor.commit();
			}
		});
		
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
			}
		});
		
		builder.create().show();
	}
	
	
	//�ָ����ŵķ���
	private void restore(){
		
		final ProgressDialog pd=new ProgressDialog(this);
		pd.setTitle("��ԭ����");
		pd.setMessage("���ڻ�ԭ����");
		pd.setCancelable(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		
		final SmsService smsService=new SmsService(this);
		
		new Thread(){

			@Override
			public void run() {
				
				try {
					
					smsService.resotreSms(Environment.getExternalStorageDirectory()+"/security/backup/smsbackup.xml", pd);
					pd.dismiss();
					
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "���Ż�ԭ�ɹ�", Toast.LENGTH_LONG).show();
					Looper.loop();
					
					
				} catch (Exception e) {
					e.printStackTrace();
					
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "���Ż�ԭʧ��", Toast.LENGTH_LONG).show();
					Looper.loop();
				}
				
			}
			
			
			
			
		}.start();
		
		
	}
	
	
}





















