package com.hanqing.receiver;

import java.text.BreakIterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * 手机防盗的主逻辑
 * 手机丢失了，小偷会换sim卡，我们只要在重启完成之后，比较现在的Sim卡是不是之前的sim卡就行了，如果不是，就发送一条信息到设置的安全号码那里
 * @author baohanqing
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver{

	private SharedPreferences sp;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isProtected=sp.getBoolean("isProtected", false);
		
		//如果开启了防盗
		if(isProtected){
			TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			//开机后，拿到当前sim卡的标识与我们之前存放的标识对比
			String currentSim=telephonyManager.getSimSerialNumber();
			String protectedSim=sp.getString("sim_serial", "");
			
			//如果换了sim卡，就给指定的手机号发送短信
			if(!currentSim.equals(protectedSim)){
				//拿到一个短信管理器
				SmsManager smsManager=SmsManager.getDefault();
				String phoneNum=sp.getString("phone_number", "");
				/*
				 * 发送短信有五个参数
				 * 第一个是要发送的地址
				 * 第二个是发送人，可以Null
				 * 第三个是要发送的信息
				 * 第四个是发送状态
				 * 第五个是发送后的
				 */
				
				smsManager.sendTextMessage(phoneNum, null, "sim卡已经变更，手机可能被盗！", null, null);
				
			}
			
		}
		
	}
	
}
