package com.hanqing.util;

import java.text.DecimalFormat;

/**
 * 该类用来将字节数据转换为KB，MB,GB
 * @author baohanqing
 *
 */
public class TextFomater{

	public static String dataSizeFormat(long size){
		
		DecimalFormat formater=new DecimalFormat("####.00");
		
		//字节
		if(size<1024){
			
			return size+"byte";
			
		}
		//KB
		else if(size<(1024*1024)){
			
			float kSize=size/(1024);
			
			return kSize+"KB";
			
		}
		//MB
		else if(size<(1024*1024*1024)){
			
			float mSize=size/(1024*1024);
			
			return mSize+"MB";
			
		}
		//GB
		else if(size<(1024*1024*1024*1024)){
			
			float gSize=size/(1024*1024*1024);
			
			return gSize+"GB";
		}
		else{
			
			return "出错了";
			
		}
		
		
		
	}
	
}
