package com.hanqing.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.hanqing.util.DBHelper;

/**
 * 黑名单，对数据库操作层，Data Access Objcet
 * @author baohanqing
 *
 */


public class BlackNumberDao {


	//数据库的名称
	public static final String DATABASE_NAME="sercure.db";
	//数据库版本
	public static final int DATABASE_VERSION=1;
	
	private DBHelper dbHelper;
	
	public BlackNumberDao(Context context){
		
		//创建一个数据库的DataBaseHelper类
		dbHelper=new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	
	//判断该号码是否在黑名单当中
	public boolean find(String number){
		
		boolean isFind=false;
		
		//或得数据库对象
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		
		if(db.isOpen()){
			
			Cursor cursor=db.rawQuery("select number from blacknumber where number=?", new String[]{number});
			
			if(cursor.moveToNext()){
				
				isFind=true;
				
			}
			cursor.close();
			db.close();
		}
		return isFind;
		
	}
	
	//添加号码
	public void add(String number){
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		
		if(db.isOpen()){
			
			db.execSQL("insert into blacknumber(number) values(?)", new String[]{number});
			db.close();
		}
	}
	
	
	//删除号码
	public void delete(String number){
		
		Log.e("BlackNumberDao当中number", number+"");
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		if(db.isOpen()){
			
			db.execSQL("delete from blacknumber where number=?",new String[]{number});
			db.close();
			
			Log.e("BlackNumberDao当中number", "删除操作");
			
		}

	}
	
	//更新号码
	public void update(String oldNumber,String newNumber){
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		if(db.isOpen()){
			
			db.execSQL("update blacknumber set number=? where number=?",new String[]{oldNumber,newNumber});
			db.close();
		}
	}
	
	
	//查找所有号码
	public List<String> findAll(){
		
		List<String> numbers=new ArrayList<String>();
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		
		if(db.isOpen()){
			
			Cursor cursor=db.rawQuery("select number from blacknumber",null);
			while(cursor.moveToNext()){
				numbers.add(cursor.getString(0));
			}
			
			cursor.close();
			db.close();
			
		}
		
		return numbers;
		
	}
	
	
}
















