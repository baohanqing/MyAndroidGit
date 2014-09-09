package com.hanqing.activity;

import com.example.secureapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DragViewActivity extends Activity implements OnTouchListener{

	ImageView iv_drag_location;
	SharedPreferences sp;
	
	//记录第一次触摸的坐标
	private int startX;
	private int startY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_view);
		
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		
		iv_drag_location=(ImageView) super.findViewById(R.id.iv_drag_location);
		iv_drag_location.setOnTouchListener(this);
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(v.getId()){
			
			case R.id.iv_drag_location:
				
				//选择事件
				switch(event.getAction()){
					
					case	MotionEvent.ACTION_DOWN:
						//按下手势开始的位置
						startX=(int) event.getRawX();
						startY=(int) event.getRawY();
						break;
						
					case MotionEvent.ACTION_MOVE:
						//移动的情况下
						int x=(int) event.getRawX();
						int y=(int) event.getRawY();
						
						//算出移动距离
						int dx=x-startX;
						int dy=y-startY;
						
						int l=iv_drag_location.getLeft();
						int r=iv_drag_location.getRight();
						int t=iv_drag_location.getTop();
						int b=iv_drag_location.getBottom();
						
						//设置新的布局位置
						iv_drag_location.layout(l+dx, t+dy, r+dx, b+dy);
						
						//重新获取位置
						startX=(int) event.getRawX();
						startY=(int) event.getRawY();
						
						break;
					
					case MotionEvent.ACTION_UP:
						
						int lastX=iv_drag_location.getLeft();
						int lastY=iv_drag_location.getTop();
						
						//将最后的坐标存到SharedPrefence当中
						
						Editor editor=sp.edit();
						editor.putInt("lastX", lastX);
						editor.putInt("lastY", lastY);
						editor.commit();
						
						break;
				
					default:
						break;
				
				}

				break;
				
			default:
				break;
		}
		
		return true;
	}
}
