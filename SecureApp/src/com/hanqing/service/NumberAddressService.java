package com.hanqing.service;

import com.hanqing.domain.AddressDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class NumberAddressService {

	public static String getAddress(String number){
		
		String pattern="^1[3458]\\d{9}";
		String address="";
		
		//������ֻ�����
		if(number.matches(pattern)){
			
			address=query("select city from address_tb where _id=(select outkey from numinfo where mobileprefix=?)", new String[]{number.substring(0, 7)});
			if(address.equals("")){
				address=number;
			}
			
		}
		//����ǹ̶��绰
		else{
			int len=number.length();
			
			switch(len){
				case 4:
					//ģ����
					address="ģ����";
					break;
				
				case 7:
					//���غ���
					address="���غ���";
					break;
				
				case 8:
					//���غ���
					address="���غ���";
					break;
				
				case 10:
					//3λ���ţ�7λ����
					address=query("select city from address_tb where area=?",new String[]{number.substring(0, 3)});
					if(address.equals("")){
						address=number;
					}
					break;
					
				 case 11:
					 //4λ���ţ�7λ���룬����3λ���ţ�8λ����
					 //һ��ʼĬ����4λ����
					 address=query("select city from address_tb where area=?",new String[]{number.substring(0, 4)});
					 if(address.equals("")){
						 //��ʾһ��ʼ���ǣ�3λ����
						 address=query("select city from address_tb where area=?",new String[]{number.substring(0, 3)});
						 if(address.equals("")){
							 address=number;
						 }
					 }
					 
					 break;
				
				 case 12:
					 //4λ���ţ�8λ����
					 address=query("select city from address_tb where area=?",new String[]{number.substring(0, 4)});
					 if(address.equals("")){
						 address=number;
					 }
					 break;
					 
				default:
					break;
			}
		}		
		
		return address;
		
	}
	
	private static String query(String sql,String[] selectionArgs){
		
		String result="";
		String path=Environment.getExternalStorageDirectory()+"/SecureApp/db/number_location.db";
		
		Log.e("path", path);
		
		SQLiteDatabase db=AddressDao.getAddressDB(path);
		
		if(db.isOpen()){
			
			Log.e("���ݿ��Ƿ��", db.getPath());
			
			Cursor cursor=db.rawQuery(sql, selectionArgs);
			if(cursor.moveToNext()){
				
				result=cursor.getString(0);
				db.close();
				
			}
	
		}
		
		return result;
		
	}
}
