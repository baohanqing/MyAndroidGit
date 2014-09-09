package com.hanqing.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.util.Log;

/**
 * 该函数用来升级版本的时候，下载新版本,读取要下载的新版本内容
 * @author baohanqing
 *
 */

public class DownloadVersionTask {

	//首先获取到我们要获得将要升级的版本文件
	/**
	 * @param path 服务区升级apk包的位置
	 * @param filePath  存到手机的位置
	 * @param progressDialog
	 * @return 
	 */
	public static File getFile(String path,String filePath,ProgressDialog progressDialog){
		
		
		
		try {
			URL url=new URL(path);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			//设置超时时间
			con.setConnectTimeout(2*1000);
			con.setRequestMethod("GET");
			//如果连接成功
			if(con.getResponseCode()==200){
				//获取文件的长度,字节的形式
				int totalLength=con.getContentLength();
				progressDialog.setMax(totalLength);
				
				InputStream is=con.getInputStream();
				File file=new File(filePath);
			
				FileOutputStream fos=new FileOutputStream(file);
				
				//一次读取一个字节
				byte[] buffer=new byte[1024];
				int len;
				//当前进度
				int curProgress=0;
				while((len=is.read(buffer))!=-1){
					fos.write(buffer, 0, len);
					curProgress+=len;
					progressDialog.setProgress(curProgress);
				}
				fos.flush();
				fos.close();
				is.close();
				return file;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取新版本文件出现异常");
		}
		
		return null;
		
	}
	
}
