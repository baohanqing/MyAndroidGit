package com.hanqing.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	
		//创建一个黑名单表
		db.execSQL("create table blacknumber(num_id integer primary key autoincrement," +
				"number varchar(20))");
		
		//创建程序锁的数据库列表
		db.execSQL("create table applock(app_id integer primary key autoincrement,packagename varchar(30))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		System.out.println("数据库版本更新：旧版本"+oldVersion+"新版本："+newVersion);
		
		
		
		
	}

	
	
}
