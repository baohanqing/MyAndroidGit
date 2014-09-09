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
		
		//如果是手机号码
		if(number.matches(pattern)){
			
			address=query("select city from address_tb where _id=(select outkey from numinfo where mobileprefix=?)", new String[]{number.substring(0, 7)});
			if(address.equals("")){
				address=number;
			}
			
		}
		//如果是固定电话
		else{
			int len=number.length();
			
			switch(len){
				case 4:
					//模拟器
					address="模拟器";
					break;
				
				case 7:
					//本地号码
					address="本地号码";
					break;
				
				case 8:
					//本地号码
					address="本地号码";
					break;
				
				case 10:
					//3位区号，7位号码
					address=query("select city from address_tb where area=?",new String[]{number.substring(0, 3)});
					if(address.equals("")){
						address=number;
					}
					break;
					
				 case 11:
					 //4位区号，7位号码，或者3位区号，8位号码
					 //一开始默认用4位区号
					 address=query("select city from address_tb where area=?",new String[]{number.substring(0, 4)});
					 if(address.equals("")){
						 //表示一开始的是，3位区号
						 address=query("select city from address_tb where area=?",new String[]{number.substring(0, 3)});
						 if(address.equals("")){
							 address=number;
						 }
					 }
					 
					 break;
				
				 case 12:
					 //4位区号，8位号码
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
			
			Log.e("数据库是否打开", db.getPath());
			
			Cursor cursor=db.rawQuery(sql, selectionArgs);
			if(cursor.moveToNext()){
				
				result=cursor.getString(0);
				db.close();
				
			}
	
		}
		
		return result;
		
	}
}
