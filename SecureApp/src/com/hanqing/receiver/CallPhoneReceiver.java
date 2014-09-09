package com.hanqing.receiver;
/**
 * ������androidManifest��������receiver
 * actionΪandroid.intent.action.NEW_OUTGOING_CALL�Ĺ㲥
 * ֻҪ����绰ʱ�ͻᷢ�͹㲥���ͻ���ø÷������ܸù㲥
 */
import com.hanqing.activity.LostProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CallPhoneReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String outPhoneNumber=this.getResultData();
		if(outPhoneNumber.equals("1314")){
			//���û�����ĺ�����1314��ʱ�򣬾ͽ������µĲ���
			Intent i=new Intent(context,LostProtectedActivity.class);
			//��Ϊ������������һ��Receiver��������һ��Activity������Activity������ʱ�򣬶��Ƿ���ͬһ��ջ���
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			//�ѹ㲥������Ϊnull�������Ͳ���Ѹո��Ǹ����벦���ȥ�ˣ�ֻ���������ǵ�Activity
			setResultData(null);
		}
		
	}

	
	
}
