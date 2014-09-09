package com.hanqing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.secureapp.R;
import com.hanqing.dao.AppLockDao;
import com.hanqing.domain.AppInfo;
import com.hanqing.service.AppInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AppLockActivity extends Activity{

	private ListView app_lock_list_view;
	
	private AppLockAdapter lockAdapter;
	
	private List<AppInfo> appLockList;
	
	private AppInfoProvider infoProvider;
	
	//��ȡ����������
	private LinearLayout progress;
	
	private AppLockDao lockDao;
	
	List<String> packageNameList;
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			lockAdapter=new AppLockAdapter();
			app_lock_list_view.setAdapter(lockAdapter);
			
			//���ý�������ʧ
			progress.setVisibility(View.GONE);
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_lock);
		
		app_lock_list_view=(ListView) super.findViewById(R.id.app_lock_list_view);
		
		progress=(LinearLayout) super.findViewById(R.id.ll_progress);
		
		progress.setVisibility(View.VISIBLE);
		
		lockDao=new AppLockDao(AppLockActivity.this);
		
		
		
		/*
		 * ע�����䲻�ܷŵ������Ϊ����һ����ʱ��������һֱ�ŵ�����ߣ���Ҫ������һ���߳�
		 * appLockList=infoProvider.getAllAppsInfo();
		 */
		
		 getAppList();
		 
		 
		 //Ϊapp_lock_list_view���е�ÿ��item��ӵ���¼�
		 app_lock_list_view.setOnItemClickListener(new OnItemClickListener() {

			 
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				//��Ӷ���
				TranslateAnimation translateAnimation=new TranslateAnimation(
						 Animation.RELATIVE_TO_SELF, 0.0f, 
						 Animation.RELATIVE_TO_SELF, 0.3f, 
						 Animation.RELATIVE_TO_SELF, 0.0f, 
						 Animation.RELATIVE_TO_SELF, 0.0f);
				
				translateAnimation.setDuration(3000);
				view.startAnimation(translateAnimation);
				
				
				
				AppInfo info=appLockList.get(position);
				
				String packageName=info.getPackageName();
				
				//��ȡ����ͼƬ
				ImageView imageView=(ImageView) view.findViewById(R.id.iv_app_lock);
				
				if(lockDao.find(packageName)){
					//����Ѿ����뵽�����������ˣ����֮�󣬽���ӳ���������ɾ������������ͼƬ���ɽ�����
					lockDao.delete(packageName);
					//packageNameList.remove(packageName);
					imageView.setImageResource(R.drawable.app_unlock_img);
					
				}
				else{
					lockDao.add(packageName);
					//packageNameList.add(packageName);
					imageView.setImageResource(R.drawable.app_lock_img);
					
				}
			}
			
		});
		 
		 packageNameList=lockDao.getAllLockAppList();
		
	}

	//��ȡ����Ӧ�ó������Ϣ
	private List<AppInfo> getAppList(){
		
		new Thread(){

			@Override
			public void run() {
				infoProvider=new AppInfoProvider(AppLockActivity.this);
				appLockList=infoProvider.getAllAppsInfo();
				
				//������Ϣ����ʱ������List����Adapter��
				handler.sendEmptyMessage(0);
			}
		}.start();
		
		return appLockList;

	}
	
	
	//ΪListView�ṩ���ݵ�Adapter
	private class AppLockAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			return appLockList.size();
		}

		@Override
		public Object getItem(int position) {
			
			return appLockList.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			AppInfo appInfo=appLockList.get(position);
			
			if(convertView==null){
				
				View view=View.inflate(AppLockActivity.this, R.layout.app_lock_item, null);
				
				AppManagerViews am=new AppManagerViews();
				
				am.iv_app_lock_icon=(ImageView) view.findViewById(R.id.iv_app_lock_icon);
				am.tv_app_lock_name=(TextView) view.findViewById(R.id.tv_app_lock_name);
				am.iv_lock_img=(ImageView) view.findViewById(R.id.iv_app_lock);
				
				am.iv_app_lock_icon.setImageDrawable(appInfo.getIcon());
				am.tv_app_lock_name.setText(appInfo.getAppName());
				
				if(packageNameList.size()>0){
					//�ж�һ�£����ǵ�app�Ƿ��Ѿ�������
					if(packageNameList.contains(appInfo.getPackageName())){
						//�Ѿ�������
						am.iv_lock_img.setImageResource(R.drawable.app_lock_img);
					}
					else {
						//��δ����
						am.iv_lock_img.setImageResource(R.drawable.app_unlock_img);
					}
				}
				view.setTag(am);
				
				return view;
				
			}
			else{
				
				AppManagerViews am=(AppManagerViews) convertView.getTag();
				
				am.iv_app_lock_icon.setImageDrawable(appInfo.getIcon());
				am.tv_app_lock_name.setText(appInfo.getAppName());
				
				return convertView;
			}

		}
	
	}
	
	private class AppManagerViews{
		ImageView iv_app_lock_icon;
		TextView tv_app_lock_name;
		ImageView iv_lock_img;
	}
	
}
