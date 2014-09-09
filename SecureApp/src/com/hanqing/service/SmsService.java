package com.hanqing.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.hanqing.domain.SmsInfo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

/**
 * 读取手机短信的内容，以便于短信的备份操作
 * @author baohanqing
 *
 */
public class SmsService {

	private Context context;
	
	//表示短信的下载条数
	private int index;
	
	
	
	public SmsService(Context context){
		this.context=context;
	}
	
	//还原短信的方法
	public void resotreSms(String path,ProgressDialog dialog){
		
		File file=new File(path);
		
		try {
			
			FileInputStream fis=new FileInputStream(file);
			
			XmlPullParser parser=Xml.newPullParser();
			
			ContentValues values=null;
			
			parser.setInput(fis, "utf-8");
	
			//获取Xml类型,START_TAG, END_TAG, TEXT
			int type=parser.getEventType();
			
			int index=0;
			
			//如果没有到xml文档的结尾
			while(type!=XmlPullParser.END_DOCUMENT){
				
				switch(type){
					
					case XmlPullParser.START_TAG:
						
						//获取短信的数量
						if("count".equals(parser.getName())){
							int count=Integer.parseInt(parser.nextText());
							dialog.setMax(count);
						}
						if("sms".equals(parser.getName())){
							values=new ContentValues();
						}
						else if("address".equals(parser.getName())){
							values.put("address", parser.nextText());
						}
						else if("date".equals(parser.getName())){
							values.put("date", parser.nextText());
						}
						else if("type".equals(parser.getName())){
							values.put("type", parser.nextText());
						}
						else if("body".equals(parser.getName())){
							values.put("body", parser.nextText());
						}
						
						break;	
						
					case XmlPullParser.END_TAG:
						if("sms".equals(parser.getName())){
							//表示是一条短信的结尾,那么我们将该条短信插入到数据库当中
							ContentResolver resolver=context.getContentResolver();
							resolver.insert(Uri.parse("content://sms/"), values);
							
							values=null;
							index++;
							dialog.setProgress(index);
						}
						
						break;
						
						default:
							break;
				}
				
				type=parser.next();
				
			}
			
		} catch (Exception e) {
			System.out.println("短信备份失败");
			e.printStackTrace();
		}
		
		
		
		
	}
	
	//获取所有的短信内容
	public List<SmsInfo> getAllSmsInfo(){
		
		SmsInfo smsInfo;
		
		List<SmsInfo> smsList=new ArrayList<SmsInfo>();
		
		Uri uri=Uri .parse("content://sms/");
		
		ContentResolver resolver=context.getContentResolver();
		
		Cursor cursor=resolver.query(uri, new String[]{"_id","address","date","type","body"}, null, null, "date desc");
		
		while(cursor.moveToNext()){
			smsInfo=new SmsInfo();
			
			String id=cursor.getString(0);
			String address=cursor.getString(1);
			String date=cursor.getString(2);
			int type=cursor.getInt(3);
			String body=cursor.getString(4);
			
			smsInfo.setId(id);
			smsInfo.setAddress(address);
			smsInfo.setDate(date);
			smsInfo.setType(type);
			smsInfo.setBody(body);
			
			smsList.add(smsInfo);
		}
		
		cursor.close();
		
		return smsList;
		
	}
	
}
