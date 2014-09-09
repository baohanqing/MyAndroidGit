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
 * �������������ݿ�����㣬Data Access Objcet
 * @author baohanqing
 *
 */


public class BlackNumberDao {


	//���ݿ������
	public static final String DATABASE_NAME="sercure.db";
	//���ݿ�汾
	public static final int DATABASE_VERSION=1;
	
	private DBHelper dbHelper;
	
	public BlackNumberDao(Context context){
		
		//����һ�����ݿ��DataBaseHelper��
		dbHelper=new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	
	//�жϸú����Ƿ��ں���������
	public boolean find(String number){
		
		boolean isFind=false;
		
		//������ݿ����
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
	
	//��Ӻ���
	public void add(String number){
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		
		if(db.isOpen()){
			
			db.execSQL("insert into blacknumber(number) values(?)", new String[]{number});
			db.close();
		}
	}
	
	
	//ɾ������
	public void delete(String number){
		
		Log.e("BlackNumberDao����number", number+"");
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		if(db.isOpen()){
			
			db.execSQL("delete from blacknumber where number=?",new String[]{number});
			db.close();
			
			Log.e("BlackNumberDao����number", "ɾ������");
			
		}

	}
	
	//���º���
	public void update(String oldNumber,String newNumber){
		
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		if(db.isOpen()){
			
			db.execSQL("update blacknumber set number=? where number=?",new String[]{oldNumber,newNumber});
			db.close();
		}
	}
	
	
	//�������к���
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
















