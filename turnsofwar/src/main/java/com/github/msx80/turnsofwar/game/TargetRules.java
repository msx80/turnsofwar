package com.github.msx80.turnsofwar.game;

import com.github.msx80.turnsofwar.TurnsOfWar;

public class TargetRules {

    public static final TargetRule ADJACENT = (unit, target, x, y) -> 
        Math.abs(unit.x - x) <=1 &&  Math.abs(unit.y - y) <=1 && (x!=unit.x || y!=unit.y);

    public static final TargetRule WALKABLE = (unit, target, x, y) -> {
        int tt = Game.mapTile(x, y);
        
        return TurnsOfWar.globalMap.layer(0).bit(tt, 0);
    };

    public static final TargetRule EMPTY = (unit, target, x, y) -> 
        target == null;

    public static final TargetRule TREE = (unit, target, x, y) -> 
        target != null && (target.type == Units.TREE || target.type == Units.PINE);

    public static final TargetRule TOMB = (unit, target, x, y) -> 
        target != null && target.type == Units.TOMB;

    public static final TargetRule HURT = (unit, target, x, y) -> 
        target != null && target.hp < target.type.hp;

    public static final TargetRule IN_RANGE = (unit, target, x, y) -> {
        double d = Math.sqrt(Math.pow(unit.x - x, 2) + Math.pow(unit.y - y, 2));
        return d <= unit.getRange();
    };

    public static final TargetRule ENEMY = (unit, target, x, y) -> 
        target != null && target.team >= 0 && target.team != unit.team;

    public static final TargetRule FRIEND = (unit, target, x, y) -> 
        target != null && target != unit && target.team == unit.team;

    public static final TargetRule UNSPENT = (unit, target, x, y) -> 
        target != null && !target.hasEffect(Effects.SPENT);

    public static final TargetRule NONE = (unit, target, x, y) -> false;


}