package com.cletomcj.wakeup;



import com.cletomcj.wakeup.broadcastrcv.AlarmBroadcastReceiver;
import com.cletomcj.wakeup.dialogs.TimePickerFragment;

import android.os.Bundle;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


/**
 * This is the main Activity, into this activity the user can select an alarm,
 * cancel it and add a playlist.
 * @author Carlos Martin-Cleto
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener{
	
	private Context mContext;
	private static boolean alarmIsSet;
	private static Button b1;
	private static Button b2;
	private static Button b3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		b1 = (Button) findViewById(R.id.alarms_button);
		b2 = (Button) findViewById(R.id.cancel_button);
		b3 = (Button) findViewById(R.id.musics_button);
		//the callback interface that is passed like argument of "setOnClickListener"
		//is this class, because this class implements "OnClickListener" interface 
		//and defines the "onClick" method.
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch (item.getItemId()) {
        case R.id.help_menu:
			startActivity(new Intent(mContext, HelpActivity.class));
			return true;
        case R.id.about_menu:
			startActivity(new Intent(mContext, AboutActivity.class));
			return true;
        default:
            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * This method is required to implements the "OnClickListener" interface
	 * Into this method we distinguish the action to be performed as a function 
	 * of the pressed button 
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alarms_button:
			showTimePickerDialog();
			break;
		case R.id.cancel_button:
			if(alarmIsSet){
			cancelAlarm();
			Toast.makeText(this, R.string.alarm_canceled, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.musics_button:
			startActivity(new Intent(mContext, MusicListsActivity.class));
			break;
		}
	}
	
	public void showTimePickerDialog() {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	public void cancelAlarm(){
		Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(mContext, 12345, intent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
        alarmIsSet = false;
	}
	
	/**
	 * Auxiliary method to set the value of the static field "alarmIsSet"
	 * from other classes
	 * @param value boolean attribute to distinguish when the the alarm has been canceled 
	 * or when has been activated 
	 */
	public static void setAlarmState(boolean value){
		alarmIsSet = value;
	}
		
}
