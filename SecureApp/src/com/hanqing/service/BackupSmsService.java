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
 * 短信备份需要很长时间，特别是当短信量特别大的时候，所以我们不能在主线程当中操作界面
 * 因此我们开启一个服务，进行短信的备份
 * @author baohanqing
 *
 */
public class BackupSmsService extends Service{

	SmsService smsService;

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	//创建Service时候调用
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
				
				
				//首先获得所有的短信内容
				List<SmsInfo> smsList=smsService.getAllSmsInfo();
				
				
				//然后创建短信备份的目录
				File dir=new File(Environment.getExternalStorageDirectory(),"/security/backup");
				
				
				if(!dir.exists()){
					dir.mkdirs();
				}
				
				//创建文件
				File file=new File(Environment.getExternalStorageDirectory()+"/security/backup/smsbackup.xml");
				
				//创建一个xml序列号器
				XmlSerializer xmlSerializer=Xml.newSerializer();
				
				try {
					FileOutputStream fos=new FileOutputStream(file);
					
					//设置xml的编码
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
					 * 在子线程里不能弹出一个Toast的，因为子线程里没有Looper
					 * 但是通过Looper就可以了
					 */
					
					Looper.prepare();//创建一个LooPer
					Toast.makeText(getApplicationContext(), "短信备份成功", Toast.LENGTH_LONG).show();
					Looper.loop();//轮循一次消息
					
					
				} catch (Exception e) {
					e.printStackTrace();
					
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "短信备份失败", Toast.LENGTH_LONG).show();
					Looper.loop();
					
				}
				
			}
			
		}.start();
		
	}
	
	
	
}
