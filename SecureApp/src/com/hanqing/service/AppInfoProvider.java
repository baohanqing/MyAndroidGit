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
 * 该类获取我们手机应用内的所有应用程序的信息
 * @author baohanqing
 *
 */
public class AppInfoProvider {

	private PackageManager packageMan;
	
	public AppInfoProvider(Context context){
		packageMan=context.getPackageManager();
	}
	
	//获取手机内安装应用的所有信息
	public List<AppInfo> getAllAppsInfo(){
		
		List<AppInfo> appInfoList=new ArrayList<AppInfo>();
		
		AppInfo appInfoObj;
		
		//获取所有安装了的应用程序的信息，包括哪些卸载了的
		List<PackageInfo>allAppsList=packageMan.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		
		for(int i=0;i<allAppsList.size();i++){
			
			appInfoObj=new AppInfo();
			
			//拿到包名
			String packageName=allAppsList.get(i).packageName;
			//拿到应用程序的信息，以便获取程序的名称和图标
			ApplicationInfo appInfo=allAppsList.get(i).applicationInfo;
			
			//获取图标
			Drawable icon=appInfo.loadIcon(packageMan);
			//获取程序的名字
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
	
	
	//判断一个应用程序是不是系统的应用程序
	public boolean isSysApp(ApplicationInfo info){
		
		//如果用户下载了一个新的系统应用程序，更新了原来的
		if((info.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0){
			return true;
		}
		else if((info.flags&ApplicationInfo.FLAG_SYSTEM)==0){
			return true;
		}
		
		return false;
		
	}
	
	
}
