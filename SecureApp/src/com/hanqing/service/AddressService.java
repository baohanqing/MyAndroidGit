package com.hanqing.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.secureapp.R;
import com.hanqing.activity.BlackNumberActivity;
import com.hanqing.dao.BlackNumberDao;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddressService extends Service{

	private TelephonyManager telephonyManager;
	private WindowManager windowManager;
	private TextView tv;
	
	private SharedPreferences sp;
	
	private MyPhoneListener listener;
	
	private View view;
	
	private BlackNumberDao numDao;
	
	long start,end;
	//�������ߵĲ�ֵ�����ж��ǲ�����һ���绰
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		
		super.onCreate();
		
		windowManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
		listener=new MyPhoneListener();
		telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		
		numDao=new BlackNumberDao(AddressService.this);
		
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

	
	//�Ҷϵ绰
	private void endCall(){
		
		try {
			
			Log.e("�Ҷϵ绰", "������");
			
			
			//ͨ�������õ�android.os.ServiceManager��ߵ�getService����
			Method method=Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
			//ͨ���������getService������Ȼ���õ�IBinder����Ȼ��Ϳ��Խ���aidl��
			IBinder ibinder=(IBinder) method.invoke(null, new Object[]{TELEPHONY_SERVICE});
			ITelephony iTelephony=ITelephony.Stub.asInterface(ibinder);
			iTelephony.endCall();
			
		} catch (Exception e) {
			e.printStackTrace();
			//�Ҷϵ绰����
			System.out.println("���ùҶϵ绰�����������쳣��");
		}
	}
	

	//��ʾ�����صĴ���
	private void showLocation(String address){
		
		WindowManager.LayoutParams params=new WindowManager.LayoutParams();
		params.width=WindowManager.LayoutParams.WRAP_CONTENT;
		params.height=WindowManager.LayoutParams.WRAP_CONTENT;
		
		params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE //�޷���ȡ����
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE //�޷����
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON ;//������Ļ��
				
		params.format=PixelFormat.TRANSLUCENT;//���óɰ�͸��
		params.type=WindowManager.LayoutParams.TYPE_TOAST;
		
		params.setTitle("Toast");
		
		params.gravity=Gravity.LEFT|Gravity.TOP;
		
		params.x=sp.getInt("lastX", 0);
		params.y=sp.getInt("lastY", 0);
		
		Log.e("x   y", params.x+"--"+params.y);
		
		view=View.inflate(getApplicationContext(), R.layout.show_location, null);
		
		LinearLayout ll=(LinearLayout) view.findViewById(R.id.ll_show_location);
		
		//��ȡ��������ʾ�ķ������
		int type=sp.getInt("background", 0);
		
		//String[] items=new String[]{"��͸��","������","ƻ����","��ȸ��","������"};
		
		switch(type){
			
			case 0: 
				//��͸��
				ll.getBackground().setAlpha(100);
				break;
			case 1:
				//������
				ll.setBackgroundColor(Color.YELLOW);
				break;
			case 2:
				//ƻ����
				ll.setBackgroundColor(Color.GREEN);
				break;
			case 3:
				ll.setBackgroundColor(Color.BLUE);
				break;
			case 4:
				ll.setBackgroundColor(Color.GRAY);
				break;
			default:
				break;
		
		}
		
		
		
		tv=(TextView) view.findViewById(R.id.tv_location);
		tv.setText("������:"+address);
		
		windowManager.addView(view, params);
	}
	
	//��֪ͨ����ʾ��һ���ĵ绰֪ͨ
	private void showNotification(String incomingNumber){
		
		Log.e("��ʾ֪ͨ��", "ʮ��");
		
		//�õ�Notification�Ĺ�����
		NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		//new һ��Notification����
		Notification notification=new Notification(R.drawable.ic_launcher,"������һ���绰",System.currentTimeMillis());
		
		Context context=getApplicationContext();
		
		//���ó�һ�������ʧ
		notification.flags=Notification.FLAG_AUTO_CANCEL;
		//�����Notification��ת������������
		Intent notificationIntent=new Intent(context,BlackNumberActivity.class);
		//�������봫�ݹ�ȥ
		notificationIntent.putExtra("number", incomingNumber);
		
		PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, notificationIntent, 0);
		
		notification.setLatestEventInfo(context, "��һ������", incomingNumber, pendingIntent);
		
		//����Notification
		notificationManager.notify(0,notification);
		
	}
	
	//�����绰
	private class MyPhoneListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			
			super.onCallStateChanged(state, incomingNumber);
			
			switch(state){
				
				case TelephonyManager.CALL_STATE_IDLE:
					//����״̬������ʾ�����ص�״̬
					
					end=System.currentTimeMillis();
					
					Log.e("��ʾ֪ͨ��end---start", "end-start="+(end-start));
					
					if(end>start&&(end-start)<200000){
						//��ʾ��һ���绰
						start=end=0;
						//��֪ͨ����ʾ��Ϣ
						showNotification(incomingNumber);
					}
					
					if(tv!=null){
						
						windowManager.removeView(view);
						tv=null;
						
					}
					break;
					
				case TelephonyManager.CALL_STATE_OFFHOOK:
					//��ͨ�绰
					if(tv!=null){
						
						windowManager.removeView(view);
						tv=null;
					}
					
					
					break;
					
				case TelephonyManager.CALL_STATE_RINGING:
					
					start=System.currentTimeMillis();
					
					//����ú����ڸú��������У��Զ��Ҷϵ绰
					if(numDao.find(incomingNumber)){
						
						Log.e("�Ƿ��ں���������", "�ǵ�");
						
						endCall();
					}
					
					//����״̬����ʾ������
					String address=NumberAddressService.getAddress(incomingNumber);
					showLocation(address);
					break;
					
				default:
					break;
			}
			
		}
		
	}
	
	
}






















