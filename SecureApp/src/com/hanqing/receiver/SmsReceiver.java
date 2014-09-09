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
 * 该类用于监听用户发送短信
 * 然后判断用户的位置
 * 我们只要发送一条内容为#*location*#指令的短信，就可以获取该手机的位置
 * @author baohanqing
 *
 */
public class SmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Object[] pdus=(Object[]) intent.getExtras().get("pdus");
		
		for(Object pdu:pdus){
			
			SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) pdu);
				
			//拿到短信内容
			String content=smsMessage.getMessageBody();
			//拿到发送人的电话号码
			String sender=smsMessage.getOriginatingAddress();
			
			if(content.equals("#*location*#")){
				//终止广播，免得让小偷看见
				abortBroadcast();
				GPSInfoProvider provider=GPSInfoProvider.getInstance(context);
				String location=provider.getLocation();
				System.out.println(location);
				
				if(!location.equals("")){
					//将定位短信发回到发送至手机
					SmsManager smsManager=SmsManager.getDefault();
					smsManager.sendTextMessage(sender, null, location, null, null);
				}
				
			}
			else if(content.equals("#lockscreen#")){
				//表示执行锁屏的命令
				DevicePolicyManager dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				dpm.resetPassword("123", 0);
				dpm.lockNow();
				abortBroadcast();
			}
			else if(content.equals("#wipedata#")){
				//回复出厂设置
				DevicePolicyManager dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				dpm.wipeData(0);
				abortBroadcast();
			}
			else if(content.equals("#alarm")){
				//播放警报
				MediaPlayer player=MediaPlayer.create(context,R.raw.my_alarm);
				//设为最大音量
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			}
			
			
		}
		
	}

	
}
