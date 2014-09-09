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
 * ����ҳ����ҳ��ȡ��ϵ����Ϣ��service
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
		
		//��ȡ��ϵ�˵�Uri
		//Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
		//��ȡ��ϵ����Ϣ��Uri�������绰���ʼ���
		//Uri dataUri=Uri.parse("content://com.android.contacts/data");
		
		Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		while(cursor.moveToNext()){
			contractInfo=new ContractInfo();
			//��ȡ��ϵ�˵�ID
			String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			//��ȡ��ϵ�˵�����
			String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			 contractInfo.setName(name);
			
			//������ϵ�˵�ID��ȡ����ϵ�˵ĵ绰
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
