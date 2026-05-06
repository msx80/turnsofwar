package com.github.msx80.turnsofwar.game;

import java.util.function.Function;

import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.turnsofwar.animations.AnimationFactory;
import com.github.msx80.turnsofwar.animations.IAnimation;

public enum Effects {
    SPENT("Spent", 16, 0, 1, "Unit already moved this turn", false),
    HEXEN("Hexen", 17, 0, 4, "Does 1 less damage", false),
    BLIND("Blind", 80, 0, 2, "Reduce range to 1.5", false),
    MORALE("Morale", 64, 6, -1, "Receive 1 less damage on any attack", true),
    ENSNARE("Ensnared", 32, 0, 3, "Unit will not be able to move", false),
    FOCUSED("Focused", 33, 0, 4, "Range of attacks increased by 1", false),
    FURIOUS("Furious", 65, 0, 4, "Attack dmg increased by 1", false),
    POISONED("Poisoned", 65, 0, 13, "Deal one damage per turn", false, u-> { 
    	
    	IAnimation a = AnimationFactory.makeShakeAndTxtAndParticleAnim(u, "POISON", 6, Colors.CRIMSON);
    	a.setOnEnd(() -> {u.damage(1);});
    	//Game.appendAnimation(a);
    	return a;
    	
    });

    public final String name;
    public final int spr;
    @Deprecated
    public final int bg;
    public final int turns; // -1 indicates permanent or special duration
    public final String desc;
    public final boolean isAreaEffect;
    public final Function<Unit, IAnimation> turner;

    Effects(String name, int spr, int bg, int turns, String desc, boolean isAreaEffect) {
    	this(name, spr, bg, turns, desc, isAreaEffect, null);
    }
    Effects(String name, int spr, int bg, int turns, String desc, boolean isAreaEffect, Function<Unit, IAnimation> turner) {
        this.name = name;
        this.spr = spr;
        this.bg = bg;
        this.turns = turns;
        this.desc = desc;
        this.isAreaEffect = isAreaEffect;
        this.turner = turner;
    }

    /**
     * Helper to check if the effect is permanent/infinite
     */
    public boolean isPermanent() {
        return this.turns == -1;
    }
}