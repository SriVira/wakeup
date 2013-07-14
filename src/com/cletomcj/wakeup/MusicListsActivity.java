package com.cletomcj.wakeup;

import java.util.ArrayList;

import com.cletomcj.wakeup.adapters.DatabaseAdapter;
import com.cletomcj.wakeup.adapters.MyListAdapter;
import com.cletomcj.wakeup.dialogs.AddListDialog;
import com.cletomcj.wakeup.dialogs.EditListDialog;
import com.cletomcj.wakeup.listeners.OnListAddListener;
import com.cletomcj.wakeup.listeners.OnListEditListener;
import com.cletomcj.wakeup.retos.MusicList;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * This is the Acivity wich allows the user create, remove and edit the music lists
 * Shows on the screen all music lists 
 * @author Carlos Martin-Cleto
 *
 */
public class MusicListsActivity extends FragmentActivity implements OnClickListener, OnListAddListener, 
OnListEditListener{
	
	private ListView listView1;
	private DatabaseAdapter databaseAdapter;
	private MyListAdapter adapter;
	private ArrayList<MusicList> lists; 
	//this field allows know which item has been selected on the 
	//context menu
	private static MusicList listSelected;  
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musics);
		databaseAdapter  = new DatabaseAdapter(this);
		//Creates and/or open the database
		if(!databaseAdapter.isOpen()){
		databaseAdapter.open();
		}
		//Get all lists 
		lists = databaseAdapter.getLists();
		//Adapts all MusicLists to the ListView
		adapter = new MyListAdapter(this, R.layout.item_row, lists);
		listView1 = (ListView)findViewById(R.id.listView1);
		View header = (View)getLayoutInflater().inflate(R.layout.add_list_row, null);
		Button addList = (Button)header.findViewById(R.id.add_list_button);
		addList.setOnClickListener(this);

		listView1.addHeaderView(header);
		listView1.setAdapter(adapter);
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		databaseAdapter.close();
	}
	
	/**
	 * This method is called when a list is added or removed to see immediately
	 * the change on the screen, using the UI Thread and the method "notifyDataSetChanged" 
	 * 
	 */
	public void refreshList(){
		//the database has been modified is needed to update variable "lists"
		lists = databaseAdapter.getLists();
		//also is needed to update the adapter
		adapter.setLists(lists);
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	//any View reflecting the data should refresh itself
	        	adapter.notifyDataSetChanged();
	        }
	    });	
	}
	
	
	@Override
	public void onClick(View v){
		AddListDialog dialog = new AddListDialog();
		dialog.show(getSupportFragmentManager(), "Nueva Lista");

	}
	
	@Override
	public void onListAdd(String title){
		databaseAdapter.insertList(title);
		refreshList();
	}
	
	@Override
	public void onListEdit(String nTitle){
		listSelected.setTitle(nTitle);
		databaseAdapter.updateList(listSelected);
		refreshList();
	}
	
	
	/**
	 * This method creates the context menu on long click  
	 * and get the MusicList selected.
	 * 
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    //we set the ID of each item with its position on MyListAdapter and in 
	    //this way we only need to call the public method "getItem(position)"
	    listSelected = adapter.getItem(v.getId());
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.list_selected_menu, menu);
	}
	
	/**
	 *  This method do the right actions according to the 
	 *  option selected on the menu
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.delete_list:
	        	long id = listSelected.getId();
	    		databaseAdapter.deleteList(id);
	    		refreshList();
	            return true;
	        case R.id.edit_name:
	    		EditListDialog dialog = new EditListDialog();
	    		dialog.show(getSupportFragmentManager(), "Editar Nombre");
	    		return true;
	        case R.id.select_this:
	        	listSelected.setPlaylist(1);
	        	databaseAdapter.updateList(listSelected);
	        	refreshList();
	    		return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	public static MusicList getItemSelected(){
		return listSelected;
	}
}