package com.cletomcj.wakeup.dialogs;

import java.util.Calendar;

import com.cletomcj.wakeup.MainActivity;
import com.cletomcj.wakeup.R;
import com.cletomcj.wakeup.broadcastrcv.AlarmBroadcastReceiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * This is an auxiliary class to create the Time Picker DIalog and define the 
 * actions to be performed when the time is set.
 * This class must implement the "TimePickerDialog.OnTimeSetListener" interface
 * to be avaible to define the "onTimeSet" method.
 * 
 * @author Carlos Martin-Cleto
 *
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
	
	private Context context;
	private Notification notification;
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		context = getActivity();
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(context, this, hour, minute, DateFormat.is24HourFormat(context));
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	  //We define a "Calendar" Object with the time choosen
	   Calendar calNow = Calendar.getInstance();
       Calendar calSet = (Calendar) calNow.clone();

       calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
       calSet.set(Calendar.MINUTE, minute);
       calSet.set(Calendar.SECOND, 0);
       calSet.set(Calendar.MILLISECOND, 0);

       if(calSet.compareTo(calNow) <= 0)
       {
          //Today Set time passed, count to tomorrow
          calSet.add(Calendar.DATE, 1);
       }
       setAlarm(calSet);//creates the PendingIntent
	   Toast.makeText(context, R.string.alarm_activated, Toast.LENGTH_SHORT).show();
	   
	   //--------- CODE TO ADD THE NOTIFICATION -------------------------------------
	   Intent intent = new Intent(context, MainActivity.class);
	   //It's needed to distinguish between older and latest versions as a result of deprecations
	   if(android.os.Build.VERSION.SDK_INT < 11){
		notification = new Notification(R.drawable.alarm_icon, "WakeUp app", System.currentTimeMillis());
		notification.setLatestEventInfo(context, "Wake Up", "Alarma activada", PendingIntent.getActivity(context, 1, intent, 0));
	   }else{
	   notification = new Notification.Builder(context).setSmallIcon(R.drawable.alarm_icon)
			   											.setContentTitle("Wake Up")
			   											.setContentText("Alarma activada")
			   											.setContentIntent(PendingIntent.getActivity(context, 1, intent, 0))
			   											.build();
	   }
	   notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
	   NotificationManager notifier = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	   notifier.notify(1, notification);
	   MainActivity.setAlarmState(true);
	}
	//-----------------------------------------------------------------------------
	
	private void setAlarm(Calendar targetCal){

		      Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		      PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12345, intent, 0);
		      AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		      alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
	}
		
}
