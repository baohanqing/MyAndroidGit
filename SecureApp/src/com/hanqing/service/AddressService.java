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
	//根据两者的差值，来判断是不是响一声电话
	
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

	
	//挂断电话
	private void endCall(){
		
		try {
			
			Log.e("挂断电话", "被调用");
			
			
			//通过反射拿到android.os.ServiceManager里边的getService方法
			Method method=Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
			//通过反射调用getService方法，然后拿到IBinder对象，然后就可以进行aidl拉
			IBinder ibinder=(IBinder) method.invoke(null, new Object[]{TELEPHONY_SERVICE});
			ITelephony iTelephony=ITelephony.Stub.asInterface(ibinder);
			iTelephony.endCall();
			
		} catch (Exception e) {
			e.printStackTrace();
			//挂断电话操作
			System.out.println("调用挂断电话方法，出现异常！");
		}
	}
	

	//显示归属地的窗体
	private void showLocation(String address){
		
		WindowManager.LayoutParams params=new WindowManager.LayoutParams();
		params.width=WindowManager.LayoutParams.WRAP_CONTENT;
		params.height=WindowManager.LayoutParams.WRAP_CONTENT;
		
		params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE //无法获取焦点
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE //无法点击
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON ;//保持屏幕亮
				
		params.format=PixelFormat.TRANSLUCENT;//设置成半透明
		params.type=WindowManager.LayoutParams.TYPE_TOAST;
		
		params.setTitle("Toast");
		
		params.gravity=Gravity.LEFT|Gravity.TOP;
		
		params.x=sp.getInt("lastX", 0);
		params.y=sp.getInt("lastY", 0);
		
		Log.e("x   y", params.x+"--"+params.y);
		
		view=View.inflate(getApplicationContext(), R.layout.show_location, null);
		
		LinearLayout ll=(LinearLayout) view.findViewById(R.id.ll_show_location);
		
		//获取归属地显示的风格类型
		int type=sp.getInt("background", 0);
		
		//String[] items=new String[]{"半透明","活力橙","苹果绿","孔雀蓝","金属灰"};
		
		switch(type){
			
			case 0: 
				//半透明
				ll.getBackground().setAlpha(100);
				break;
			case 1:
				//活力橙
				ll.setBackgroundColor(Color.YELLOW);
				break;
			case 2:
				//苹果绿
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
		tv.setText("归属地:"+address);
		
		windowManager.addView(view, params);
	}
	
	//在通知栏显示响一声的电话通知
	private void showNotification(String incomingNumber){
		
		Log.e("显示通知栏", "十点");
		
		//拿到Notification的管理者
		NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		//new 一个Notification出来
		Notification notification=new Notification(R.drawable.ic_launcher,"发现响一声电话",System.currentTimeMillis());
		
		Context context=getApplicationContext();
		
		//设置成一点击就消失
		notification.flags=Notification.FLAG_AUTO_CANCEL;
		//点击该Notification跳转到黑名单界面
		Intent notificationIntent=new Intent(context,BlackNumberActivity.class);
		//并将号码传递过去
		notificationIntent.putExtra("number", incomingNumber);
		
		PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, notificationIntent, 0);
		
		notification.setLatestEventInfo(context, "响一声号码", incomingNumber, pendingIntent);
		
		//激活Notification
		notificationManager.notify(0,notification);
		
	}
	
	//监听电话
	private class MyPhoneListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			
			super.onCallStateChanged(state, incomingNumber);
			
			switch(state){
				
				case TelephonyManager.CALL_STATE_IDLE:
					//空闲状态，不显示归属地的状态
					
					end=System.currentTimeMillis();
					
					Log.e("显示通知栏end---start", "end-start="+(end-start));
					
					if(end>start&&(end-start)<200000){
						//表示响一声电话
						start=end=0;
						//在通知栏显示消息
						showNotification(incomingNumber);
					}
					
					if(tv!=null){
						
						windowManager.removeView(view);
						tv=null;
						
					}
					break;
					
				case TelephonyManager.CALL_STATE_OFFHOOK:
					//接通电话
					if(tv!=null){
						
						windowManager.removeView(view);
						tv=null;
					}
					
					
					break;
					
				case TelephonyManager.CALL_STATE_RINGING:
					
					start=System.currentTimeMillis();
					
					//如果该号码在该黑名单当中，自动挂断电话
					if(numDao.find(incomingNumber)){
						
						Log.e("是否在黑名单当中", "是的");
						
						endCall();
					}
					
					//玲响状态，显示归属地
					String address=NumberAddressService.getAddress(incomingNumber);
					showLocation(address);
					break;
					
				default:
					break;
			}
			
		}
		
	}
	
	
}






















