package com.hanqing.util;

import java.security.MessageDigest;

/**
 * �ó������ڽ�һ���ַ���װ��ΪMD5����ʽ
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
				//���ֻ��һ���ַ�����Ҫ��0
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
			System.out.println("û����������㷨");
			return null;
		}

	}
	
}
