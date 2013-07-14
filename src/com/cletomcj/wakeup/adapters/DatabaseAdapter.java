package com.cletomcj.wakeup.adapters;

import java.util.ArrayList;

import com.cletomcj.wakeup.retos.MusicList;
import com.cletomcj.wakeup.retos.Song;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseAdapter {
	private static final String DATABASE_NAME = "wakeup";
	private static final int DATABASE_VERSION = 3;
	private static final String TABLE_SONGS = "songs";
	private static final String TABLE_LISTS = "lists";
	
	// Columns of tables
	public static final String ID = "_id"; //Both tables  
	public static final String NAME = "name"; //Both tables
	public static final String PATH = "path"; //only SONGS table
	public static final String LIST_ID = "list_id"; //only SONGS table
	public static final String IS_PLAYLIST  = "is_playlist"; //only LISTS table
	
	private final Context mContext;
	//DatabaseHelper is needed to create an update the database versions
	private static DatabaseHelper mDBHelper;
	private static SQLiteDatabase mDataBase;
	
	public DatabaseAdapter(Context ctx) {
		mContext = ctx;
		mDBHelper = new DatabaseHelper(mContext);
	}

	//------------------ DATABASE HELPER CLASS ---------------------------------------------------
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		//This method is called when the "getWritableDataBase" method is called
		@Override
		public void onCreate(SQLiteDatabase db) {
			//create the TABLE_SONGS
			db.execSQL("create table " + TABLE_SONGS + " " + "("
					+ ID + " integer primary key autoincrement, " + LIST_ID + " integer, "
					+ NAME + " text, " + PATH + " text " + ");" );
			//create the TABLE_LISTS
			db.execSQL("create table " + TABLE_LISTS + " " + "("
					+ ID + " integer primary key autoincrement, "
					+ NAME + " text, " + IS_PLAYLIST + " integer " + ");" );	
		}
		
		//When the structure of the database has been modified It is created again
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
			onCreate(db);
		}	
	}
    //---------------------------- END OF DATABASE HELPER CLASS ----------------------------------------
	
	// ---opens the database---
	public void open() throws SQLException {
		mDataBase = mDBHelper.getWritableDatabase();
	}
	
	// ---closes the database---
	public void close() {
		if((mDataBase != null) && (mDataBase.isOpen())){	
		mDBHelper.close();
		}
	}
	
	public long insertList(String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, name);
		initialValues.put(IS_PLAYLIST, 0);
		return mDataBase.insert(TABLE_LISTS, null, initialValues);
	}
	
	public ArrayList<MusicList> getLists(){
		ArrayList<MusicList> lists = new ArrayList<MusicList>();
		//with this cursor we will be able to look into the database
		Cursor cursor = mDataBase.query(true, TABLE_LISTS, null, null, null, null, null, null,
										null);
		if (cursor.moveToFirst()) {
			do {
				MusicList mList = new MusicList();
				mList.setId(cursor.getLong(cursor.getColumnIndex(ID)));
				mList.setTitle(cursor.getString(cursor.getColumnIndex(NAME)));
				mList.setPlaylist(cursor.getInt(cursor.getColumnIndex(IS_PLAYLIST)));
				lists.add(mList);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return lists;
	
	}
	
	public boolean deleteList(long id){
		mDataBase.delete(TABLE_SONGS, LIST_ID + "=" + id, null);
		mDataBase.delete(TABLE_LISTS, ID + "=" + id, null);
		return true;
	}
	
	public boolean deleteSong(long id){
		mDataBase.delete(TABLE_SONGS, ID + "=" + id, null);
		return true;
	}
	
	
	public boolean updateList(MusicList ml) {
		ContentValues args = new ContentValues();
		args.put(NAME, ml.getTitle());
		args.put(IS_PLAYLIST, ml.isPlaylist());
		mDataBase.update(TABLE_LISTS, args, ID + "=" + ml.getId(), null);
		//only one can be the playlist
		if(ml.isPlaylist() == 1){
			ContentValues nArg = new ContentValues();
			nArg.put(IS_PLAYLIST, 0);
			mDataBase.update(TABLE_LISTS, nArg, ID + "!=" + ml.getId(), null);
		}
		return true;	
	}
	
	public ArrayList<Song> getSongs(long id){
		ArrayList<Song> songs = new ArrayList<Song>();
		//with this cursor we will be able to look into the database
		Cursor cursor = mDataBase.query(true, TABLE_SONGS, null, LIST_ID + "=" + id, null, null, null, null,
										null);
		if (cursor.moveToFirst()) {
			do {
				Song song = new Song();
				song.setListId(cursor.getLong(cursor.getColumnIndex(LIST_ID)));
				song.setTitle(cursor.getString(cursor.getColumnIndex(NAME)));
				song.setPath(cursor.getString(cursor.getColumnIndex(PATH)));
				song.setId(cursor.getLong(cursor.getColumnIndex(ID)));
				songs.add(song);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return songs;
	
	}
	
	public long insertSong(Song song) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, song.getTitle());
		initialValues.put(PATH, song.getPath());
		initialValues.put(LIST_ID, song.getListId());
		return mDataBase.insert(TABLE_SONGS, null, initialValues);
	}
	
	public long getSelectedListId(){
		Cursor cursor = mDataBase.query(true, TABLE_LISTS, null, IS_PLAYLIST + "=" + 1, null, null, null, null, null);
		cursor.moveToFirst();
		long id = cursor.getLong(cursor.getColumnIndex(ID));
		cursor.close();
		return id;
	}
	
	public boolean isOpen(){
		if(mDataBase != null){
		return mDataBase.isOpen();
		}else{
		return false;
		}
	}	
	
}
