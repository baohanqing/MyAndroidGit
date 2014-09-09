package com.hanqing.activity;
/**
 * ���û�����1314����֮����ת���ý���
 * ����û���һ�ν�������������ȷ���������
 * ����û��ڶ��ν�����ֱ�����������½
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
			//֮ǰ���ù����룬ֱ�ӵ�½
			showLoginDialog();
		}
		else{
			//ע��
			showRegisterDilog();
		}
		
	}

	
	
	//�ж��Ƿ���Ҫ�������룬����û���һ�ν��������뵽����ȷ��������棬����û��ڶ��ν��������뵽��½����
	private boolean isSetPassword(){
		String pwd=sp.getString("lock_password", "");
		if(pwd.equals("")||pwd==null){
			return false;
		}
		return true;
	}
	
	//�����Ҫ�������룬���һ�ν�����Ҫ����ע����棨�����������룩
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

	//��½����
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
				//ע���ȷ�ϰ�ť
				String fpwd=passwd.getText().toString();
				String cpwd=confrimPwd.getText().toString();
				
				if(fpwd.equals("")||cpwd.equals("")){
					Toast.makeText(LostProtectedActivity.this, "���벻��Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				else{
					//����������������һ�£��ͰѸ�����д�뵽SharePreferences����
					if(fpwd.equals(cpwd)){
						Editor editor=sp.edit();
						editor.putString("lock_password", MD5Encoder.encode(fpwd));
						editor.commit();
						//��ת�������򵼽���
						Intent intent=new Intent(LostProtectedActivity.this,FangDaoGuideActivity1.class);
						startActivity(intent);
						finish();
					}
					else{
						Toast.makeText(LostProtectedActivity.this, "�����������벻һ��", Toast.LENGTH_LONG).show();
						return;
					}
				}
				
				dialog.dismiss();
				break;
			
			case R.id.cancle_reg_pwd:
				//ע��ȡ����ť
				dialog.dismiss();
				finish();
				break;
				
			case R.id.confirm_login_pwd:
				//��½ȷ����ť
				String loginPwd=passwd.getText().toString();
				if(loginPwd.equals("")){
					Toast.makeText(LostProtectedActivity.this, "�������¼����", Toast.LENGTH_LONG).show();
				}
				else{
					//�õ��洢������
					String str=sp.getString("lock_password", "");
					if(MD5Encoder.encode(loginPwd).equals(str)){
						dialog.dismiss();
						
						setContentView(R.layout.lost_protected_activity);
						tv_phone_number=(TextView) super.findViewById(R.id.tv_phone_number);
						tv_is_protected=(TextView) super.findViewById(R.id.tv_is_protected);
						
						tv_phone_number.setText("�������ֻ����룺"+sp.getString("phone_number", ""));
						tv_is_protected.setText("�ֻ��Ƿ��Ѿ�����������"+sp.getBoolean("isProtected", false));
					}
					else{
						Toast.makeText(LostProtectedActivity.this, "��¼�������", Toast.LENGTH_LONG).show();
					}
				}
				
				break;
				
			case R.id.cancle_login_pwd:
				//��½ȡ����ť
				dialog.dismiss();
				finish();
				break;
		}

	}

}
