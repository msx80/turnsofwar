package com.github.msx80.turnsofwar.ui;

public class Tooltip {

	public String text;
	public int x, y, w, h;
	
	public Tooltip(String text, int x, int y, int w, int h) {
		super();
		this.text = text;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public boolean inside(int x, int y) 
	{
		Tooltip r = this;
	    return x >= r.x && 
	           y >= r.y && 
	           x < (r.x + r.w) && 
	           y < (r.y + r.h);
	}
	
	
}
