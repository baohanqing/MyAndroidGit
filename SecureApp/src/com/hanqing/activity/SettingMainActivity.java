package com.hanqing.activity;

import com.example.secureapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingMainActivity extends Activity{

	private TextView lockService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_home);
		
		lockService=(TextView) super.findViewById(R.id.tv_lock_service);
		
		lockService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(SettingMainActivity.this,LockServiceSetting.class);
				startActivity(intent);
			}
		});
		
	}
}
