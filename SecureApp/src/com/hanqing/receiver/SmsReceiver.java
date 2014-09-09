package com.hanqing.receiver;

import com.example.secureapp.R;
import com.hanqing.service.GPSInfoProvider;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
/**
 * �������ڼ����û����Ͷ���
 * Ȼ���ж��û���λ��
 * ����ֻҪ����һ������Ϊ#*location*#ָ��Ķ��ţ��Ϳ��Ի�ȡ���ֻ���λ��
 * @author baohanqing
 *
 */
public class SmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Object[] pdus=(Object[]) intent.getExtras().get("pdus");
		
		for(Object pdu:pdus){
			
			SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) pdu);
				
			//�õ���������
			String content=smsMessage.getMessageBody();
			//�õ������˵ĵ绰����
			String sender=smsMessage.getOriginatingAddress();
			
			if(content.equals("#*location*#")){
				//��ֹ�㲥�������С͵����
				abortBroadcast();
				GPSInfoProvider provider=GPSInfoProvider.getInstance(context);
				String location=provider.getLocation();
				System.out.println(location);
				
				if(!location.equals("")){
					//����λ���ŷ��ص��������ֻ�
					SmsManager smsManager=SmsManager.getDefault();
					smsManager.sendTextMessage(sender, null, location, null, null);
				}
				
			}
			else if(content.equals("#lockscreen#")){
				//��ʾִ������������
				DevicePolicyManager dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				dpm.resetPassword("123", 0);
				dpm.lockNow();
				abortBroadcast();
			}
			else if(content.equals("#wipedata#")){
				//�ظ���������
				DevicePolicyManager dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				dpm.wipeData(0);
				abortBroadcast();
			}
			else if(content.equals("#alarm")){
				//���ž���
				MediaPlayer player=MediaPlayer.create(context,R.raw.my_alarm);
				//��Ϊ�������
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			}
			
			
		}
		
	}

	
}
