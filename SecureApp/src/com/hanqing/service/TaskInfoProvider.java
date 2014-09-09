package com.hanqing.service;

import java.util.ArrayList;
import java.util.List;

import com.hanqing.domain.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

/**
 * 该程序为进程管理列表项提供内容的部分
 * @author baohanqing
 *
 */
public class TaskInfoProvider {

	private PackageManager packageManager;
	private ActivityManager activityManager;
	
	public TaskInfoProvider(Context context){
		
		packageManager=context.getPackageManager();
		activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
	}
	
	
	//获取所有正在运行应用的任务信息
	public List<TaskInfo> getAllTask(List<RunningAppProcessInfo>  runningAppProcessInfo){
		
		List<TaskInfo> taskInfoList=new ArrayList<TaskInfo>();
		
		for(int i=0;i<runningAppProcessInfo.size();i++){
			
			RunningAppProcessInfo runningApp=runningAppProcessInfo.get(i);
			
			TaskInfo taskInfo=new TaskInfo();
			
			//获取到进程Id和包名
			int pid=runningApp.pid;
			String packageName=runningApp.processName;
			
			taskInfo.setId(pid);
			taskInfo.setPackageName(packageName);
			
			//根据包名，获取应用程序的更为详细的信息
			
			try {
				
				//根据包名获取程序更为详细的信息
				ApplicationInfo appInfo=packageManager.getPackageInfo(packageName, 0).applicationInfo;
				
				//应用图标
				Drawable icon=appInfo.loadIcon(packageManager);
				taskInfo.setIcon(icon);
				
				//应用名字
				String appName=appInfo.loadLabel(packageManager).toString();
				taskInfo.setName(appName);
				
				//是不是系统应用
				taskInfo.setSysApp(isSysApp(appInfo));
				
				//根据pid获取程序使用的内存信息,传进去几个id信息，就会返回多少个对应的id内存信息
				MemoryInfo[] memoryInfos=activityManager.getProcessMemoryInfo(new int[] { pid });
				
				//拿到占用内存空间
				int memory=memoryInfos[0].getTotalPrivateDirty();
				taskInfo.setMemory(memory);
				
				taskInfoList.add(taskInfo);
				
				taskInfo=null;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		//返回所有应用的任务列表
		return taskInfoList;

	}
	
	//判断一个应用是不是系统应用，是，返回true
	public boolean isSysApp(ApplicationInfo app){
		
		if((app.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
			
			return true;
			
		}
		
		return false;
	}
	
}
