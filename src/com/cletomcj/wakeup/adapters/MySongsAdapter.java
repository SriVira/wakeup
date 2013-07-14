package com.cletomcj.wakeup.adapters;

import java.util.ArrayList;

import com.cletomcj.wakeup.R;
import com.cletomcj.wakeup.retos.Song;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MySongsAdapter extends BaseAdapter{
	
	private Context context;
	private int layoutId;
	private ArrayList<Song> songs;
	private LayoutInflater mInflater;

	
	
	public MySongsAdapter(Context context, int layoutId, ArrayList<Song> songs){
		super();
		this.context = context;
		this.layoutId = layoutId;
		this.songs = songs;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * Returns the size of the ListView
	 */
	@Override
	public int getCount() {
		if(songs == null){
			return 0;
		}
		return songs.size();
	}

	/**
	 * Returns the MusicList object at this position on the ListView
	 */
	@Override
	public Song getItem(int position) {
		return songs.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View row = mInflater.inflate(layoutId, parent, false);		
		final Song song = getItem(position);
		Button btnSong = (Button)row.findViewById(R.id.list_button);
		//we declare a context menu for each row
		((Activity) context).registerForContextMenu(btnSong);
		//we set manually the row ID to do easier find each Item from the Activity class. 
		//Only we'll have to call the public method "getItem(position)" from the Activity 
		btnSong.setId(position);
		btnSong.setText(song.getTitle());
		//if it's the playlist set a "tick" icon on the right side
		btnSong.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//nothing for now
			}
		});
		return row;
		
	}
	
	public void setSongs(ArrayList<Song> songs){
		this.songs = songs;
	}			
}


