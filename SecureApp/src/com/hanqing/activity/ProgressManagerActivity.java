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
	
	//һ��Handler�����ڼ�����֮�����ؽ�����
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
		
		progressCount.setText("��������"+getRunningAppCount());
		progressMemory.setText("ʣ���ڴ棺"+getAvailMemory());
		
		
		initData();
		
	}

	
	//�õ���ǰ���еĽ�����Ŀ
	private int getRunningAppCount(){
		
		runningAppProcessInfos=activityManager.getRunningAppProcesses();
		return runningAppProcessInfos.size();
		
	}
	

	//�õ��ֻ�ʣ����ڴ�
	private String getAvailMemory(){
		
		//newһ���ڴ����
		MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
		//�õ�����ϵͳ����ڴ���Ϣ
		activityManager.getMemoryInfo(memoryInfo);
		
		//�õ���Ч���ڴ�ռ�
		long size=memoryInfo.availMem;
		
		return TextFomater.dataSizeFormat(size);
		
	}
	
	//��һ�γ�ʼ��ListView�б�
	private void initData(){
		
		//������֮��֪ͨHandler����progressbar
		loadingProgress.setVisibility(View.VISIBLE);
		
		new Thread(){

			@Override
			public void run() {
				
				taskInfoProvider=new TaskInfoProvider(ProgressManagerActivity.this);
				taskInfoList=taskInfoProvider.getAllTask(runningAppProcessInfos);
				
				//֪ͨHandler���ؽ�����
				Message msg=new Message();
				msg.what=LOAD_FINISH;
				handler.sendMessage(msg);
				
			}

		}.start();
		
		
	}
	
	
	private class ListAdapter extends BaseAdapter{

		//��������List��һ����������û�Ӧ�õ�List��һ���������ϵͳӦ�õ�List
		private List<TaskInfo> userTaskInfoList;
		private List<TaskInfo> sysTaskInfoList;
		
		//�����ǵ�Ӧ�ø����û�Ӧ�û���ϵͳӦ�÷ŵ��ϱߵ�����List����
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
			// �������������û�Ӧ�û���ϵͳӦ�õı�ǩ
			return taskInfoList.size()+2;
		}

		@Override
		public Object getItem(int position) {
			
			if(position==0){
				return 0;//��ʾ�û�Ӧ�õı�ǩ
			}
			else if(position<=userTaskInfoList.size()){
				//��ʾ�û�Ӧ��
				return userTaskInfoList.get(position-1);
			}
			else if(position==userTaskInfoList.size()+1){
				//��ʾϵͳ��ǩ
				return position;	
			}
			else if(position<=taskInfoList.size()+2){
				//��ʾϵͳӦ��
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
				
				return newTextView("�û�����("+userTaskInfoList.size()+")");
				
			}
			else if(position<=userTaskInfoList.size()){
				
				taskInfo=taskInfoList.get(position);
				
			}
			else if(position==userTaskInfoList.size()+1){
				
				return newTextView("ϵͳ����("+sysTaskInfoList.size()+")");
				
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
	
	//����TextView�ķ���
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
