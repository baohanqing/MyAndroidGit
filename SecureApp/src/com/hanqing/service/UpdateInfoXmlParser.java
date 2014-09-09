package com.hanqing.service;

import java.io.InputStream;

import org.xml.sax.Parser;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.hanqing.domain.UpdateInfo;

/**
 * 改程序负责将update.xml当中的文件解析成UpdateInfo对象
 * @author baohanqing
 */

public class UpdateInfoXmlParser {

	public static UpdateInfo getUpdateInfoObj(InputStream is){
		
		UpdateInfo info=new UpdateInfo();
		
		XmlPullParser parser=Xml.newPullParser();
		
		try {
			//以utf-8的形式读入输入流
			parser.setInput(is, "utf-8");
			//获取标签的类型
			
			/**
			 * START_DOCUMENT:0
			 * END_DOCUMENT:1 
			 * START_TAG:2
			 * END_TAG:3 
			 * TEXT:4
			 */
			
			int type=parser.getEventType();
			
			
			
			//如果没有到文档的结尾
			while(type!=XmlPullParser.END_DOCUMENT){
				
				switch (type) {
					//文档的开始
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
			System.out.println("读取xml文件失败了！！！");
			return null;
		}
		
		return info;
		
	}
	
}
