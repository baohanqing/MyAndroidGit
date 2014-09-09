package com.hanqing.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hanqing.domain.AppInfo;
import com.hanqing.util.DBHelper;

/**
 * �Գ������б��Ӧ�ó�������ݿ������ɾ�Ĳ�Ĳ���
 * @author baohanqing
 *
 */
public class AppLockDao {

	private DBHelper dbHelper;
	
	//���ݿ������
		public static final String DATABASE_NAME="sercure.db";
		//���ݿ�汾
		public static final int DATABASE_VERSION=1;
	
	public AppLockDao(Context context){
		dbHelper=new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//�ж��Ƿ��Ӧ�ó����ڳ��������ݿ⵱��
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
	
	//��Ӱ�
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
	
	//ɾ����
	public void delete(String packagename){
		
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("delete from applock where packagename=?",new Object[]{packagename});
			db.close();
		}
	}
	
	//��ȡ�����м�������İ����б�
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
