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
 * 应用程序管理界面的Activity
 * @author baohanqing
 *
 */
public class AppManagerActivity extends Activity implements OnClickListener{

	//应用程序信息列表
	private ListView appInfoListView;
	//应用程序加载的进度条
	private LinearLayout appManagerProgress;
	
	private AppInfoProvider infoProvider;
	
	private List<AppInfo> appList;
	
	private PopupWindow popupWindow;
	
	// 表示所有的应用程序的信息，都已经加载完成，此时通知hander隐藏加装进度条
	private static final int GET_ALL_APP_FINISH=1;
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			//表示所有的应用都已经加装完成了，此时需要隐藏进度条
			appManagerProgress.setVisibility(View.GONE);
			
			switch (msg.what) {
			case GET_ALL_APP_FINISH:

				//显示ListView
				appInfoListView.setAdapter(new AppManagerAdapter(appList));
				break;
				
			default:
				break;
			}
			
			
			
		}
	};
	
	
	//获取用户app
	private List<AppInfo> getUserApp(){
		
		List<AppInfo> userApp=new ArrayList<AppInfo>();
		
		for(int i=0;i<appList.size();i++){
			
			if(!appList.get(i).isSystemApp()){
				userApp.add(appList.get(i));
			}
			
		}
		
		return appList;
		
	}
	
	
	
	//删除之后，通过这个方法更新数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		initUI();
	}

	//卸载完程序之后更新界面
	private void initUI(){
		
		
		appManagerProgress.setVisibility(View.VISIBLE);
		
		//手机里搜索应用非常耗时，因此我们开启一个新的线程去搜索
		//搜索完成之后，吧消息发送给Handler
		new Thread(){

			@Override
			public void run() {
				
				infoProvider=new AppInfoProvider(AppManagerActivity.this);
				appList=infoProvider.getAllAppsInfo();
				
				//获取所有的应用还是只获取系统的应用
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
		
		//进来的时候，将进度先设为可见
		appManagerProgress.setVisibility(View.VISIBLE);
		
		//搜索手机内的应用是非常好使的，所以开启一个新的线程进行搜索，搜索完成之后把一个消息发送给handler然后把数据写入到listview
		new Thread(){

			@Override
			public void run() {
				
				infoProvider=new AppInfoProvider(AppManagerActivity.this);
				//获取所用应用程序的信息，保存在list当中
				appList=infoProvider.getAllAppsInfo();
				
				//通知Handler隐藏加装进度
				Message msg=new Message();
				msg.what=GET_ALL_APP_FINISH;
				handler.sendMessage(msg);
				
			}
		}.start();
		
		
		//ListView当中某一个item被点击的时候的方法
		appInfoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				//获取当前view的item的坐标值，第一个是x的坐标，第二个是y的坐标
				//用来存放item的坐标值，第一个是x的坐标，第二个是y的坐标
				int[] location=new int[2];
				//获取位置，并把位置放到数组当中
				view.getLocationInWindow(location);
				
				View popupView=View.inflate(AppManagerActivity.this, R.layout.popup_item, null);
				//获取Popup当中的弹出菜单的选项内容
				LinearLayout ll_uninstall=(LinearLayout) popupView.findViewById(R.id.ll_uninstall);
				LinearLayout ll_run=(LinearLayout) popupView.findViewById(R.id.ll_run);
				LinearLayout ll_share=(LinearLayout) popupView.findViewById(R.id.ll_share);
				
				//为菜单选项内容店家点击事件
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				ll_run.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);
				
				//拿到点击条目
				AppInfo app=(AppInfo) appInfoListView.getItemAtPosition(position);
				ll_uninstall.setTag(app);
				ll_run.setTag(app);
				ll_share.setTag(app);
				
				//添加动画
				LinearLayout ll_app_popup=(LinearLayout) popupView.findViewById(R.id.ll_popup_item);
				ScaleAnimation scaleAnimation=new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				scaleAnimation.setDuration(300);
				
				//new一个PopupWindow出来
				popupWindow=new PopupWindow(popupView, 230,60);
				//给popupWindow设置一个背景图片
				
				//如果没有我们要设置的图片，那么就给他加一个透明的背景图片
				Drawable drawable=new ColorDrawable(Color.GRAY);
				popupWindow.setBackgroundDrawable(drawable);
				
				int x=location[0]+60;
				int y=location[1]+30;
				
				//把popupWindow显示出来
				popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, x, y);
				
				//开启动画
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

	//ListView的Adapter类
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
	
	

	//让popupWindow消失
	private void dismissionPopupWindow(){
		
		if(popupWindow!=null){
			popupWindow.dismiss();
			popupWindow=null;
		}
		
	}
	
	
	//popup 菜单的点击事件
	@Override
	public void onClick(View v) {
		
		AppInfo appInformation=(AppInfo) v.getTag();
		
		switch(v.getId()){
			
			//卸载------------------------------------------------------------------------------------------------
		   case R.id.ll_uninstall:
			   if(appInformation.isSystemApp()){
				   
				   Toast.makeText(AppManagerActivity.this, "不能卸载系统应用", Toast.LENGTH_LONG).show();
				   
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
			   
			//运行
			case R.id.ll_run:
				
			try {
				PackageInfo packageInfo=getPackageManager().getPackageInfo(appInformation.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
				
				//扫描所有的activity节点的信息
				ActivityInfo[] activityInfos=packageInfo.activities;
				
				if(activityInfos!=null&&activityInfos.length>0){
					
					ActivityInfo statrActivity=activityInfos[0];
					
					Intent intent=new Intent();
					intent.setClassName(appInformation.getPackageName(), statrActivity.name);
					startActivity(intent);

				}
				else{
					Toast.makeText(AppManagerActivity.this, "这个应用无法启动", Toast.LENGTH_LONG).show();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("运行程序失败");
				
			}
				
				
				
				break;
				
			 //分享	------------------------------------------------------------------------------------------------------
			case R.id.ll_share:
				
				Intent shareIntent=new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				
				shareIntent.setType("text/plain");
				
				//设置分享主题
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				
				//设置分享的文本
				shareIntent.putExtra(Intent.EXTRA_TEXT, "有一个很好的应用"+appInformation.getAppName());
				
				shareIntent=Intent.createChooser(shareIntent, "分享应用");
				
				startActivity(shareIntent);
				
				break;
			
		
		   default:
			   break;
				
		}
		
		dismissionPopupWindow();
	}
	
	
}
