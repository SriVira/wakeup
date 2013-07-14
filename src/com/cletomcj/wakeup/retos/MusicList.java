package com.cletomcj.wakeup.retos;

public class MusicList {
	
	private String title;
	private long id;
	private int isPlaylist;
	
	public MusicList(){
		isPlaylist = 0;
	}
	
	public String getTitle(){
		return title;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public void setTitle(String title){
		this.title = title;
	}

	public int isPlaylist() {
		return isPlaylist;
	}

	public void setPlaylist(int isPlaylist) {
		if(isPlaylist == 0 || isPlaylist ==1)
		this.isPlaylist = isPlaylist;
	}

}
