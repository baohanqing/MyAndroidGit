package com.hanqing.activity;

import com.example.secureapp.R;
import com.hanqing.util.MD5Encoder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 该界面为锁定应用程序，输入密码的界面
 * @author baohanqing
 *
 */
public class LockActivityPwd extends Activity{

	private ImageView appIcon;
	private TextView appName;
	private EditText passwd;
	private Button confirmBut;
	
	private SharedPreferences sp;
	
	private String setPasswd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_lock_app_pwd);
		
		appIcon=(ImageView) super.findViewById(R.id.iv_app_icon);
		appName=(TextView) super.findViewById(R.id.tv_app_name);
		passwd=(EditText) super.findViewById(R.id.et_passwd);
		confirmBut=(Button) super.findViewById(R.id.but_confirm_passwd);
		
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		
		//我们设置的程序密码，默认为空
		setPasswd=sp.getString("lock_password", "");
		
		confirmBut.setOnClickListener(new OnClickListener() {
			
			String userInputPwd=passwd.getText().toString().trim();
			
			@Override
			public void onClick(View v) {
				if(setPasswd.equals("")){
					Toast.makeText(LockActivityPwd.this, "请设置防盗密码", Toast.LENGTH_LONG).show();
				}
				else if(userInputPwd.equals("")||userInputPwd==null){
					Toast.makeText(LockActivityPwd.this, "密码不能为空", Toast.LENGTH_LONG).show();
				}
				else if(setPasswd.equals(MD5Encoder.encode(userInputPwd))){
					finish();
				}
				else{
					Toast.makeText(LockActivityPwd.this, "密码错误", Toast.LENGTH_LONG).show();
				}
			}
		});

		
		try {
			
			//获取package，通过Intent传递过来的
			String packageName=getIntent().getStringExtra("packageName");
			//然后根据包名获取到我们的应用程序的信息
			
			ApplicationInfo appInfo=getPackageManager().getPackageInfo(packageName, 0).applicationInfo;
			
			//应用图标
			Drawable app_icon=appInfo.loadIcon(getPackageManager());
			
			//应用名字
			String app_name=appInfo.loadLabel(getPackageManager()).toString();
			
			appIcon.setImageDrawable(app_icon);
			appName.setText(app_name);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//屏蔽后退键
		if(KeyEvent.KEYCODE_BACK==event.getKeyCode()){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	
}
