package com.hanqing.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hanqing.domain.AppInfo;
import com.hanqing.util.DBHelper;

/**
 * 对程序锁列表的应用程序的数据库进行增删改查的操作
 * @author baohanqing
 *
 */
public class AppLockDao {

	private DBHelper dbHelper;
	
	//数据库的名称
		public static final String DATABASE_NAME="sercure.db";
		//数据库版本
		public static final int DATABASE_VERSION=1;
	
	public AppLockDao(Context context){
		dbHelper=new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//判断是否该应用程序在程序表的数据库当中
	public boolean find(String packageName){
		boolean result=false;
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor=db.rawQuery("select packagename from applock where packagename=?", new String[]{packageName});
			
			if(cursor.moveToNext()){
				result=true;
				cursor.close();
				
				db.close();
			}
			
		}
		
		return result;
	}
	
	//添加包
	public void add(String packagename){
		
		if(find(packagename)){
			return;
		}
		
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		if(db.isOpen()){
			db.execSQL("insert into applock (packagename) values(?)",new Object[]{packagename});
			db.close();
		}
	}
	
	//删除包
	public void delete(String packagename){
		
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("delete from applock where packagename=?",new Object[]{packagename});
			db.close();
		}
	}
	
	//获取到所有加锁程序的包名列表
	public List<String> getAllLockAppList(){
		
		List<String> lockPackageNames=new ArrayList<String>();
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		
		if(db.isOpen()){
			
			Cursor cursor=db.rawQuery("select packagename from applock", null);
			
			while(cursor.moveToNext()){
				String packageName=cursor.getString(0);
				
				lockPackageNames.add(packageName);
			}
			
			cursor.close();
			db.close();
			
		}
		
		return lockPackageNames;
		
	}
	
}
