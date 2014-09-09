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
 * �ó���Ϊ���̹����б����ṩ���ݵĲ���
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
	
	
	//��ȡ������������Ӧ�õ�������Ϣ
	public List<TaskInfo> getAllTask(List<RunningAppProcessInfo>  runningAppProcessInfo){
		
		List<TaskInfo> taskInfoList=new ArrayList<TaskInfo>();
		
		for(int i=0;i<runningAppProcessInfo.size();i++){
			
			RunningAppProcessInfo runningApp=runningAppProcessInfo.get(i);
			
			TaskInfo taskInfo=new TaskInfo();
			
			//��ȡ������Id�Ͱ���
			int pid=runningApp.pid;
			String packageName=runningApp.processName;
			
			taskInfo.setId(pid);
			taskInfo.setPackageName(packageName);
			
			//���ݰ�������ȡӦ�ó���ĸ�Ϊ��ϸ����Ϣ
			
			try {
				
				//���ݰ�����ȡ�����Ϊ��ϸ����Ϣ
				ApplicationInfo appInfo=packageManager.getPackageInfo(packageName, 0).applicationInfo;
				
				//Ӧ��ͼ��
				Drawable icon=appInfo.loadIcon(packageManager);
				taskInfo.setIcon(icon);
				
				//Ӧ������
				String appName=appInfo.loadLabel(packageManager).toString();
				taskInfo.setName(appName);
				
				//�ǲ���ϵͳӦ��
				taskInfo.setSysApp(isSysApp(appInfo));
				
				//����pid��ȡ����ʹ�õ��ڴ���Ϣ,����ȥ����id��Ϣ���ͻ᷵�ض��ٸ���Ӧ��id�ڴ���Ϣ
				MemoryInfo[] memoryInfos=activityManager.getProcessMemoryInfo(new int[] { pid });
				
				//�õ�ռ���ڴ�ռ�
				int memory=memoryInfos[0].getTotalPrivateDirty();
				taskInfo.setMemory(memory);
				
				taskInfoList.add(taskInfo);
				
				taskInfo=null;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		//��������Ӧ�õ������б�
		return taskInfoList;

	}
	
	//�ж�һ��Ӧ���ǲ���ϵͳӦ�ã��ǣ�����true
	public boolean isSysApp(ApplicationInfo app){
		
		if((app.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
			
			return true;
			
		}
		
		return false;
	}
	
}
