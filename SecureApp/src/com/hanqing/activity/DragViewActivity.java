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
	
	//��¼��һ�δ���������
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
				
				//ѡ���¼�
				switch(event.getAction()){
					
					case	MotionEvent.ACTION_DOWN:
						//�������ƿ�ʼ��λ��
						startX=(int) event.getRawX();
						startY=(int) event.getRawY();
						break;
						
					case MotionEvent.ACTION_MOVE:
						//�ƶ��������
						int x=(int) event.getRawX();
						int y=(int) event.getRawY();
						
						//����ƶ�����
						int dx=x-startX;
						int dy=y-startY;
						
						int l=iv_drag_location.getLeft();
						int r=iv_drag_location.getRight();
						int t=iv_drag_location.getTop();
						int b=iv_drag_location.getBottom();
						
						//�����µĲ���λ��
						iv_drag_location.layout(l+dx, t+dy, r+dx, b+dy);
						
						//���»�ȡλ��
						startX=(int) event.getRawX();
						startY=(int) event.getRawY();
						
						break;
					
					case MotionEvent.ACTION_UP:
						
						int lastX=iv_drag_location.getLeft();
						int lastY=iv_drag_location.getTop();
						
						//����������浽SharedPrefence����
						
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
