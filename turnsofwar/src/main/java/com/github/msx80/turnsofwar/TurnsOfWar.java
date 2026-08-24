package com.github.msx80.turnsofwar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.msx80.omicron.api.Pointer;
import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.MapDrawer;
import com.github.msx80.omicron.basicutils.ShapeDrawer;
import com.github.msx80.omicron.basicutils.SpriteSheet;
import com.github.msx80.omicron.basicutils.palette.Tic80;
import com.github.msx80.omicron.basicutils.text.TextDrawerVariable;
import com.github.msx80.tilefest.SheetByNameLoader;
import com.github.msx80.tilefest.TileMap;
import com.github.msx80.turnsofwar.game.Actions;
import com.github.msx80.turnsofwar.game.Coord;
import com.github.msx80.turnsofwar.game.Effects;
import com.github.msx80.turnsofwar.game.Event;
import com.github.msx80.turnsofwar.game.Game;
import com.github.msx80.turnsofwar.game.Unit;
import com.github.msx80.turnsofwar.game.UtilsToCleanup;
import com.github.msx80.turnsofwar.intro.IntroLoop;
import com.github.msx80.turnsofwar.intro.Loop;
import com.github.msx80.turnsofwar.intro.OptionsLoop;
import com.github.msx80.turnsofwar.ui.Button;
import com.github.msx80.turnsofwar.ui.Dialog;
import com.github.msx80.turnsofwar.ui.Tooltip;


/*
 * TODO:
 * V Win/lose conditions
 * V Caricamento nuovi livelli
 * V Suoni e musica
 * V animazioni mancanti
 * V migliorare tileset
 * V Pagina iniziale di scelta livello
 * V gestione angoli tileset
 * V finalizzare bene i livelli
 * V traps
 * V Livello coi due arcieri: gli scheletri stanno li davanti e non fanno il giro?? su TIC80 fanno il giro
 *   V Rifare qdistToNearestEnemyAStart
 * V dialog
 * V pulsanti
 * V icone in giro
 * V richtext
 * V Locked levels
 * MIGLIORIE:
 * - tooltip non generati ogni frame
 * - bottoni non generati ogni frame
 * - passaggio animazioni a AnimationManager
 * 
 * IDEE:
 * V Stonecaster: create stones where he wants
 * - Trapper: makes unit unable to move (c'e' gia' ensnare)
 * - Monsters that explodes dealing damage around (red slime)
 * - Poison effect
 * 
 */

public class TurnsOfWar implements com.github.msx80.omicron.api.Game {

	public static final SpriteSheet UNIT_SPRITES = new SpriteSheet(16, 16, 8);
	public static final SpriteSheet ICONS_SPRITES = new SpriteSheet(8, 8, 16);
	public static final int UNIT_SHEET = 1;
	public static final int ICON_SHEET = 3;
	
	public static TextDrawerVariable TD;
	public static TileMap globalMap;
	
	
	public static MapDrawer mapDrawer;
	
	static Pointer mouse;
	
	public static Loop currentLoop;
	public static String buildId;
	
	@Override
	public SysConfig sysConfig() {
		return new SysConfig(240, 136, VirtualScreenMode.FILL_SIDE, "Turns of War", "turnsofwar");
	}

	@Override
	public void init() throws Exception 
	{
		buildId = new String(Sys.binfile(1));
		
		
		globalMap = TileMap.load(Sys.binfile(2), new SheetByNameLoader());
		TD = new TextDrawerVariable(4, 4, 6, 1);
		
		mouse = Sys.pointers()[0];
		
		currentLoop = new IntroLoop();
	}

	
	
	@Override
	public boolean loop() throws Exception {
		currentLoop.loop();
		return true;
	}
	
	public static void drawInfo(Unit u, int x, int y) {

	    UtilsToCleanup.printBig(u.type.name, x, y, 15);
	    
	    TurnsOfWar.UNIT_SPRITES.draw(TurnsOfWar.UNIT_SHEET, x, y+8, u.type.spr, 0,0);
	    
	    // 2. Stats (HP, Attack, Range)
	    if (u.hp > 0) {
	        UtilsToCleanup.printSmall(u.hp + "/" + u.type.hp, x + 17, y + 8, 14);
	        Game.tooltip(x + 17, y + 8, 15, 6, "Health points left and total");
	    }

	    if (u.type.attack != null) {
	        String atkRange = u.type.attack.getMin() + "-" + u.type.attack.getMax();
	        UtilsToCleanup.printSmall(atkRange, x + 17, y + 14, 6);
	        Game.tooltip(x + 17, y + 14, 15, 6, "Attack range, min and max");
	    }

	    if (u.type.range > 0) {
	        UtilsToCleanup.printSmall(String.valueOf(u.getRange()), x + 17, y + 20, 5);
	        Game.tooltip(x + 17, y + 20, 15, 6, "Distance of ranged attack or magic");
	    }

	    y += 25;

	    // 3. Flavor Text
	    if (u.type.flavor != null && u.type.flavor.length >= 2) {
	        UtilsToCleanup.printSmall(u.type.flavor[0], x, y, 3);
	        UtilsToCleanup.printSmall(u.type.flavor[1], x, y + 6, 3);
	    }
	    y += 13;

	    // 4. Capabilities (Actions, Cooldowns, Frees)
	    for (Actions a : u.type.cap) {
	        String tp = a.desc;
	        List<Object> tok = new ArrayList<>(List.of(-1, a.icon, 15, " " + a.name));

	        // Handle Free uses logic
	        if (u.frees != null) {
	            Integer q = u.frees.get(a);
	            if (q != null && q > 0) {
	                tok.add(14);
	                tok.add(" +" + q);
	                tp += " * " + q + " free (doesn't spend unit)";
	            }
	        }

	        // Handle Cooldown logic
	        if (a.cooldown > 0) {
	            int q = u.cooldown.getOrDefault(a, 0);
	            tok.add(q == 0 ? 14 : 6); // Green if ready, Red if cooling
	            tok.add(" " + q + "/" + a.cooldown);
	            tp += (q == 0) ? " * Cooldown: Ready" : " * Cooldown: " + q + " turns left";
	        }

	        UtilsToCleanup.richPrint(tok, x, y + 1);
	        Game.tooltip(x, y + 1, 60, 8, tp);
	        y += 9;
	    }

	    // 5. Active Effects
	    for (Map.Entry<Effects, Integer> entry : u.eff.entrySet()) {
	        y += 7;
	        Effects eff = entry.getKey();
	        int val = entry.getValue();

	        ICONS_SPRITES.draw(ICON_SHEET, x - 1, y - 2, eff.spr, 0, 0);
	        String turns = (val > 0) ? String.valueOf(val) : "A"; // "A" for Area effect
	        
	        UtilsToCleanup.richPrint(Arrays.asList(15, eff.name, 14, " [" + turns + "]"), x + 7, y);
	        String effectDesc = eff.desc + (val > 0 ? "" : " (area effect)");
	        Game.tooltip(x + 7, y, 60, 8, effectDesc);
	    }

	    // 6. Action Button (Pass Turn)
	    y = 100;
	    if (u.team == 0) {
	        Game.button(x+10, y, "Pass", "Don't use this unit this turn.", () -> {
	            u.addEffect(Effects.SPENT);
	            UtilsToCleanup.sfx(2);
	            Game.moveEnded(); // Helper to refresh UI/state
	            
	        });
	    }
	}
	

	private static void gameTIC() {
		
		
		
		Game.tooltips.clear();
		int tx = mouse.x() / 16;
		int ty = mouse.y() / 16;
		
		// pick up next event
		Game.pickUpEvent();
		
		if (Game.anim != null) 
		{
		
			if(Game.anim.isFinished() || !Game.anim.update())
			{
				Game.anim = null; 
				
				// animation just finished. Check the game status
				Game.progressGame();
			}
			
		}
	
		
		if(Game.anim == null)
		{
			if(Game.dialog != null)
			{
				
				  if (mouse.btnp(0) && !Game.dialog.animating())
				  {
					  Runnable a = Game.dialog.onClose;
					  Game.dialog = null;
					  int oldLvl = Game.lvlNum;
					  if(a!=null) a.run();
					  // hack: if level changed in dialog, then we cannot call progressGame
					  // and in general is better to start a fresh gameTic.
					  if(Game.lvlNum != oldLvl)
					  {
						  gameTIC();
						  return;
					  }
					  Game.progressGame();
				  }
				
			}
			else
			{
				Actions action = null;
				if(Game.targets!= null) 
				{
					List<Actions> afds = Game.targets.get(Game.idx(tx, ty));
					action = afds == null ? null : afds.get(0);
				}
				
				if (mouse.btnp(0) && action== null) {
					  if (mouse.x()<=176)
					  {
							   Game.selectUnit(Game.unitAt(tx,ty));
					  }else
					  {
							   // check buttons
							   for (Button b : Game.buttons) {
								if(b.inside(mouse.x(), mouse.y()))
								{
									b.onClick.run();
									// siccome non disegnamo ne facciamo niente, richiamiamo gameTic in modo da non perdere il frame.
									Game.buttons.clear();
									gameTIC();
									return;
								}
							}
					  }
				} 
				else if ( mouse.btnp(0) && Game.selected!=null && (mouse.x()<=176) && Game.targets!=null && (Game.selected.team == Game.currentTeam) ) 
				{
					if (action != null) {
						Event e = action.exec(Game.selected, tx, ty);
						Game.appendEvent(e);
					}
				}
			}
		}
		

		// START OF DRAWING STUFF
		Sys.clear(Colors.BLACK);
		Sys.draw(6, 0, 0, 0,0, 240, 136, 0,0);

		mapDrawer.draw(2, 0, 0, 0,0, 11, 8);
		
		if ((tx<11) && (ty<8))
		{
			ShapeDrawer.outline(tx*16, ty*16, 16, 16, 0, Colors.YELLOW);
		}
		
		for(Unit u : com.github.msx80.turnsofwar.game.Game.units)
		{
			drawUnit(u);
		}
		
		if (Game.selected != null) {
		  Game.buttons.clear(); // clean at the end
		  drawInfo(Game.selected, 180, 1);
		}
		
		Sys.color(Colors.DARKKHAKI);
		TurnsOfWar.TD.print("Turn "+Game.turnCount, 190, 111);
		Sys.color(Colors.WHITE);
		
		Game.button(190,118,"Options", "Open options menu.", () -> {
			currentLoop = new OptionsLoop();
			return;
		});
		
		if (Game.anim == null && Game.dialog == null && Game.targets != null) {
			  drawTargets();
		}
		
		if(Game.anim != null) Game.anim.draw();
			
		// t=t+1
		Game.jumpt++;
		
		if(Game.radipix!=null && Game.dialog == null && Game.anim == null)
		{
			for (Coord c : Game.radipix) {
				Sys.fill(0, c.getX(), c.getY(),1,1, Tic80.DARK_GREEN );
			}
		
		}
		
		for (Tooltip t : Game.tooltips) {
			if(t.inside(mouse.x(), mouse.y()))
			{
				UtilsToCleanup.printSmall(t.text,0,16*8+2, 15);
			}
		}
	    
		
		if (Game.dialog != null ) printDialog(Game.dialog);
		
	}


	private static void printDialog(Dialog d) 
	{
		
		if(d.animating())
		{
			double perc = d.perc();
   		 	ShapeDrawer.rect((int) Game.inter(88,d.x,perc), (int) (d.y*perc), (int) (d.w*perc), (int) (d.h*perc), 0, Tic80.P[d.type.colors[0]]);
   		 	ShapeDrawer.outline((int)Game.inter(88,d.x,perc)+1, (int) (d.y*perc+1), (int) (d.w*perc-2), (int) (d.h*perc-2), 0, Tic80.P[d.type.colors[1]]);
   		 	d.timer++;
		}
		else
		{
		
		  ShapeDrawer.rect(d.x, d.y, d.w, d.h, 0, Tic80.P[d.type.colors[0]]);
		  ShapeDrawer.outline(d.x+1, d.y+1, d.w-2, d.h-2, 0, Tic80.P[d.type.colors[1]]);
		  
		  int i = 1;
		  for (String line : d.strs) {
				  UtilsToCleanup.printBig(line, d.x+5, d.y+5+(i-1)*7,(i==1) ? d.type.colors[2] : d.type.colors[3]);
				  i++;
			}
		}
	}

	private static void drawTargets() {
		for (int x = 0; x <= 10; x++) {
	        for (int y = 0; y <= 7; y++) {
			      List<Actions> at = Game.targets.get(Game.idx(x, y));
				  if(at!=null) {
					  ICONS_SPRITES.draw(ICON_SHEET,  x*16+4, y*16+4,at.get(0).icon, 0,0);
				  }
	        }
		}
	}

	private static void drawUnit(Unit u) {
		float ax = u.ax;
		float ay = u.ay;

		// little jumps
		if (u == Game.selected && Game.anim == null && Game.targets!=null && Game.dialog==null && u.team == 0)
		{
		  ay = (float) (-Math.abs(Math.sin(Game.jumpt/10f))*3f);
		  ax = 0;
		}
		
	  // effects icons
	  int dx = (7 * u.eff.size()) / 2; 
	  for (Effects e : u.eff.keySet()) {
		  dx = dx-7;
		  ICONS_SPRITES.draw(ICON_SHEET, Math.round( u.x*16+ax -dx ), Math.round( u.y*16+ay-8), e.spr, 0, 0);
	  }
	  
	  int dir = 0;
	  if (u.team == 1)  dir = 1;
	  UNIT_SPRITES.draw(TurnsOfWar.UNIT_SHEET, Math.round(u.x*16+ax),Math.round(u.y*16+ay), u.type.spr , 0, dir);

	  String tp = u.type.name;
	  if(u.hp > 0)
	  {
		  tp = tp +" "+u.hp+"/"+u.type.hp;  
	  }
	  Game.tooltip(u.x*16, u.y*16, 16, 16, tp);
	 
	}

	public static void startLevel(int lev) {
		com.github.msx80.turnsofwar.game.Game.makeGame(lev);
		TurnsOfWar.mapDrawer = new MapDrawer(16, 16, 8, com.github.msx80.turnsofwar.game.Game.map);
		currentLoop = TurnsOfWar::gameTIC;
	}

	public static void resumeGame() {
		currentLoop = TurnsOfWar::gameTIC;
	}

}
