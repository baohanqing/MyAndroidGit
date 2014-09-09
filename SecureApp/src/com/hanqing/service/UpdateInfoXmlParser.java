package com.hanqing.service;

import java.io.InputStream;

import org.xml.sax.Parser;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.hanqing.domain.UpdateInfo;

/**
 * �ĳ�����update.xml���е��ļ�������UpdateInfo����
 * @author baohanqing
 */

public class UpdateInfoXmlParser {

	public static UpdateInfo getUpdateInfoObj(InputStream is){
		
		UpdateInfo info=new UpdateInfo();
		
		XmlPullParser parser=Xml.newPullParser();
		
		try {
			//��utf-8����ʽ����������
			parser.setInput(is, "utf-8");
			//��ȡ��ǩ������
			
			/**
			 * START_DOCUMENT:0
			 * END_DOCUMENT:1 
			 * START_TAG:2
			 * END_TAG:3 
			 * TEXT:4
			 */
			
			int type=parser.getEventType();
			
			
			
			//���û�е��ĵ��Ľ�β
			while(type!=XmlPullParser.END_DOCUMENT){
				
				switch (type) {
					//�ĵ��Ŀ�ʼ
					case XmlPullParser.START_TAG:
						
						if(parser.getName().equals("version")){
							
							info.setVersion(parser.nextText());
						}
						else if(parser.getName().equals("descprition")){
							
							info.setDescription(parser.nextText());
						}
						else if(parser.getName().equals("apkurl")){
							
							info.setApkUrl(parser.nextText());
						}
					break;

				default:
					break;
				}
				type=parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("��ȡxml�ļ�ʧ���ˣ�����");
			return null;
		}
		
		return info;
		
	}
	
}
