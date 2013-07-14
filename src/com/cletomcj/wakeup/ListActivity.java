package com.cletomcj.wakeup;

import java.io.File;
import java.util.ArrayList;

import com.cletomcj.wakeup.adapters.DatabaseAdapter;
import com.cletomcj.wakeup.adapters.MySongsAdapter;
import com.cletomcj.wakeup.retos.Song;
import com.ipaulpro.afilechooser.utils.FileUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;

public class ListActivity extends Activity implements OnClickListener{
	
	private static final int RESULT_CHOOSER = 333; // onActivityResult request code
	
	private ArrayList<Song> songs;
	private MySongsAdapter adapter;
	private ListView listView2;
	private long list_id;
	private DatabaseAdapter databaseAdapter;
	private static Song songSelected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		//we catch the ID of the parent list of the songs
		list_id = getIntent().getLongExtra("list_id", 0);
		databaseAdapter  = new DatabaseAdapter(this);
		songs = databaseAdapter.getSongs(list_id);
		adapter = new MySongsAdapter(this, R.layout.item_row, songs);
		listView2 = (ListView)findViewById(R.id.listView2);
		View header2 = (View)getLayoutInflater().inflate(R.layout.add_song_row, null);
		Button addSong = (Button)header2.findViewById(R.id.add_song_button);
		addSong.setOnClickListener(this);

		listView2.addHeaderView(header2);
		listView2.setAdapter(adapter);
	}
	
	/**
	 * When the user clicks on the "add Song" button, a File Chooser Dialog appears
	 * This File Chooser Dialog is obtained from an external library called "aFileChooser" 
	 * library. 
	 */
	@Override
	public void onClick(View v){
		//These methods belong to the external library "aFileChooser"
	    Intent getContentIntent = FileUtils.createGetContentIntent();
	    Intent intent = Intent.createChooser(getContentIntent, "Select a file");
		try {
			startActivityForResult(intent, RESULT_CHOOSER);
		}catch (ActivityNotFoundException e) {
			// The reason for the existence of aFileChooser
			Log.e("wakeup", "Unable to launch de file chooser activity");
		}	
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RESULT_CHOOSER:	
			// If the file selection was successful
			if (resultCode == RESULT_OK) {	
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();
					try {
						final File file = FileUtils.getFile(uri);
						final String path = file.getAbsolutePath();
						final String title = file.getName();
						addSong(title, path);
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error", e);
					}
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void addSong(String title, String path){
		if (!title.endsWith(".mp3")){
			Toast.makeText(this, R.string.wrong_file, Toast.LENGTH_SHORT).show();
		}else{
			Song song = new Song(title, path, list_id);
			databaseAdapter.insertSong(song);
			refreshList();
			
		}	
	}
	
	public void refreshList(){
		//the database has been modified is needed to update variable "songs"
		songs = databaseAdapter.getSongs(list_id);
		//also is needed to update the adapter
		adapter.setSongs(songs);
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	//any View reflecting the data should refresh itself
	        	adapter.notifyDataSetChanged();
	        }
	    });	
	}
	
	/**
	 * This method creates the context menu on long click  
	 * and get the Song selected.
	 * 
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    //we set the ID of each item with its position on MySongAdapter and in 
	    //this way we only need to call the public method "getItem(position)"
	    songSelected = adapter.getItem(v.getId());
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.song_selected_menu, menu);
	}
	
	/**
	 *  This method do the right actions according to the 
	 *  option selected on the menu
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.delete_song:
	        	long id = songSelected.getId();
	    		databaseAdapter.deleteSong(id);
	    		refreshList();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

}
