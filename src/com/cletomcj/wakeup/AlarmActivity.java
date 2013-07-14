package com.cletomcj.wakeup;


import java.io.IOException;
import java.util.ArrayList;

import com.cletomcj.wakeup.adapters.DatabaseAdapter;
import com.cletomcj.wakeup.retos.Operation;
import com.cletomcj.wakeup.retos.Song;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.NotificationManager;

/**
 * This Activity appears when the alarm starts. In this activity a ringtone is played and
 * the user will have to resolve an operation to turn off the alarm.
 * @author Carlos Martin-Cleto
 *
 */
public class AlarmActivity extends Activity implements OnClickListener{
	
	private Window wind;
	private Context context;
	private TextView operacion;
	private Operation op;
	private EditText edit_text;
	private static ArrayList<Operation> lista;
	private static ArrayList<Song> songs;
	private static DatabaseAdapter databaseAdapter;
	private MediaPlayer mp;
	private String randomPath;
	private AudioManager audioMng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarms);
		//This forces the associated window of this activity to turn on the
		//screen and unlock the mobile phone
		 wind = this.getWindow();
		 wind.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		 wind.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		 wind.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
		 context = this;
		 databaseAdapter = new DatabaseAdapter(context);
		 songs = new ArrayList<Song>();
		 if(!databaseAdapter.isOpen()){
		 databaseAdapter.open();
		 }
		 //we obtain a random song from the list
		 randomPath = getSongPath();
		 //This forces that even in silent mode the song will be played with max volume
		audioMng = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolumeMusic = audioMng.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		audioMng.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audioMng.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolumeMusic,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		 
		Button b1 = (Button) findViewById(R.id.turnoff_button);
		operacion = (TextView) findViewById(R.id.operacion);
		edit_text = (EditText) findViewById(R.id.edit_text);
		createList();
		 //choose a random operation
		 op = lista.get((int) (Math.random()*9+0));
		 //add the choosen operation to the TextView
		 operacion.setText(op.getTexto());
		 b1.setOnClickListener(this); 
		 playSong(randomPath);
		 	
	}
	@Override
	protected void onStart(){
		super.onStart();
		//remove the notification from the status bar
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
	}
	
	
	/**
	 * Into this method we decide if the user has introduced a right value or 
	 * he hasn't and we declare the respectives actions to perform in each case
	 */
	@Override
	public void onClick(View v) {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		//A try/catch sentence is needed because an user can be idiot and perhaps
		//he doesn't type a number or just doesn't type anything
		try{
			int input = Integer.parseInt(edit_text.getText().toString());
			if(input == op.getResultado()){
			//if the user resolves the problem right...
				if(mp != null){
				mp.stop();
				mp.release();
				mp = null;
				}
				MainActivity.setAlarmState(false);
				finish(); //just finish the activity and comes back to MainActivity
			}else{
			//if he doesn't
				vibrator.vibrate(300);
				Toast.makeText(context, "Incorrecto!", Toast.LENGTH_SHORT).show();
			}
		}
		catch(Exception e){
			Log.e("Operation exception", "Exception shit", e);
			vibrator.vibrate(300);
			Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();	
		}

	}
	
	private void createList(){
		lista = new ArrayList<Operation>();
		lista.add(new Operation(60, "12 x 5 = ?"));
		lista.add(new Operation(64, "16 x 4 = ?"));
		lista.add(new Operation(115, "23 x 5 = ?"));
		lista.add(new Operation(84, "28 x 3 = ?"));
		lista.add(new Operation(108, "12 x 9 = ?"));
		lista.add(new Operation(105, "15 x 7 = ?"));
		lista.add(new Operation(119, "17 x 7 = ?"));
		lista.add(new Operation(90, "18 x 5 = ?"));
		lista.add(new Operation(168, "24 x 7 = ?"));
		lista.add(new Operation(57, "19 x 3 = ?"));
	}
	
	private String getSongPath(){
		long id = databaseAdapter.getSelectedListId();
		songs = databaseAdapter.getSongs(id);
		Song selectedSong = songs.get((int)(Math.random()*songs.size()));
		return selectedSong.getPath();
		
	}
	
	private void playSong(String path){
		try {
		mp = new MediaPlayer();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.setDataSource(path);
		mp.setLooping(true);
		mp.prepare();
		mp.start();
		} catch (IOException e) {
			Log.e("Mediaplayer", "Error trying to play the song", e);
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		databaseAdapter.close();
		if(mp != null){
		mp.stop();
		mp.release();
		mp = null;
		}
	}
		
}
