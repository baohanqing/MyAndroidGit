package com.hanqing.receiver;

import java.text.BreakIterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * �ֻ����������߼�
 * �ֻ���ʧ�ˣ�С͵�ỻsim��������ֻҪ���������֮�󣬱Ƚ����ڵ�Sim���ǲ���֮ǰ��sim�������ˣ�������ǣ��ͷ���һ����Ϣ�����õİ�ȫ��������
 * @author baohanqing
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver{

	private SharedPreferences sp;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isProtected=sp.getBoolean("isProtected", false);
		
		//��������˷���
		if(isProtected){
			TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			//�������õ���ǰsim���ı�ʶ������֮ǰ��ŵı�ʶ�Ա�
			String currentSim=telephonyManager.getSimSerialNumber();
			String protectedSim=sp.getString("sim_serial", "");
			
			//�������sim�����͸�ָ�����ֻ��ŷ��Ͷ���
			if(!currentSim.equals(protectedSim)){
				//�õ�һ�����Ź�����
				SmsManager smsManager=SmsManager.getDefault();
				String phoneNum=sp.getString("phone_number", "");
				/*
				 * ���Ͷ������������
				 * ��һ����Ҫ���͵ĵ�ַ
				 * �ڶ����Ƿ����ˣ�����Null
				 * ��������Ҫ���͵���Ϣ
				 * ���ĸ��Ƿ���״̬
				 * ������Ƿ��ͺ��
				 */
				
				smsManager.sendTextMessage(phoneNum, null, "sim���Ѿ�������ֻ����ܱ�����", null, null);
				
			}
			
		}
		
	}
	
}
