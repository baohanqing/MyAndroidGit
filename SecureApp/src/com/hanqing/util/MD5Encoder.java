package com.hanqing.util;

import java.security.MessageDigest;

/**
 * 该程序用于将一个字符串装换为MD5的形式
 * @author baohanqing
 *
 */
public class MD5Encoder {

	public static String encode(String pwd){
		
		try {
			MessageDigest digest=MessageDigest.getInstance("MD5");
			byte[] bytes=digest.digest(pwd.getBytes());
			StringBuilder sb=new StringBuilder();
			String tmp;
			
			
			for(int i=0;i<bytes.length;i++){
				tmp=Integer.toHexString(0xff&bytes[i]);
				//如果只有一个字符串就要补0
				if(tmp.length()==1){
					sb.append("0"+tmp);
				}
				else{
					sb.append(tmp);
				}
			}
			
			return sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("没有这个加密算法");
			return null;
		}

	}
	
}
