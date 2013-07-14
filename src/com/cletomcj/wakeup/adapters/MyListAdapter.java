package com.cletomcj.wakeup.adapters;

import java.util.ArrayList;

import com.cletomcj.wakeup.retos.MusicList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.cletomcj.wakeup.ListActivity;
import com.cletomcj.wakeup.R;

public class MyListAdapter extends BaseAdapter{
	
	private Context context;
	private int layoutId;
	private ArrayList<MusicList> lists;
	private LayoutInflater mInflater;

	
	
	public MyListAdapter(Context context, int layoutId, ArrayList<MusicList> lists){
		super();
		this.context = context;
		this.layoutId = layoutId;
		this.lists = lists;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * Returns the size of the ListView
	 */
	@Override
	public int getCount() {
		if(lists == null){
			return 0;
		}
		return lists.size();
	}

	/**
	 * Returns the MusicList object at this position on the ListView
	 */
	@Override
	public MusicList getItem(int position) {
		return lists.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View row = mInflater.inflate(layoutId, parent, false);		
		final MusicList mList = getItem(position);
		Button btnList = (Button)row.findViewById(R.id.list_button);
		//we declare a context menu for each row
		((Activity) context).registerForContextMenu(btnList);
		//we set manually the row ID to do easier find each Item from the Activity class. 
		//Only we'll have to call the public method "getItem(position)" from the Activity 
		btnList.setId(position);
		btnList.setText(mList.getTitle());
		//if it's the playlist set a "tick" icon on the right side
		if(mList.isPlaylist() == 1){
			Drawable img = context.getResources().getDrawable( R.drawable.ticklist);
			btnList.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null );
		}
		btnList.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(context, ListActivity.class);
				intent.putExtra("list_id", mList.getId());
				context.startActivity(intent);
			}
		});
		return row;
		
	}
	
	public void setLists(ArrayList<MusicList> lists){
		this.lists = lists;
	}			
}
