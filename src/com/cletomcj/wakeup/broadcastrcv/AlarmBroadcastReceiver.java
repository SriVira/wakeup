package com.cletomcj.wakeup.broadcastrcv;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This broadcast receiver is needed to receive the Pending Intent 
 * delivered on the MainActivity
 * @author Carlos Martin-Cleto
 *
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver{
	
		
	@Override
	 public void onReceive(Context context, Intent intent) {
		//starts the AlarmActivity when the alarm goes off
		Intent i = new Intent();
        i.setClassName("com.cletomcj.wakeup", "com.cletomcj.wakeup.AlarmActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		
	 }
}

