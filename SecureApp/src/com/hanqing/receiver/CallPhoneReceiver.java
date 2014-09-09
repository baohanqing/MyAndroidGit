package com.hanqing.receiver;
/**
 * 我们在androidManifest当中配置receiver
 * action为android.intent.action.NEW_OUTGOING_CALL的广播
 * 只要拨打电话时就会发送广播，就会调用该方法接受该广播
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
			//当用户拨打的号码是1314的时候，就进行如下的操作
			Intent i=new Intent(context,LostProtectedActivity.class);
			//因为这里我们是在一个Receiver当中启动一个Activity，但是Activity启动的时候，都是放在同一个栈里的
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			//把广播收设置为null，这样就不会把刚刚那个号码拨打出去了，只会启动我们的Activity
			setResultData(null);
		}
		
	}

	
	
}
