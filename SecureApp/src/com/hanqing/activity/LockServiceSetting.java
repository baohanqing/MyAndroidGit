package com.hanqing.activity;

import com.example.secureapp.R;
import com.hanqing.service.WatchLockAppService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LockServiceSetting extends Activity{

	private Button startLockService;
	private Button stopLockService;
	
	private TextView lockState;
	
	private SharedPreferences sp;
	
	private Intent appLockIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		startLockService=(Button) super.findViewById(R.id.but_start_lock_service);
		stopLockService=(Button) super.findViewById(R.id.but_stop_lock_service);
		
		lockState=(TextView) super.findViewById(R.id.tv_lock_state);
		
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		
		appLockIntent=new Intent(LockServiceSetting.this,WatchLockAppService.class);
		
		//���ǽ��Ƿ�������д��sp����
		boolean isAppLock=sp.getBoolean("isLock", false);
		
		if(isAppLock){
			lockState.setText("�Ѿ���������������");
		}
		else{
			lockState.setText("�����������Ѿ��ر�");
		}
		
		startLockService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(LockServiceSetting.this, "�����Ѿ�����", Toast.LENGTH_LONG).show();
				
				Editor editor=sp.edit();
				editor.putBoolean("isLock", true);
				editor.commit();
				
				startService(appLockIntent);
				
				lockState.setText("�����������Ѿ�����");
				
			}
		});
		
		stopLockService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(LockServiceSetting.this, "�����Ѿ��ر�", Toast.LENGTH_LONG).show();
				
				Editor editor=sp.edit();
				editor.putBoolean("isLock", false);
				editor.commit();
				
				stopService(appLockIntent);
				
				lockState.setText("��δ��������������");
				
			}
		});
		
	}

	
	
}
