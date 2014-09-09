package com.hanqing.activity;

import com.example.secureapp.R;
import com.hanqing.receiver.MyReceiverAdmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FangDaoGuideActivity4 extends Activity implements OnTouchListener,OnGestureListener{

	GestureDetector gesture;
	LinearLayout ll;
	Intent intent;
	
	TextView isSetLock;
	TextView cancleFangdao;
	Button finishBut;
	
	SharedPreferences sp;
	Editor editor;
	
	//�õ�һ���豸�������������û�����ж�ظ�Ӧ��
	DevicePolicyManager devicePolicyManager;
	//newһ�������������������ע�����������
	ComponentName componentName;
	
	public static final int MIN_DISTANCE=50;
	public static final int MIN_VELOCITY=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fangdao_guide4);
		
		isSetLock=(TextView) super.findViewById(R.id.is_set_locked);
		cancleFangdao=(TextView) super.findViewById(R.id.cancle_fangdao);
		
		sp=this.getSharedPreferences("config",Context.MODE_PRIVATE);
		
		devicePolicyManager=(DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName=new ComponentName(FangDaoGuideActivity4.this, MyReceiverAdmin.class);
		
		
		//���ȡ������
		cancleFangdao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//�û����ȡ��������ȷ�϶Ի���
				AlertDialog.Builder builder=new AlertDialog.Builder(FangDaoGuideActivity4.this);
				builder.setTitle("����");
				builder.setMessage("ȷ��ȡ������������ȡ�����ֻ�������������");
				builder.setCancelable(false);
				
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						editor=sp.edit();
						editor.putBoolean("isProtected",false);
						editor.commit();
						finish();
					}
				});
				
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				
				builder.create().show();
				
				
			}
			
		});
		
		
		finishBut=(Button) super.findViewById(R.id.finish_but);
		
		//�����ɿ���,������������Ϣд�뵽SharedPreferences����
		finishBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editor=sp.edit();
				//��������������Ϣд�뵽SharedPreference����
				editor.putBoolean("isProtected", true);
				editor.commit();
				//�ж�һ���Ƿ�����ֻ����к�
				String simSerial=sp.getString("sim_serial", null);
				String phoneNum=sp.getString("phone_number", null);
				if(simSerial==null || phoneNum==null){
					
					Toast.makeText(FangDaoGuideActivity4.this, "��δ���ֻ�����ѡ���ֻ�����", Toast.LENGTH_LONG).show();
					
				}
				else{
					
					//��ת���������óɹ��Ľ���
					//Intent intent=new Intent(FangDaoGuideActivity4.this,LostProtectedActivity.class);
					//startActivity(intent);
					
					startActive();
				}
			}
		});
		
		intent=new Intent();
		
		gesture=new GestureDetector(this);
		ll=(LinearLayout) super.findViewById(R.id.fangdao3_ll);
		ll.setOnTouchListener(this);
		ll.setClickable(true);
	}

	//��������
	private void startActive(){
		
	boolean isActive=devicePolicyManager.isAdminActive(componentName);
		
	Log.e("hahaa", "devicePolicyManager"+devicePolicyManager+"componentName"+componentName+"isActive"+isActive);
	
		//�ж��Ƿ��Ѿ�ע�����û�оͽ���ע��
		if(!isActive){
		
			Intent myIntent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			myIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			myIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "����");
			finish();
			startActivity(myIntent);
			
		}
	}
	
	@Override
	public boolean onDown(MotionEvent e) {

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		//��ʾ���󻬶�
		if(e1.getX()-e2.getX()>MIN_DISTANCE&&Math.abs(velocityX)>MIN_VELOCITY){
			
		}
			//��ʾ���һ���
		else if(e2.getX()-e1.getX()>MIN_DISTANCE&&Math.abs(velocityX)>MIN_VELOCITY){
			intent.setClass(FangDaoGuideActivity4.this, FangDaoGuideActivity3.class);
			finish();
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

		}
		
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		return gesture.onTouchEvent(event);
	}
}
