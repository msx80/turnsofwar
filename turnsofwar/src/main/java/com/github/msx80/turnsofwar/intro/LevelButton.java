package com.github.msx80.turnsofwar.intro;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.ShapeDrawer;
import com.github.msx80.omicron.basicutils.gui.BasicButton;
import com.github.msx80.omicron.basicutils.gui.Event;
import com.github.msx80.omicron.basicutils.text.TextDrawer.Align;
import com.github.msx80.omicron.basicutils.text.TextDrawerFixed;
import com.github.msx80.turnsofwar.TurnsOfWar;

public class LevelButton extends BasicButton 
{

	private int level;
	private boolean locked;
	private boolean next;

	public LevelButton(int level, boolean locked, boolean next, Event onClick) {
		super(18, 14, onClick);
		this.level = level;
		this.locked = locked;
		this.next = next;
		
	}

	@Override
	public void draw() {
		ShapeDrawer.rect(1, 0, w-2, h, 0, locked ? Colors.SADDLEBROWN : next ? Colors.YELLOW : Colors.ORANGE);
		ShapeDrawer.rect(0, 1, w, h-2, 0, locked ? Colors.SADDLEBROWN : next ? Colors.YELLOW : Colors.ORANGE);
		if(locked)
		{
			TurnsOfWar.ICONS_SPRITES.draw(TurnsOfWar.ICON_SHEET, 5, 2, 176, 0, 0);
		}
		else
		{
			Sys.color(Colors.from(80, 80, 80));
			String lbl = ""+(level+1);
			TextDrawerFixed.DEFAULT.print(lbl, w/2+1, 4, Align.CENTER);
			TextDrawerFixed.DEFAULT.print(lbl, w/2, 4+1, Align.CENTER);
			TextDrawerFixed.DEFAULT.print(lbl, w/2-1, 4, Align.CENTER);
			TextDrawerFixed.DEFAULT.print(lbl, w/2, 4-1, Align.CENTER);
			Sys.color(Colors.WHITE);
			TextDrawerFixed.DEFAULT.print(lbl, w/2, 4, Align.CENTER);
		}
	}
	
	

}
