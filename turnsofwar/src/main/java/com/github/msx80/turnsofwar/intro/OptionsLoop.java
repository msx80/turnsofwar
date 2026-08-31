package com.github.msx80.turnsofwar.intro;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.gui.Button;
import com.github.msx80.omicron.basicutils.gui.WidgetManager;
import com.github.msx80.turnsofwar.TurnsOfWar;
import com.github.msx80.turnsofwar.game.Game;

public class OptionsLoop implements Loop {
	
	WidgetManager wm;
	int knock = 0;
	
	public OptionsLoop()
	{
		init();
	}
	
	private void init()
	{
		int x = 70;
		int y = 30;
		int dist = 14;
		wm = new WidgetManager(240, 136);
		{
		Button b = new AButton("Back To Game", eb -> {
			
			TurnsOfWar.resumeGame();
			
		});
		wm.add(b, x, y); y+=dist;
		}
		
		{
		Button b = new AButton("Restart Level", eb -> {
			
			TurnsOfWar.startLevel(Game.lvlNum);
			
		});
		wm.add(b, x, y); y+=dist;
		}

		{
		Button b = new AButton("Quit To Main Menu", eb -> {
			
			TurnsOfWar.currentLoop = new IntroLoop();
			
		});
		wm.add(b, x, y); y+=dist;
		}

		{
		Button b = new AButton("Quit Game", eb -> {
			
			Sys.hardware("com.github.msx80.omicron.plugins.builtin.PlatformPlugin", "QUIT", null);
			
		});
		wm.add(b, x, y); y+=dist;
		}
	
	}

	
	public void loop()
	{
		wm.update();
		
		
		Sys.clear(Colors.BLACK);
		wm.draw();
	}

}
