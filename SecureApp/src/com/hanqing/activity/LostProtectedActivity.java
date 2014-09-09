package com.hanqing.activity;
/**
 * 当用户按下1314号码之后，跳转到该界面
 * 如果用户第一次进来，进入输入确认密码界面
 * 如果用户第二次进来，直接输入密码登陆
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secureapp.R;
import com.hanqing.util.MD5Encoder;

public class LostProtectedActivity extends Activity implements OnClickListener{

	private SharedPreferences sp;
	private Dialog dialog;
	
	private EditText passwd;
	private EditText confrimPwd;
	
	TextView tv_phone_number;
	TextView tv_is_protected;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		
		if(isSetPassword()){
			//之前设置过密码，直接登陆
			showLoginDialog();
		}
		else{
			//注册
			showRegisterDilog();
		}
		
	}

	
	
	//判断是否需要设置密码，如果用户第一次进来，进入到输入确认密码界面，如果用户第二次进来，进入到登陆界面
	private boolean isSetPassword(){
		String pwd=sp.getString("lock_password", "");
		if(pwd.equals("")||pwd==null){
			return false;
		}
		return true;
	}
	
	//如果需要输入密码，则第一次进来需要进入注册界面（连输两次密码）
	private void showRegisterDilog(){
		dialog=new Dialog(LostProtectedActivity.this,R.style.MyDialog);
		View view=View.inflate(LostProtectedActivity.this, R.layout.fangdao_register_dialog, null);
		passwd=(EditText) view.findViewById(R.id.first_pwd);
		confrimPwd=(EditText) view.findViewById(R.id.confirm_pwd);
		
		Button confirm=(Button) view.findViewById(R.id.confirm_reg_pwd);
		Button cancle=(Button) view.findViewById(R.id.cancle_reg_pwd);
		
		confirm.setOnClickListener(this);
		cancle.setOnClickListener(this);
		
		dialog.setContentView(view);
		dialog.show();
		
	}

	//登陆界面
	private void showLoginDialog(){
		
		dialog=new Dialog(LostProtectedActivity.this,R.style.MyDialog);
		View view=View.inflate(LostProtectedActivity.this, R.layout.fangdao_login_dialog, null);
		passwd=(EditText) view.findViewById(R.id.login_pwd);
		Button login=(Button) view.findViewById(R.id.confirm_login_pwd);
		Button cancle=(Button) view.findViewById(R.id.cancle_login_pwd);
		
		login.setOnClickListener(this);
		cancle.setOnClickListener(this);
		
		dialog.setContentView(view);
		dialog.show();
	}
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
			
			case R.id.confirm_reg_pwd:
				//注册的确认按钮
				String fpwd=passwd.getText().toString();
				String cpwd=confrimPwd.getText().toString();
				
				if(fpwd.equals("")||cpwd.equals("")){
					Toast.makeText(LostProtectedActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				else{
					//如果两次输入的密码一致，就把该密码写入到SharePreferences当中
					if(fpwd.equals(cpwd)){
						Editor editor=sp.edit();
						editor.putString("lock_password", MD5Encoder.encode(fpwd));
						editor.commit();
						//跳转到防盗向导界面
						Intent intent=new Intent(LostProtectedActivity.this,FangDaoGuideActivity1.class);
						startActivity(intent);
						finish();
					}
					else{
						Toast.makeText(LostProtectedActivity.this, "两次输入密码不一致", Toast.LENGTH_LONG).show();
						return;
					}
				}
				
				dialog.dismiss();
				break;
			
			case R.id.cancle_reg_pwd:
				//注册取消按钮
				dialog.dismiss();
				finish();
				break;
				
			case R.id.confirm_login_pwd:
				//登陆确定按钮
				String loginPwd=passwd.getText().toString();
				if(loginPwd.equals("")){
					Toast.makeText(LostProtectedActivity.this, "请输入登录密码", Toast.LENGTH_LONG).show();
				}
				else{
					//得到存储的密码
					String str=sp.getString("lock_password", "");
					if(MD5Encoder.encode(loginPwd).equals(str)){
						dialog.dismiss();
						
						setContentView(R.layout.lost_protected_activity);
						tv_phone_number=(TextView) super.findViewById(R.id.tv_phone_number);
						tv_is_protected=(TextView) super.findViewById(R.id.tv_is_protected);
						
						tv_phone_number.setText("保护的手机号码："+sp.getString("phone_number", ""));
						tv_is_protected.setText("手机是否已经开启保护："+sp.getBoolean("isProtected", false));
					}
					else{
						Toast.makeText(LostProtectedActivity.this, "登录密码错误", Toast.LENGTH_LONG).show();
					}
				}
				
				break;
				
			case R.id.cancle_login_pwd:
				//登陆取消按钮
				dialog.dismiss();
				finish();
				break;
		}

	}

}
