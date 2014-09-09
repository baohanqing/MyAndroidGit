package com.hanqing.service;

import java.util.ArrayList;
import java.util.List;

import com.hanqing.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * �����ȡ�����ֻ�Ӧ���ڵ�����Ӧ�ó������Ϣ
 * @author baohanqing
 *
 */
public class AppInfoProvider {

	private PackageManager packageMan;
	
	public AppInfoProvider(Context context){
		packageMan=context.getPackageManager();
	}
	
	//��ȡ�ֻ��ڰ�װӦ�õ�������Ϣ
	public List<AppInfo> getAllAppsInfo(){
		
		List<AppInfo> appInfoList=new ArrayList<AppInfo>();
		
		AppInfo appInfoObj;
		
		//��ȡ���а�װ�˵�Ӧ�ó������Ϣ��������Щж���˵�
		List<PackageInfo>allAppsList=packageMan.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		
		for(int i=0;i<allAppsList.size();i++){
			
			appInfoObj=new AppInfo();
			
			//�õ�����
			String packageName=allAppsList.get(i).packageName;
			//�õ�Ӧ�ó������Ϣ���Ա��ȡ��������ƺ�ͼ��
			ApplicationInfo appInfo=allAppsList.get(i).applicationInfo;
			
			//��ȡͼ��
			Drawable icon=appInfo.loadIcon(packageMan);
			//��ȡ���������
			String appName=appInfo.loadLabel(packageMan).toString();
			
			appInfoObj.setIcon(icon);
			appInfoObj.setPackageName(packageName);
			appInfoObj.setAppName(appName);
			
			if(isSysApp(appInfo)){
				appInfoObj.setSystemApp(true);
			}
			else{
				appInfoObj.setSystemApp(false);
			}
			
			appInfoList.add(appInfoObj);
			
		}
		
		return appInfoList;
		
	}
	
	
	//�ж�һ��Ӧ�ó����ǲ���ϵͳ��Ӧ�ó���
	public boolean isSysApp(ApplicationInfo info){
		
		//����û�������һ���µ�ϵͳӦ�ó��򣬸�����ԭ����
		if((info.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0){
			return true;
		}
		else if((info.flags&ApplicationInfo.FLAG_SYSTEM)==0){
			return true;
		}
		
		return false;
		
	}
	
	
}
