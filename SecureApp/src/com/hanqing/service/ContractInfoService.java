package com.hanqing.service;

import java.util.ArrayList;
import java.util.List;

import com.hanqing.domain.ContractInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * 引导页第三页获取联系人信息的service
 * @author baohanqing
 *
 */
public class ContractInfoService {

	private Context context;
	
	public ContractInfoService(Context context){
		this.context=context;
	}
	
	public List<ContractInfo> getContractsInfo(){
		
		List<ContractInfo> infos=new ArrayList<ContractInfo>();
		ContractInfo contractInfo;
		
		ContentResolver contentResolver=context.getContentResolver();
		
		//获取联系人的Uri
		//Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
		//获取联系人信息的Uri，包括电话，邮件等
		//Uri dataUri=Uri.parse("content://com.android.contacts/data");
		
		Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		while(cursor.moveToNext()){
			contractInfo=new ContractInfo();
			//获取联系人的ID
			String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			//获取联系人的姓名
			String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			 contractInfo.setName(name);
			
			//根据联系人的ID获取到联系人的电话
			 Cursor phones=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=" +id,null, null);
			
			 while(phones.moveToNext()){
				 String phone=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				 contractInfo.setPhone(phone);
			 }
			 
			phones.close();
			infos.add(contractInfo);
			contractInfo=null;

		}
		cursor.close();
		return infos;
	}
	
}
