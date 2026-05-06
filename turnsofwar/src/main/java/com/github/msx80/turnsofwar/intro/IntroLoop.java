package com.github.msx80.turnsofwar.intro;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.gui.WidgetManager;
import com.github.msx80.omicron.basicutils.text.TextDrawer.Align;
import com.github.msx80.omicron.basicutils.text.TextDrawerVariable;
import com.github.msx80.turnsofwar.TurnsOfWar;
import com.github.msx80.turnsofwar.game.Game;
import com.github.msx80.turnsofwar.game.Levels;
import com.github.msx80.turnsofwar.game.UtilsToCleanup;

public class IntroLoop implements Loop {
	
	WidgetManager wm;
	
	public void init()
	{
		wm = new WidgetManager(240, 136);
		int nl = Game.nextUnlockedLevel();
		
		for (int i = 0; i < Levels.ALL.size() -1 ; i++) {
			int tx = i % 9;
			int ty = i / 9;
			int x = tx*20 + 30;
			int y = ty*16 + 65;
			
			final int lev = i;
			
			boolean locked = i>nl;
			LevelButton b = new LevelButton(i, locked, i==nl, e -> {
				
				if(locked)
				{
					
				}
				else
				{
					TurnsOfWar.startLevel(lev);
				}
				
			});
			
			wm.add(b, x, y);		
		}
	}

	
	public void loop()
	{
		wm.update();
		
		
		Sys.clear(Colors.BLACK);
		Sys.draw(5, 0, 0, 0, 0, 240, 136, 0, 0);
		UtilsToCleanup.printBig("A turn based strategy game by msx80", 40, 30, 15);
		UtilsToCleanup.printBig("Graphics: Stavros    Music/sfx: Fubuki ", 40, 37, 15);
		
		// UtilsToCleanup.printBig("Choose a LEVEL:", 220, 50, 15, Align.CENTER);
		TextDrawerVariable.DEFAULT.print("Choose a LEVEL:", 120, 54, Align.CENTER);
		UtilsToCleanup.printBig("Ver 1.1  -  git:"+TurnsOfWar.buildId, 2, 130, 3);
		wm.draw();
	}

}
