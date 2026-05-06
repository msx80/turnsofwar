package com.github.msx80.turnsofwar.ui;

public class Button extends Tooltip {

	public Runnable onClick;

	public Button(String text, int x, int y, int w, int h, Runnable onClick) {
		super(text, x, y, w, h);
		this.onClick = onClick;
	}

}
