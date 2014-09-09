package com.hanqing.domain;

import android.database.sqlite.SQLiteDatabase;

/**
 * 打开我们存放电话号码的数据库
 * @author baohanqing
 *
 */
public class AddressDao {

	public static SQLiteDatabase getAddressDB(String path){
		
		//打开那个存放电话号码的数据库
		return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	}
	
}
