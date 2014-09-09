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
 * �ӷ������϶�ȡupdate.xml�ļ�
 */

public class UpdateInfoService {

	private Context context;
	
	public UpdateInfoService(Context context){
		this.context=context;
	}
	
	//��ȡ������Ϣ�ķ���,��ȡһ�����¶���
	public UpdateInfo getUpdateInfo(int urlId){
			/**
			 * �ڳ����л�ȡstring.xml�е��ַ���
			 * 1��context.getString(R.string.resource_name)
			 * 2��application.getString(R.string.resource_name)
			 */
			String path=context.getString(urlId);
			
			
			try {
				URL url=new URL(path);
				
				
				
				//����һ��http����
				HttpURLConnection con=(HttpURLConnection) url.openConnection();
				
				//�������ӵĳ���ʱ�䣬5s
				con.setConnectTimeout(5000);
				//��������ʽ
				con.setRequestMethod("GET");
				
				//�õ�һ��������
				InputStream is=con.getInputStream();
				
				//int state=con.getResponseCode();
				
				//Log.e("������", state+"");
				
				
				
				//ͨ�������ķ���������һ�������Ķ���
				return UpdateInfoXmlParser.getUpdateInfoObj(is);
				
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("�򿪸�������ʧ�ܣ�����");
			
			}
			return null;
	}
	
}
