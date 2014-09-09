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
 * ��ȡ�ֻ����ŵ����ݣ��Ա��ڶ��ŵı��ݲ���
 * @author baohanqing
 *
 */
public class SmsService {

	private Context context;
	
	//��ʾ���ŵ���������
	private int index;
	
	
	
	public SmsService(Context context){
		this.context=context;
	}
	
	//��ԭ���ŵķ���
	public void resotreSms(String path,ProgressDialog dialog){
		
		File file=new File(path);
		
		try {
			
			FileInputStream fis=new FileInputStream(file);
			
			XmlPullParser parser=Xml.newPullParser();
			
			ContentValues values=null;
			
			parser.setInput(fis, "utf-8");
	
			//��ȡXml����,START_TAG, END_TAG, TEXT
			int type=parser.getEventType();
			
			int index=0;
			
			//���û�е�xml�ĵ��Ľ�β
			while(type!=XmlPullParser.END_DOCUMENT){
				
				switch(type){
					
					case XmlPullParser.START_TAG:
						
						//��ȡ���ŵ�����
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
							//��ʾ��һ�����ŵĽ�β,��ô���ǽ��������Ų��뵽���ݿ⵱��
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
			System.out.println("���ű���ʧ��");
			e.printStackTrace();
		}
		
		
		
		
	}
	
	//��ȡ���еĶ�������
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