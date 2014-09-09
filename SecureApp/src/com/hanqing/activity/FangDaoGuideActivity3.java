package com.hanqing.activity;

import com.example.secureapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 防盗引导页第三个
 * @author baohanqing
 *
 */

public class FangDaoGuideActivity3 extends Activity implements OnTouchListener,OnGestureListener{

	GestureDetector gesture;
	LinearLayout ll;
	Intent intent;
	
	Button selBut;
	
	EditText phoneNumber;
	
	SharedPreferences sp;
	
	
	public static final int MIN_DISTANCE=50;
	public static final int MIN_VELOCITY=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fangdao_guide3);
		
		sp=this.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		intent=new Intent();
		
		gesture=new GestureDetector(this);
		ll=(LinearLayout) super.findViewById(R.id.fangdao3_ll);
		ll.setOnTouchListener(this);
		ll.setClickable(true);
		
		phoneNumber=(EditText) super.findViewById(R.id.contracts_text);
		
		//读取存在SharedPreferences当中的最新的号码
		String phoneNum=sp.getString("phone_number", "");
		phoneNumber.setText(phoneNum);
		
		selBut=(Button) super.findViewById(R.id.search_contracts_but);
		
		selBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(FangDaoGuideActivity3.this,SelectContactsActivity.class);
				startActivityForResult(intent, 100);
			}
		});
		
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode==100&&resultCode==101){
			//获取到传过来的手机号
			String number=data.getStringExtra("number");
			phoneNumber.setText(number);
			
			//将该电话号码存到SharedPreference当中
			Editor editor=sp.edit();
			editor.putString("phone_number", number);
			editor.commit();
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
		
		
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
		
			//表示向左滑动
			if(e1.getX()-e2.getX()>MIN_DISTANCE&&Math.abs(velocityX)>MIN_VELOCITY){
				intent.setClass(FangDaoGuideActivity3.this, FangDaoGuideActivity4.class);
				finish();
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
				//表示向右滑动
			else if(e2.getX()-e1.getX()>MIN_DISTANCE&&Math.abs(velocityX)>MIN_VELOCITY){
				intent.setClass(FangDaoGuideActivity3.this, FangDaoGuideActivity2.class);
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
