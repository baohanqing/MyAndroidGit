package com.hanqing.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.hanqing.domain.SmsInfo;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

/**
 * ���ű�����Ҫ�ܳ�ʱ�䣬�ر��ǵ��������ر���ʱ���������ǲ��������̵߳��в�������
 * ������ǿ���һ�����񣬽��ж��ŵı���
 * @author baohanqing
 *
 */
public class BackupSmsService extends Service{

	SmsService smsService;

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	//����Serviceʱ�����
	@Override
	public void onCreate() {
		
		super.onCreate();
		
		smsService=new SmsService(this);
		
		new Thread(){

			/* (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				
				
				//���Ȼ�����еĶ�������
				List<SmsInfo> smsList=smsService.getAllSmsInfo();
				
				
				//Ȼ�󴴽����ű��ݵ�Ŀ¼
				File dir=new File(Environment.getExternalStorageDirectory(),"/security/backup");
				
				
				if(!dir.exists()){
					dir.mkdirs();
				}
				
				//�����ļ�
				File file=new File(Environment.getExternalStorageDirectory()+"/security/backup/smsbackup.xml");
				
				//����һ��xml���к���
				XmlSerializer xmlSerializer=Xml.newSerializer();
				
				try {
					FileOutputStream fos=new FileOutputStream(file);
					
					//����xml�ı���
					xmlSerializer.setOutput(fos,"utf-8");
					xmlSerializer.startDocument("utf-8", true);
					
					xmlSerializer.startTag(null, "smss");
					
					xmlSerializer.startTag(null,"count");
					xmlSerializer.text(smsList.size()+"");
					xmlSerializer.endTag(null, "count");
					
					for(int i=0;i<smsList.size();i++){
						
						SmsInfo sms=smsList.get(i);
						
						xmlSerializer.startTag(null, "sms");
						
						xmlSerializer.startTag(null, "id");
						xmlSerializer.text(sms.getId());
						xmlSerializer.endTag(null, "id");
						
						
						xmlSerializer.startTag(null, "address");
						xmlSerializer.text(sms.getAddress());
						xmlSerializer.endTag(null, "address");
						
						xmlSerializer.startTag(null, "date");
						xmlSerializer.text(sms.getDate());
						xmlSerializer.endTag(null, "date");
						
						xmlSerializer.startTag(null, "type");
						xmlSerializer.text(sms.getType()+"");
						xmlSerializer.endTag(null, "type");
						
						xmlSerializer.startTag(null, "body");
						xmlSerializer.text(sms.getBody());
						xmlSerializer.endTag(null, "body");
						
						xmlSerializer.endTag(null, "sms");

					}
					
					xmlSerializer.endTag(null, "smss");
					xmlSerializer.endDocument();
					
					fos.flush();
					fos.close();
					
					/*
					 * �����߳��ﲻ�ܵ���һ��Toast�ģ���Ϊ���߳���û��Looper
					 * ����ͨ��Looper�Ϳ�����
					 */
					
					Looper.prepare();//����һ��LooPer
					Toast.makeText(getApplicationContext(), "���ű��ݳɹ�", Toast.LENGTH_LONG).show();
					Looper.loop();//��ѭһ����Ϣ
					
					
				} catch (Exception e) {
					e.printStackTrace();
					
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "���ű���ʧ��", Toast.LENGTH_LONG).show();
					Looper.loop();
					
				}
				
			}
			
		}.start();
		
	}
	
	
	
}
