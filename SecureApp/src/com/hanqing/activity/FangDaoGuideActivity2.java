package com.hanqing.activity;

import com.example.secureapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 防盗部分的第二个引导页
 * @author baohanqing
 *
 */
public class FangDaoGuideActivity2 extends Activity implements OnTouchListener,OnGestureListener{

	GestureDetector gesture;
	LinearLayout ll;
	Button button;
	SharedPreferences preferences;
	
	
	public static final int MIN_DISTANCE=50;
	public static final int MIN_VELOCITY=0;
	
	//是否已经绑定了防盗
	TextView isBinded;
	
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fangdao_guide2);
		
		isBinded=(TextView) super.findViewById(R.id.isBinded);
		button=(Button) super.findViewById(R.id.BingBut);
		
		gesture=new GestureDetector(this);
		ll=(LinearLayout) super.findViewById(R.id.fangdao2_ll);
		ll.setOnTouchListener(this);
		ll.setClickable(true);
		
		intent=new Intent();
		
		preferences=this.getSharedPreferences("config",Context.MODE_PRIVATE);
		
		String isBindedStr=preferences.getString("sim_serial", null);
		
		if(isBindedStr!=null){
			//表示已经绑定了
			isBinded.setText("已绑定");
			button.setText("解除绑定");
		}
		else{
			//未绑定
			isBinded.setText("未绑定");
			button.setText("立即绑定");
		}
		
		//点击按钮，绑定，解除绑定
		button.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				//如果没有绑定，点击“立即绑定”按钮，进行绑定
				if(button.getText().equals("立即绑定")){
					if(setSimInfo()){
						isBinded.setText("已绑定");
						button.setText("解除绑定");
						Toast.makeText(FangDaoGuideActivity2.this, "绑定成功", Toast.LENGTH_LONG).show();
					}
					else{
						Toast.makeText(FangDaoGuideActivity2.this, "检测不到手机SIM卡！", Toast.LENGTH_LONG).show();
					}
				}
				else if(button.getText().equals("解除绑定")){
					//解除绑定的操作
					Editor edit=preferences.edit();
					edit.remove("simSerial");
					edit.commit();
					
					isBinded.setText("未绑定");
					button.setText("立即绑定");
					Toast.makeText(FangDaoGuideActivity2.this, "解除绑定成功", Toast.LENGTH_LONG).show();
				}
						
			}
		});
				
		
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
			intent.setClass(FangDaoGuideActivity2.this, FangDaoGuideActivity3.class);
			finish();
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		//表示向右滑动
		else if(e2.getX()-e1.getX()>MIN_DISTANCE&&Math.abs(velocityX)>MIN_VELOCITY){
			intent.setClass(FangDaoGuideActivity2.this, FangDaoGuideActivity1.class);
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

	//绑定Sim卡的方法
	private boolean setSimInfo(){
		
		boolean isOk=true;
		
		String simSerial = "";
		
		try {
			TelephonyManager telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			
			//获取sim卡的序列号，是唯一的
			simSerial=telephonyManager.getSimSerialNumber();
			//如果为Null，表示没有手机卡
			
			Log.e("hehe", simSerial+"haha");
			
			if(simSerial==null){
				isOk=false;
			}
			else{
				//将该序列号写入到SharedPreferences
				Editor edit=preferences.edit();
				edit.putString("sim_serial", simSerial);
				edit.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}
}
