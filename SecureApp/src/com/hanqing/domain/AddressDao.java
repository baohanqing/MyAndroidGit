package com.hanqing.domain;

import android.database.sqlite.SQLiteDatabase;

/**
 * �����Ǵ�ŵ绰��������ݿ�
 * @author baohanqing
 *
 */
public class AddressDao {

	public static SQLiteDatabase getAddressDB(String path){
		
		//���Ǹ���ŵ绰��������ݿ�
		return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	}
	
}
