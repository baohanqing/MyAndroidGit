package com.hanqing.service;

import java.util.List;

import com.hanqing.activity.LockActivityPwd;
import com.hanqing.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * �ó����������������Ǵ򿪵�Ӧ�ó����Ƿ����Ѿ��������ĳ�������ǵģ�����ת����������ҳ��
 * @author baohanqing
 *
 */
public class WatchLockAppService extends Service{

	private AppLockDao lockDao;
	
	private List<String> lockPackageNameList;
	
	private ActivityManager activityManager;
	
	private Intent intent;
	
	private boolean flag=true;
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	
	@Override
	public void onCreate() {
		
		super.onCreate();
		lockDao=new AppLockDao(WatchLockAppService.this);
		
		lockPackageNameList=lockDao.getAllLockAppList();
		
		activityManager=(ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
		
		intent=new Intent(WatchLockAppService.this,LockActivityPwd.class);
		
		//�������û������ջ������Ҫָ��һ���µ�����ջ����Ȼ���޷��ٷ���������Activity
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		new Thread(){

			@Override
			public void run() {
				
				while(flag){
					
					try {
						
						//�õ��������е�����ջ
						List<RunningTaskInfo> runningTaskInfos=activityManager.getRunningTasks(1);
						
						//�õ���ǰ������ջ
						RunningTaskInfo runningTaskInfo=runningTaskInfos.get(0);
						
						//�õ�Ҫ���е�Activity�İ���
						String packageName=runningTaskInfo.topActivity.getPackageName();
						
						Log.e("packageName", packageName);
						
						//����ð������������ǵĳ������б��еĻ�������ת��
						if(lockPackageNameList.contains(packageName)){
							//�Ѱ�������ӵ�Intent����Ȼ����ת�������������
							intent.putExtra("packageName", packageName);
							startActivity(intent);
						}
						
						sleep(1000);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
		}.start();
		
	}
	

	@Override
	public void onDestroy() {
		
		super.onDestroy();
		flag=false;
		
	}
	
	
	

}
