package com.github.msx80.turnsofwar.intro;

import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.gui.Button;
import com.github.msx80.omicron.basicutils.gui.Event;
import com.github.msx80.omicron.basicutils.gui.drawers.OutlineRectangle;

public class AButton extends Button {

	public AButton(String text, Event onClick) 
	{
		super(text, onClick);
		this.setBg(new OutlineRectangle(Colors.SADDLEBROWN, Colors.DARKORANGE));
		this.w = 100;
	}

}
