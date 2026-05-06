package com.github.msx80.turnsofwar.game;

import java.util.List;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.palette.Tic80;
import com.github.msx80.omicron.basicutils.text.TextDrawerVariable;
import com.github.msx80.turnsofwar.TurnsOfWar;

public class UtilsToCleanup {

	private static final float MUSIC_VOLUME = 0.5f;
	private static final float SOUND_VOLUME = 1f;

	public static void circ(int x, int y) {
		TurnsOfWar.ICONS_SPRITES.draw(TurnsOfWar.ICON_SHEET, x-4, y-4, 2, 0 ,0 );
	}

	public static int printBig(String text, int x, int y, int color) {
		Sys.color(Tic80.P[color]);
		int r = TextDrawerVariable.DEFAULT.print(text, x, y);
		Sys.color(Colors.WHITE);
		return r;
	}

	public static int printSmall(String text, int x, int y, int color) {
		Sys.color(Tic80.P[color]);
		int r = TurnsOfWar.TD.print(text, x, y);
		Sys.color(Colors.WHITE);
		return r;
	}

	public static void sfx(int id) {

		Sys.sound(id, SOUND_VOLUME, 1f);
	}
	
	
	public static void sfxRandom(int id) {
		Sys.sound(id, SOUND_VOLUME, (float)(1d -0.1d+ Math.random()*0.2d));
		
	}


	public static void richPrint(List<Object> tokens, int sx, int sy) {
	    for (int n = 0; n < tokens.size(); n += 2) {
	        int c = (Integer) tokens.get(n);
	        Object content = tokens.get(n + 1);
	        int w;

	        if (c < 0) {
	            // If color is negative, treat content as Sprite ID 
	            // and (0 - c) as the transparency color key.
	            int spriteId = (content instanceof Integer) ? (Integer) content : Integer.parseInt(content.toString());
	            TurnsOfWar.ICONS_SPRITES.draw(TurnsOfWar.ICON_SHEET, sx, sy-1, spriteId, 0,0);
	            
	            w = 8; // Sprites are 8 pixels wide
	        } else {
	            // Standard text printing
	            String str = content.toString();
	            // TIC80.print in your setup likely returns the width of the printed text
	            w = UtilsToCleanup.printSmall(str, sx, sy, c);
	        }
	        
	        sx += w;
	    }
	}

	public static void music(int track) {
		UtilsToCleanup.music(track, true);
	}
	
	public static void music(int track, boolean loop) {
		Sys.music(track, MUSIC_VOLUME, loop);
		
	}

	public static int[] btoi(byte[] binfile) {
		int[] res = new int[binfile.length];
		for (int i = 0; i < binfile.length; i++) {
			res[i] = Byte.toUnsignedInt(binfile[i]);
		}
		return res;
	}

}
