package com.hanqing.activity;

import com.example.secureapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

/**
 * �ò���Ϊ�ֻ�����������ҳ��һҳ���󻬽��뵽����ҳ�ĵڶ�ҳ
 * @author baohanqing
 *
 */

public class FangDaoGuideActivity1 extends Activity implements OnTouchListener,OnGestureListener{

	GestureDetector gesture;
	LinearLayout ll;
	private static final int MIN_DISTANCE=50;
	private static final int MIN_VELOCITY=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fangdao_guide1);
	
		gesture=new GestureDetector(this);
		ll=(LinearLayout) super.findViewById(R.id.fangdao1_ll);
		ll.setOnTouchListener(this);
		ll.setClickable(true);
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
			if(e1.getX()-e2.getX()>MIN_DISTANCE&&Math.abs(velocityX)>MIN_VELOCITY){
				//��ʾ���󻬶������뵽�ڶ�������ҳ
				Intent intent=new Intent(FangDaoGuideActivity1.this,FangDaoGuideActivity2.class);
				finish();
				startActivity(intent);
				//�����л���ʱ��Ķ���
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
		
		
		
		
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return gesture.onTouchEvent(event);
	}

	
	
}
