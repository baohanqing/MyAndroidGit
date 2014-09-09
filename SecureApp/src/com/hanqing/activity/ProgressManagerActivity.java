package com.hanqing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.secureapp.R;
import com.hanqing.domain.TaskInfo;
import com.hanqing.service.TaskInfoProvider;
import com.hanqing.util.TextFomater;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressManagerActivity extends Activity{

	private TextView progressCount;
	private TextView progressMemory;
	private ProgressBar loadingProgress;
	
	public static final int LOAD_FINISH=0;
	
	private ListView taskListView;
	
	private ActivityManager activityManager;
	
	private List<RunningAppProcessInfo> runningAppProcessInfos;
	
	private TaskInfoProvider taskInfoProvider;
	
	private  List<TaskInfo> taskInfoList;
	
	private ListAdapter adapter;
	
	//一个Handler，用于加载完之后隐藏进度条
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what){
				
				case LOAD_FINISH:
					loadingProgress.setVisibility(View.GONE);
					adapter=new ListAdapter();
					taskListView.setAdapter(adapter);
					break;
				
				default:
					break;
			
			}
			
			
			
			
		}

	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_manager);
		
		progressCount=(TextView) super.findViewById(R.id.tv_progress_count);
		progressMemory=(TextView) super.findViewById(R.id.tv_progress_memory);
		loadingProgress=(ProgressBar) super.findViewById(R.id.loading_progress);
		
		taskListView=(ListView) super.findViewById(R.id.lv_task_list);
		
		activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		
		progressCount.setText("进程数："+getRunningAppCount());
		progressMemory.setText("剩余内存："+getAvailMemory());
		
		
		initData();
		
	}

	
	//拿到当前运行的进程数目
	private int getRunningAppCount(){
		
		runningAppProcessInfos=activityManager.getRunningAppProcesses();
		return runningAppProcessInfos.size();
		
	}
	

	//拿到手机剩余的内存
	private String getAvailMemory(){
		
		//new一个内存对象
		MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
		//拿到现在系统里的内存信息
		activityManager.getMemoryInfo(memoryInfo);
		
		//拿到有效的内存空间
		long size=memoryInfo.availMem;
		
		return TextFomater.dataSizeFormat(size);
		
	}
	
	//第一次初始化ListView列表
	private void initData(){
		
		//加载完之后通知Handler隐藏progressbar
		loadingProgress.setVisibility(View.VISIBLE);
		
		new Thread(){

			@Override
			public void run() {
				
				taskInfoProvider=new TaskInfoProvider(ProgressManagerActivity.this);
				taskInfoList=taskInfoProvider.getAllTask(runningAppProcessInfos);
				
				//通知Handler隐藏进度条
				Message msg=new Message();
				msg.what=LOAD_FINISH;
				handler.sendMessage(msg);
				
			}

		}.start();
		
		
	}
	
	
	private class ListAdapter extends BaseAdapter{

		//定义两个List，一个用来存放用户应用的List，一个用来存放系统应用的List
		private List<TaskInfo> userTaskInfoList;
		private List<TaskInfo> sysTaskInfoList;
		
		//将我们的应用根据用户应用还是系统应用放到上边的两个List当中
		public ListAdapter(){
			
			userTaskInfoList=new ArrayList<TaskInfo>();
			sysTaskInfoList=new ArrayList<TaskInfo>();
			
			for(int i=0;i<taskInfoList.size();i++){
				
				TaskInfo taskInfo=taskInfoList.get(i);
				
				if(taskInfo.isSysApp()){
					sysTaskInfoList.add(taskInfo);
				}
				else{
					userTaskInfoList.add(taskInfo);
				}
			}
		}
		
		@Override
		public int getCount() {
			// 加上两个代表用户应用还是系统应用的标签
			return taskInfoList.size()+2;
		}

		@Override
		public Object getItem(int position) {
			
			if(position==0){
				return 0;//显示用户应用的标签
			}
			else if(position<=userTaskInfoList.size()){
				//显示用户应用
				return userTaskInfoList.get(position-1);
			}
			else if(position==userTaskInfoList.size()+1){
				//显示系统标签
				return position;	
			}
			else if(position<=taskInfoList.size()+2){
				//显示系统应用
				return sysTaskInfoList.get(position-userTaskInfoList.size()-2);
			}
			else{
				return position;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view;
			TaskInfo taskInfo;
			
			
			TaskInfoViews views;
			
			
			if(position==0){
				
				return newTextView("用户进程("+userTaskInfoList.size()+")");
				
			}
			else if(position<=userTaskInfoList.size()){
				
				taskInfo=taskInfoList.get(position);
				
			}
			else if(position==userTaskInfoList.size()+1){
				
				return newTextView("系统进程("+sysTaskInfoList.size()+")");
				
			}
			else if(position<=taskInfoList.size()+2){
				
				taskInfo=sysTaskInfoList.get(position-userTaskInfoList.size()-2);
				
			}
			else{
				
				taskInfo=new TaskInfo();
				
			}
			
			
			if(convertView==null || convertView instanceof TextView){
				
				view=View.inflate(ProgressManagerActivity.this, R.layout.progress_manager_item, null);
				
				views=new TaskInfoViews();
				
				views.iv_app_icon=(ImageView) view.findViewById(R.id.iv_app_icon);
				views.tv_app_name=(TextView) view.findViewById(R.id.tv_app_name);
				views.tv_app_memory=(TextView) view.findViewById(R.id.tv_app_memory);
				
				views.cb_item_checked=(CheckBox) view.findViewById(R.id.cb_ischecked);
				
				view.setTag(views);
			}
			else{
				
				view=convertView;
				views=(TaskInfoViews) view.getTag();
				
			}
			
			views.iv_app_icon.setImageDrawable(taskInfo.getIcon());
			views.tv_app_name.setText(taskInfo.getName());
			views.tv_app_memory.setText(taskInfo.getMemory()+"");
			views.cb_item_checked.setChecked(taskInfo.isChecked());

			return view;
		}

	}
	
	//创建TextView的方法
	private TextView newTextView(String text){
		
		TextView tv=new TextView(ProgressManagerActivity.this);
		tv.setText(text);
		
		return tv;
		
	}
	
	private class TaskInfoViews{
		
		ImageView iv_app_icon;
		TextView tv_app_name;
		TextView tv_app_memory;
		CheckBox cb_item_checked;
		
	}
	
}
