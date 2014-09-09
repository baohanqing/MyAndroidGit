package com.hanqing.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.util.Log;

/**
 * �ú������������汾��ʱ�������°汾,��ȡҪ���ص��°汾����
 * @author baohanqing
 *
 */

public class DownloadVersionTask {

	//���Ȼ�ȡ������Ҫ��ý�Ҫ�����İ汾�ļ�
	/**
	 * @param path ����������apk����λ��
	 * @param filePath  �浽�ֻ���λ��
	 * @param progressDialog
	 * @return 
	 */
	public static File getFile(String path,String filePath,ProgressDialog progressDialog){
		
		
		
		try {
			URL url=new URL(path);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			//���ó�ʱʱ��
			con.setConnectTimeout(2*1000);
			con.setRequestMethod("GET");
			//������ӳɹ�
			if(con.getResponseCode()==200){
				//��ȡ�ļ��ĳ���,�ֽڵ���ʽ
				int totalLength=con.getContentLength();
				progressDialog.setMax(totalLength);
				
				InputStream is=con.getInputStream();
				File file=new File(filePath);
			
				FileOutputStream fos=new FileOutputStream(file);
				
				//һ�ζ�ȡһ���ֽ�
				byte[] buffer=new byte[1024];
				int len;
				//��ǰ����
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
			System.out.println("��ȡ�°汾�ļ������쳣");
		}
		
		return null;
		
	}
	
}
