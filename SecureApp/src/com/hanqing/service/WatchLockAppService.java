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
 * 该程序用来监听，我们打开的应用程序是否是已经被锁定的程序，如果是的，将跳转到输入密码页面
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
		
		//服务里边没有任务栈，所以要指定一个新的任务栈，不然在无法再服务里启动Activity
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		new Thread(){

			@Override
			public void run() {
				
				while(flag){
					
					try {
						
						//得到最新运行的任务栈
						List<RunningTaskInfo> runningTaskInfos=activityManager.getRunningTasks(1);
						
						//拿到当前的任务栈
						RunningTaskInfo runningTaskInfo=runningTaskInfos.get(0);
						
						//拿到要运行的Activity的包名
						String packageName=runningTaskInfo.topActivity.getPackageName();
						
						Log.e("packageName", packageName);
						
						//如果该包名包含在我们的程序锁列表当中的话，就跳转到
						if(lockPackageNameList.contains(packageName)){
							//把包名，添加到Intent当中然后跳转到密码输入界面
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
