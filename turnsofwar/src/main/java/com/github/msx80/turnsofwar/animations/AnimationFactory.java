package com.github.msx80.turnsofwar.animations;
import java.util.List;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.turnsofwar.TurnsOfWar;
import com.github.msx80.turnsofwar.animations.Animation.FrameAction;
import com.github.msx80.turnsofwar.animations.EasingFunctions.Easing;
import com.github.msx80.turnsofwar.game.Coord;
import com.github.msx80.turnsofwar.game.Unit;
import com.github.msx80.turnsofwar.game.Units;
import com.github.msx80.turnsofwar.game.UtilsToCleanup;

public class AnimationFactory {

    // Helper for "AnimTxt" (Floating damage/text)
    public static Animation makeAnimTxt(String text, int x, int y, int col) {
        return new Animation((perc, time) -> {
            int offset = (int)(perc * 10);
            // Outline effect (mimicking the 4 offsets in Lua)
            UtilsToCleanup.printBig(text, x - 1, y - offset, 0);
            UtilsToCleanup.printBig(text, x + 1, y - offset, 0);
            UtilsToCleanup.printBig(text, x, y - offset + 1, 0);
            UtilsToCleanup.printBig(text, x, y - offset - 1, 0);
            // Main text
            UtilsToCleanup.printBig(text, x, y - offset, col);
        }, 50, EasingFunctions.LINEAR, null);
    }

    public static Animation makeParticleAnim(int x, int y, int color) {
        // 226 32 53
        return new Animation((perc, time) -> {
            
            int f = (int) (perc*25);
            perc = perc * 10d;
            double cy = ( (perc*perc)- 10d*perc+5d ) /2d +3;
            // Draw the 6 blood circles
            Sys.color(color);
            UtilsToCleanup.circ(x + f/3, y + (int)cy);
            UtilsToCleanup.circ(x + f/5, y + (int)(cy * 1.5));
            UtilsToCleanup.circ(x - f/3, y + (int)(cy * 1.5));
            UtilsToCleanup.circ(x - f/5, y + (int)(cy));
            Sys.color(Colors.WHITE);

        }, 30, EasingFunctions.LINEAR, null);
        
    	
    	// return new Animation((a, b) -> {}, 10, EasingFunctions.LINEAR, null);
    }

    // Base wrapper for Unit-specific animations
    public static Animation makeUnitAnim(Unit u, FrameAction action, int ttl, Easing easing, Runnable onEnd) {
        return new Animation(action, ttl, easing, () -> {
            u.ax = 0; u.ay = 0;
            if (onEnd != null) onEnd.run();
        });
    }

    public static Animation makeUnitMoveAnim(Unit uu, int tx, int ty, Runnable onEnd) {
        int dx = tx - uu.x;
        int dy = ty - uu.y;
        return makeUnitAnim(uu, (perc, time) -> {
            uu.ax = (float)(perc * 16 * dx);
            uu.ay = (float)(perc * 16 * dy);
        }, 40, EasingFunctions.QUADRATIC_IN_OUT, onEnd);
    }

    public static IAnimation makeShakeAnim(Unit u) {
        return makeUnitAnim(u, (perc, time) -> {
            u.ax = (float)(Math.random() * 2 - 1);
            u.ay = (float)(Math.random() * 2 - 1);
        }, 30, EasingFunctions.LINEAR, null);
    }

    public static IAnimation makeShakeAndTxtAndParticleAnim(Unit u, String txt, int color, int particleRgb) {
        return Animation.parallel(
            makeShakeAnim(u),
            Animation.parallel(
                makeParticleAnim(u.x * 16 + 8, u.y * 16 + 8, particleRgb),
                makeAnimTxt(txt, u.x * 16 + 8 - 5, u.y * 16, color), 
                null
            ),
            null
        );
    }

    public static IAnimation makeUnitMagicAnim(Unit src, Unit target, String dmg, boolean isGood, Runnable onEnd) {
    	
        Animation spellProjectile = makeProjectileAnim(src, target.getCoord(), isGood);

        // 2. Return the sequence: Projectile -> Shake/Text -> onEnd callback
        return Animation.sequential(
            spellProjectile,
            makeShakeAndTxtAndParticleAnim(target, dmg, 11, isGood ? Colors.LAWNGREEN : Colors.BLUEVIOLET),
            onEnd
        );
        
    }

	public static Animation makeProjectileAnim(Unit src, Coord target, boolean isGood) {
		// Calculate the distance vector between units
        float dx = target.x - src.x;
        float dy = target.y - src.y;

        // 1. Create the projectile animation
        Animation spellProjectile = new Animation(
            (perc, time) -> {
                // TIC80 spr call: spr(id, x, y, colorkey)
                // Perc (0.0 to 1.0) moves the sprite along the vector
            	TurnsOfWar.ICONS_SPRITES.draw(TurnsOfWar.ICON_SHEET, 
                
                    (int)(16 * (src.x + perc * dx) + 4), 
                    (int)(16 * (src.y + perc * dy) + 4),
                    1,
                    0,0
                );
            },
            30,         // Duration in frames
            EasingFunctions.LINEAR, // Linear movement
            () -> {     // OnStart callback for SFX
                if (isGood) {
                    UtilsToCleanup.sfx(4);
                } else {
                    UtilsToCleanup.sfx(11);
                }
            }
        );
		return spellProjectile;
	}
    
    
    public static IAnimation makeUnitAttackAnim(Unit uu, Unit u2, int dmg, Runnable onEnd) {
        int dx = u2.x - uu.x;
        int dy = u2.y - uu.y;

        // Sequence: Lunging forward, then returning while the target shakes/bleeds
        Animation lunge = new Animation((perc, time) -> {
            uu.ax = (float)(perc * 14d * dx);
            uu.ay = (float)(perc * 14d * dy);
        }, 30, EasingFunctions.BACK_IN, () -> UtilsToCleanup.sfx(0));

        IAnimation returnAndDamage = Animation.parallel(
            makeUnitAnim(uu, (perc, time) -> {
                uu.ax = (float)((14 - perc * 14) * dx);
                uu.ay = (float)((14 - perc * 14) * dy);
            }, 30, EasingFunctions.LINEAR, null),
            makeShakeAndTxtAndParticleAnim(u2, "-" + dmg, 6, Colors.CRIMSON),
            null
        );

        return Animation.sequential(lunge, returnAndDamage, onEnd);
    }

    public static IAnimation makeBombAnim(Unit uSource, int targetX, int targetY, List<Unit> targets, Runnable onEnd) {
        int dx = targetX - uSource.x;
        int dy = targetY - uSource.y;

        // 1. The projectile animation
        IAnimation bombFly = new Animation((perc, time) -> {
        	TurnsOfWar.ICONS_SPRITES.draw(TurnsOfWar.ICON_SHEET, (int)(16 * (uSource.x + perc * dx) + 4), (int)(16 * (uSource.y + perc * dy) + 4),48,0,0);
        }, 30, EasingFunctions.LINEAR, null)
        		.setOnBegin(() -> { UtilsToCleanup.sfx(0); });

        // 2. Parallel explosions for all targets
        IAnimation explosions = null;
        for (Unit t : targets) {
            int dmg = (targetX == t.x && targetY == t.y) ? 4 : 2;
            IAnimation a = makeShakeAndTxtAndParticleAnim(t, "-" + dmg, 6, Colors.CRIMSON);
            if (explosions == null) explosions = a;
            else explosions = Animation.parallel(explosions, a, null);
        }

        return Animation.sequential(bombFly, explosions, onEnd);
    }

    public static Animation makeTeleportAnim(Unit uu, int x, int y, Runnable onEnd) {
        int dx = x - uu.x;
        int dy = y - uu.y;
        return makeUnitAnim(uu, (perc, time) -> {
            uu.ax = (float)(perc * 16 * dx);
            uu.ay = (float)(perc * 16 * dy);
        }, 90, EasingFunctions.ELASTIC_OUT, onEnd);
    }

	public static IAnimation makeUnitRangedAnim(Unit uu, Unit u2, int dmg, Runnable onEnd) {
		int dx = u2.x-uu.x;
		int dy = u2.y-uu.y;
		
		return Animation.sequential(
				new Animation(
					(perc, time) -> TurnsOfWar.ICONS_SPRITES.draw(TurnsOfWar.ICON_SHEET,(int)(16*(uu.x+perc*dx)+4), (int)(16*(uu.y+perc*dy)+4), 0, 0, 0 )
					,
					30,
					EasingFunctions.LINEAR, null
				).setOnBegin(() -> { UtilsToCleanup.sfx(0); }),
				Animation.parallel(makeShakeAnim(u2), 
						Animation.parallel(
								makeParticleAnim(u2.x*16+8, u2.y*16+8, Colors.CRIMSON),
								makeAnimTxt("-"+dmg, u2.x*16+8-5, u2.y*16, 6), null)
						, null)
				,onEnd
				);
	}
	
	public static IAnimation makeUnitSplitAnim(Unit uu, Coord u2, String dmg, boolean good, Runnable onEnd) 
	{

		int dx = u2.x-uu.x;
		int dy = u2.y-uu.y;
		
		IAnimation inner = new Animation(
				
				( perc, time) -> {
				  
				  int dir =  (uu.team == 1) ? 1:0;
				  
				  //spr(uu.t.spr,16*(uu.x+perc*dx),16*(uu.y+perc*dy),uu.t.bg or 14,1,dir,0,2,2)
				  TurnsOfWar.UNIT_SPRITES.draw(TurnsOfWar.UNIT_SHEET, (int)Math.round(16*(uu.x+perc*dx)),(int) Math.round(16*(uu.y+perc*dy)), uu.type.spr , 0, dir);
				},
				30,
				EasingFunctions.LINEAR,
				null
			)
				;
		
		
		IAnimation inner2 = Animation.parallel(makeAnimTxt(dmg, uu.x*16+8-5, uu.y*16, 3), makeShakeAnim(uu), null)
				.setOnBegin(() -> { if (good) UtilsToCleanup.sfx(4); else UtilsToCleanup.sfx(11);});
		return Animation.sequential(inner2,inner,onEnd);
	}

	public static IAnimation makeUnitSummonAnim(Unit uu, Unit u2, String dmg, boolean good, Runnable onEnd) 
	{
		int dx = u2.x-uu.x;
		int dy = u2.y-uu.y;
		
		Animation internal = new Animation(
				
				( perc, time) -> {
				  TurnsOfWar.ICONS_SPRITES.draw(TurnsOfWar.ICON_SHEET, (int)Math.round(16*(uu.x+perc*dx))+4,(int) Math.round(16*(uu.y+perc*dy))+4, 204 , 0, 0);
				},
				30,
				EasingFunctions.QUADRATIC_IN_OUT,
				() -> { if (good) UtilsToCleanup.sfx(4); else UtilsToCleanup.sfx(11);}
		);
		return Animation.sequential(Animation.parallel(makeAnimTxt(dmg, uu.x*16+8-5, uu.y*16, 3), makeShakeAnim(uu), null),internal,onEnd);
	}
	
	public static Animation makeUnitAppear(Coord c, Units unittype, boolean good, Runnable onEnd) 
	{
		Animation a = new Animation(
				( perc, time) -> {
					Sys.color(Colors.from(255,255,255, (int) (perc*255)));
				  TurnsOfWar.UNIT_SPRITES.draw(TurnsOfWar.UNIT_SHEET, c.x*16,c.y*16, unittype.spr , 0, good ? 0 : 1);
				  Sys.color(Colors.WHITE);
				},
				40,
				EasingFunctions.QUADRATIC,
				onEnd
			);
		return a;
	}
	
	public static Animation makeUnitDisappear(Unit c, Runnable onEnd) 
	{
		Animation a = new Animation(
				( perc, time) -> {
					c.ax = 1000;
					Sys.color(Colors.from(255,255,255, 255 - (int) (perc*255)));
					TurnsOfWar.UNIT_SPRITES.draw(TurnsOfWar.UNIT_SHEET, c.x*16,c.y*16, c.type.spr , 0, c.team == 0 ? 0 : 1);
					Sys.color(Colors.WHITE);
				},
				60,
				EasingFunctions.LINEAR,
				onEnd
			);
		return a;
	}
}