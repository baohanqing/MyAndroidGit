package com.hanqing.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.hanqing.domain.UpdateInfo;

import android.content.Context;
import android.util.Log;

/**
 * @author baohanqing
 * 从服务器上读取update.xml文件
 */

public class UpdateInfoService {

	private Context context;
	
	public UpdateInfoService(Context context){
		this.context=context;
	}
	
	//获取更新信息的方法,获取一个更新对象
	public UpdateInfo getUpdateInfo(int urlId){
			/**
			 * 在程序中获取string.xml中的字符串
			 * 1，context.getString(R.string.resource_name)
			 * 2，application.getString(R.string.resource_name)
			 */
			String path=context.getString(urlId);
			
			
			try {
				URL url=new URL(path);
				
				
				
				//开启一个http链接
				HttpURLConnection con=(HttpURLConnection) url.openConnection();
				
				//设置链接的超市时间，5s
				con.setConnectTimeout(5000);
				//设置请求方式
				con.setRequestMethod("GET");
				
				//拿到一个输入流
				InputStream is=con.getInputStream();
				
				//int state=con.getResponseCode();
				
				//Log.e("返回码", state+"");
				
				
				
				//通过解析的方法，返回一个解析的对象
				return UpdateInfoXmlParser.getUpdateInfoObj(is);
				
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("打开更新链接失败！！！");
			
			}
			return null;
	}
	
}
