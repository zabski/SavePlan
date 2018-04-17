package com.zmobile.saveplan;


import android.graphics.Color;

public class ListItem {
	
	public String title;
	public String text;
	public String img;
	public int col;
	
	public ListItem(String title, String text, String img, int col) {
		this.title = title;
		this.text = text;
		this.img = img;
		this.col = col;
	}
}