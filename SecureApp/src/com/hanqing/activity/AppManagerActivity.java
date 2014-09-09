package com.hanqing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.secureapp.R;
import com.hanqing.domain.AppInfo;
import com.hanqing.service.AppInfoProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Ӧ�ó����������Activity
 * @author baohanqing
 *
 */
public class AppManagerActivity extends Activity implements OnClickListener{

	//Ӧ�ó�����Ϣ�б�
	private ListView appInfoListView;
	//Ӧ�ó�����صĽ�����
	private LinearLayout appManagerProgress;
	
	private AppInfoProvider infoProvider;
	
	private List<AppInfo> appList;
	
	private PopupWindow popupWindow;
	
	// ��ʾ���е�Ӧ�ó������Ϣ�����Ѿ�������ɣ���ʱ֪ͨhander���ؼ�װ������
	private static final int GET_ALL_APP_FINISH=1;
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			//��ʾ���е�Ӧ�ö��Ѿ���װ����ˣ���ʱ��Ҫ���ؽ�����
			appManagerProgress.setVisibility(View.GONE);
			
			switch (msg.what) {
			case GET_ALL_APP_FINISH:

				//��ʾListView
				appInfoListView.setAdapter(new AppManagerAdapter(appList));
				break;
				
			default:
				break;
			}
			
			
			
		}
	};
	
	
	//��ȡ�û�app
	private List<AppInfo> getUserApp(){
		
		List<AppInfo> userApp=new ArrayList<AppInfo>();
		
		for(int i=0;i<appList.size();i++){
			
			if(!appList.get(i).isSystemApp()){
				userApp.add(appList.get(i));
			}
			
		}
		
		return appList;
		
	}
	
	
	
	//ɾ��֮��ͨ�����������������
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		initUI();
	}

	//ж�������֮����½���
	private void initUI(){
		
		
		appManagerProgress.setVisibility(View.VISIBLE);
		
		//�ֻ�������Ӧ�÷ǳ���ʱ��������ǿ���һ���µ��߳�ȥ����
		//�������֮�󣬰���Ϣ���͸�Handler
		new Thread(){

			@Override
			public void run() {
				
				infoProvider=new AppInfoProvider(AppManagerActivity.this);
				appList=infoProvider.getAllAppsInfo();
				
				//��ȡ���е�Ӧ�û���ֻ��ȡϵͳ��Ӧ��
				Message msg=new Message();
				
				msg.what=GET_ALL_APP_FINISH;
				handler.sendMessage(msg);
				
			}
			
		}.start();
		
		
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_manager_main);
		
		appInfoListView=(ListView) super.findViewById(R.id.app_info_list);
		appManagerProgress=(LinearLayout) super.findViewById(R.id.ll_app_manager_progress);
		
		//������ʱ�򣬽���������Ϊ�ɼ�
		appManagerProgress.setVisibility(View.VISIBLE);
		
		//�����ֻ��ڵ�Ӧ���Ƿǳ���ʹ�ģ����Կ���һ���µ��߳̽����������������֮���һ����Ϣ���͸�handlerȻ�������д�뵽listview
		new Thread(){

			@Override
			public void run() {
				
				infoProvider=new AppInfoProvider(AppManagerActivity.this);
				//��ȡ����Ӧ�ó������Ϣ��������list����
				appList=infoProvider.getAllAppsInfo();
				
				//֪ͨHandler���ؼ�װ����
				Message msg=new Message();
				msg.what=GET_ALL_APP_FINISH;
				handler.sendMessage(msg);
				
			}
		}.start();
		
		
		//ListView����ĳһ��item�������ʱ��ķ���
		appInfoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				//��ȡ��ǰview��item������ֵ����һ����x�����꣬�ڶ�����y������
				//�������item������ֵ����һ����x�����꣬�ڶ�����y������
				int[] location=new int[2];
				//��ȡλ�ã�����λ�÷ŵ����鵱��
				view.getLocationInWindow(location);
				
				View popupView=View.inflate(AppManagerActivity.this, R.layout.popup_item, null);
				//��ȡPopup���еĵ����˵���ѡ������
				LinearLayout ll_uninstall=(LinearLayout) popupView.findViewById(R.id.ll_uninstall);
				LinearLayout ll_run=(LinearLayout) popupView.findViewById(R.id.ll_run);
				LinearLayout ll_share=(LinearLayout) popupView.findViewById(R.id.ll_share);
				
				//Ϊ�˵�ѡ�����ݵ�ҵ���¼�
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				ll_run.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);
				
				//�õ������Ŀ
				AppInfo app=(AppInfo) appInfoListView.getItemAtPosition(position);
				ll_uninstall.setTag(app);
				ll_run.setTag(app);
				ll_share.setTag(app);
				
				//��Ӷ���
				LinearLayout ll_app_popup=(LinearLayout) popupView.findViewById(R.id.ll_popup_item);
				ScaleAnimation scaleAnimation=new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				scaleAnimation.setDuration(300);
				
				//newһ��PopupWindow����
				popupWindow=new PopupWindow(popupView, 230,60);
				//��popupWindow����һ������ͼƬ
				
				//���û������Ҫ���õ�ͼƬ����ô�͸�����һ��͸���ı���ͼƬ
				Drawable drawable=new ColorDrawable(Color.GRAY);
				popupWindow.setBackgroundDrawable(drawable);
				
				int x=location[0]+60;
				int y=location[1]+30;
				
				//��popupWindow��ʾ����
				popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, x, y);
				
				//��������
				ll_app_popup.startAnimation(scaleAnimation);
				
			}
			
		});
		
		
		appInfoListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
				dismissionPopupWindow();
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				dismissionPopupWindow();
			}
		});
		
	}

	//ListView��Adapter��
	private class AppManagerAdapter extends BaseAdapter{

		private List<AppInfo> appInfos;
		
		public AppManagerAdapter(List<AppInfo> appInfos){
			this.appInfos=appInfos;
		}
		
		
		
		@Override
		public int getCount() {
			
			return appList.size();
		}

		@Override
		public Object getItem(int position) {
			
			return appList.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			AppInfo appInfo=appInfos.get(position);
			
			if(convertView==null){
				View view=View.inflate(AppManagerActivity.this, R.layout.app_info_item, null);
				
				AppManagerViews views=new AppManagerViews();
				
				views.iv_appIcon=(ImageView) view.findViewById(R.id.img_app_item);
				views.tv_app_name=(TextView) view.findViewById(R.id.tv_app_name_item);
				
				views.iv_appIcon.setImageDrawable(appInfo.getIcon());
				views.tv_app_name.setText(appInfo.getAppName());
				
				view.setTag(views);
				return view;
				
			}
			else{
				
				AppManagerViews views=(AppManagerViews) convertView.getTag();
				
				views.iv_appIcon.setImageDrawable(appInfo.getIcon());
				views.tv_app_name.setText(appInfo.getAppName());
				
				return convertView;
				
			}

		}
		
		
	}
	
	private class AppManagerViews{
		ImageView iv_appIcon;
		TextView tv_app_name;
	}
	
	

	//��popupWindow��ʧ
	private void dismissionPopupWindow(){
		
		if(popupWindow!=null){
			popupWindow.dismiss();
			popupWindow=null;
		}
		
	}
	
	
	//popup �˵��ĵ���¼�
	@Override
	public void onClick(View v) {
		
		AppInfo appInformation=(AppInfo) v.getTag();
		
		switch(v.getId()){
			
			//ж��------------------------------------------------------------------------------------------------
		   case R.id.ll_uninstall:
			   if(appInformation.isSystemApp()){
				   
				   Toast.makeText(AppManagerActivity.this, "����ж��ϵͳӦ��", Toast.LENGTH_LONG).show();
				   
			   }
			   else{
				   
				   String strUri="package:"+appInformation.getPackageName();
				   Uri uri=Uri.parse(strUri);
				   
				   Intent deleteIntent=new Intent();
				   deleteIntent.setAction(Intent.ACTION_DELETE);
				   deleteIntent.setData(uri);
				   
				   startActivityForResult(deleteIntent,0);
				   
			   }
			   
			   break;
			   
			//����
			case R.id.ll_run:
				
			try {
				PackageInfo packageInfo=getPackageManager().getPackageInfo(appInformation.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
				
				//ɨ�����е�activity�ڵ����Ϣ
				ActivityInfo[] activityInfos=packageInfo.activities;
				
				if(activityInfos!=null&&activityInfos.length>0){
					
					ActivityInfo statrActivity=activityInfos[0];
					
					Intent intent=new Intent();
					intent.setClassName(appInformation.getPackageName(), statrActivity.name);
					startActivity(intent);

				}
				else{
					Toast.makeText(AppManagerActivity.this, "���Ӧ���޷�����", Toast.LENGTH_LONG).show();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("���г���ʧ��");
				
			}
				
				
				
				break;
				
			 //����	------------------------------------------------------------------------------------------------------
			case R.id.ll_share:
				
				Intent shareIntent=new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				
				shareIntent.setType("text/plain");
				
				//���÷�������
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "����");
				
				//���÷�����ı�
				shareIntent.putExtra(Intent.EXTRA_TEXT, "��һ���ܺõ�Ӧ��"+appInformation.getAppName());
				
				shareIntent=Intent.createChooser(shareIntent, "����Ӧ��");
				
				startActivity(shareIntent);
				
				break;
			
		
		   default:
			   break;
				
		}
		
		dismissionPopupWindow();
	}
	
	
}
