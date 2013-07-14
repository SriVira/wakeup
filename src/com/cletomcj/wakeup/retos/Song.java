package com.cletomcj.wakeup.retos;

public class Song {
	
	private String title;
	private String path; //the absolute path on Android system
	private long list_id; //the ID of the list which belongs to 
	private long id; //the ID of the song on the database
	
	public Song(){
	}
	
	public Song(String title, String path, long list_id){
		this.title = title;
		this.path = path;
		this.list_id = list_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getListId() {
		return list_id;
	}

	public void setListId(long list_id) {
		this.list_id = list_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
