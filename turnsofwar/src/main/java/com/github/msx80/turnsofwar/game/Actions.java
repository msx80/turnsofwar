package com.github.msx80.turnsofwar.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.msx80.turnsofwar.animations.Animation;
import com.github.msx80.turnsofwar.animations.AnimationFactory;
import com.github.msx80.turnsofwar.animations.IAnimation;

@FunctionalInterface
interface AiValidator {
    boolean isValid(Unit u, int tx, int ty);
}

public enum Actions 
{
    MOVE("Move", 192, "Move unit to nearby cell", 0,
        TargetRules.EMPTY.and((u, t, x, y) -> !u.hasEffect(Effects.ENSNARE))
            .and(TargetRules.ADJACENT).and(TargetRules.WALKABLE),
        (self, u, tx, ty) -> {
            UtilsToCleanup.sfxRandom(10);
            Animation anim = AnimationFactory.makeUnitMoveAnim(u, tx, ty, () -> {
                Game.moveUnit(u, tx, ty);
                Game.consumeAction(u, self);
            });
            return new Event(anim);
        }, null),

    ATTACK("Attack", 193, "Attack a nearby unit", 0,
            TargetRules.ENEMY.and(TargetRules.ADJACENT),
            (self, u, tx, ty) -> {
                Unit tar = Game.unitAt(tx, ty);
                int dmg = calculateDmg(u, tar);
                IAnimation anim = AnimationFactory.makeUnitAttackAnim(u, tar, dmg, () -> {
                    tar.damage(dmg);
                    Game.consumeAction(u, self);
                });
                return new UnitEvent(u, anim);
            }, null),

    BITE("Bite", 193, "Attack and heal himself of the same amount", 0,
            TargetRules.ENEMY.and(TargetRules.ADJACENT),
            (self, u, tx, ty) -> {
                Unit tar = Game.unitAt(tx, ty);
                int dmg = calculateDmg(u, tar);
                IAnimation anim = AnimationFactory.makeUnitAttackAnim(u, tar, dmg, () -> {
                    tar.damage(dmg);
                    
                });
                IAnimation a2 = AnimationFactory.makeAnimTxt("+"+dmg, u.x * 16 + 8 - 5, u.y * 16, 11);
                return new UnitEvent(u, Animation.sequential(anim, a2, () -> {
                	u.damage(-dmg);
                	Game.consumeAction(u, self);
                }) );
            }, null),

    RANGED("Ranged", 193, "Attack an enemy at distance", 0,
        TargetRules.IN_RANGE.and(TargetRules.ENEMY),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            int dmg = calculateDmg(u, tar);
            IAnimation anim = AnimationFactory.makeUnitRangedAnim(u, tar, dmg, () -> {
                tar.damage(dmg);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null),

    TELEPORT("Teleport", 208, "Teleport to any free cell in range", 2,
        TargetRules.EMPTY.and(TargetRules.IN_RANGE).and(TargetRules.WALKABLE),
        (self, u, tx, ty) -> {
            Animation anim = AnimationFactory.makeTeleportAnim(u, tx, ty, () -> {
                Game.moveUnit(u, tx, ty);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null),

    HEX("Hex", 209, "Cast HEX: enemy does -1 damage on attacks", 0,
        TargetRules.IN_RANGE.and(TargetRules.ENEMY),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "HEX", false, () -> {
                tar.addEffect(Effects.HEXEN);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null),

    MORALE("Morale", 209, "All friends in range gets MORALE effect", 0,
        TargetRules.NONE, 
        (self, u, tx, ty) -> { return null;}, // Logic usually handled in passive game loop
        null, Effects.MORALE),

    BOMB("Bomb", 209, "Throw a bomb, 4 dmg at center, 2 adjacents", 6,
        TargetRules.IN_RANGE.and(TargetRules.ENEMY),
        (self, u, tx, ty) -> {
            List<Unit> targets = new ArrayList<>();
            for (int x = tx - 1; x <= tx + 1; x++) {
                for (int y = ty - 1; y <= ty + 1; y++) {
                    Unit t = Game.unitAt(x, y);
                    if (t != null && t.team >= 0) targets.add(t);
                }
            }
            IAnimation anim = AnimationFactory.makeBombAnim(u, tx, ty, targets, () -> {
                for (Unit t : targets) {
                    int d = (tx == t.x && ty == t.y) ? 4 : 2;
                    t.damage(d);
                }
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null),

    ENSNARE("Ensnare", 209, "Cast ENSNARE: enemy cannot move", 2,
        TargetRules.IN_RANGE.and(TargetRules.ENEMY),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "ENSNARE", false, () -> {
                tar.addEffect(Effects.ENSNARE);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, (u, x, y) -> {
            Unit tar = Game.unitAt(x, y);
            return tar != null && !tar.hasEffect(Effects.ENSNARE);
        }),

    INK("Ink", 209, "Spits ink. Range reduced to 1.5", 2,
        TargetRules.IN_RANGE.and(TargetRules.ENEMY),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "INK", false, () -> {
                tar.addEffect(Effects.BLIND);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, (u, x, y) -> {
            Unit tar = Game.unitAt(x, y);
            return tar != null && !tar.hasEffect(Effects.BLIND) && tar.type.range > 0;
        }),

    SPLIT("Split", 208, "Split into two Slimes", 2,
        TargetRules.WALKABLE.and(TargetRules.ADJACENT).and(TargetRules.EMPTY),
        (self, u, tx, ty) -> {
        	Coord  tt = Game.freeNearBy(u.x, u.y);
            if (tt == null) {
                throw new RuntimeException("This should never happen, right?");
            } else {
            	IAnimation anim = AnimationFactory.makeUnitSplitAnim(u, tt, "SPLIT", true, () -> {
                    Unit sl = Game.addNear(u.type, tt.x, tt.y, 1);
                    Game.consumeAction(sl, ActionsSPLIT());
                    Game.consumeAction(u, self);
                });
                return new UnitEvent(u, anim);
            }
        }, (u, x, y) -> true),

    WAKEENT("WakeEnt", 208, "Transform a tree into an ENT", 8,
        TargetRules.TREE.and(TargetRules.IN_RANGE),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "WAKE", true, () -> {
                Game.replace(tar, Units.ENT, u.team);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null),

    STONECAST("Stonecast", 208, "Summon a boulder", 2,
        TargetRules.EMPTY.and(TargetRules.IN_RANGE).and(TargetRules.WALKABLE),
        (self, u, tx, ty) -> {
            
            IAnimation anim = AnimationFactory.makeUnitAppear(new Coord(tx, ty), Units.BOULDER, true,  
            		null);
            IAnimation x = Animation.sequential(
                    AnimationFactory.makeProjectileAnim(u, new Coord(tx,ty), true),
                    anim,
                    () -> {
                        Game.addUnit(Units.BOULDER, tx,ty, -1);
                        Game.consumeAction(u, self);
                    }
                );
            return new UnitEvent(u, x);
        }, null),

    RECALL("Recall", 208, "Transform a tomb into a Skeleton", 0,
        TargetRules.TOMB.and(TargetRules.IN_RANGE),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "RAISE", true, () -> {
                Game.replace(tar, Units.SKELETON, u.team);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, (u, x, y) -> true),

    SUMMONSEM("Summon", 208, "Summon a Sem Demon", 0,
        TargetRules.ADJACENT.and(TargetRules.WALKABLE).and(TargetRules.EMPTY),
        (self, u, tx, ty) -> {
        	IAnimation anim = AnimationFactory.makeUnitSummonAnim(u, new Unit(Units.SEM, tx, ty, 0), "SUMMON", true, () -> {
                Unit sl = Game.addNear(Units.SEM, tx, ty, u.team);
                sl.addEffect(Effects.SPENT);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, (u, x, y) -> true),

    HEAL("Heal", 208, "Heal 3 hp to target friend", 0,
        TargetRules.FRIEND.and(TargetRules.IN_RANGE).and(TargetRules.HURT),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "+3", true, () -> {
                tar.damage(-3);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null),

    HASTE("Haste", 208, "Give 2 free move actions to unspent friend", 2,
        TargetRules.FRIEND.and(TargetRules.IN_RANGE).and(TargetRules.UNSPENT),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "HASTE", true, () -> {
                int curr = tar.frees.getOrDefault(Actions.MOVE, 0);
                tar.frees.put(Actions.MOVE, curr + 2);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null),

    FOCUS("Focus", 208, "Unit with ranged attack gets +1 range", 2,
        TargetRules.FRIEND.and(TargetRules.IN_RANGE).and((unit, target, x, y) -> 
            target.type.cap.contains(Actions.RANGED)),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "FOCUS", true, () -> {
                tar.addEffect(Effects.FOCUSED);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null),

    FURY("Fury", 208, "Unit gets +1 attack", 0,
        TargetRules.FRIEND.and(TargetRules.IN_RANGE).and((unit, target, x, y) -> 
            target.type.attack != null),
        (self, u, tx, ty) -> {
            Unit tar = Game.unitAt(tx, ty);
            IAnimation anim = AnimationFactory.makeUnitMagicAnim(u, tar, "FURY", true, () -> {
                tar.addEffect(Effects.FURIOUS);
                Game.consumeAction(u, self);
            });
            return new UnitEvent(u, anim);
        }, null);

    // Fields
    public final String name;
    public final int icon;
    public final String desc;
    public final int cooldown;
    public final TargetRule targetRule;
    public final ActionExecutor executor;
    public final AiValidator aiValidator;
    public final Effects areaEffect;

    Actions(String name, int icon, String desc, int cooldown, TargetRule targetRule, ActionExecutor executor, AiValidator aiValidator, Effects areaEffect) {
        this.name = name;
        this.icon = icon;
        this.desc = desc;
        this.cooldown = cooldown;
        this.targetRule = targetRule;
        this.executor = executor;
        this.aiValidator = aiValidator != null ? aiValidator : (u, x, y) -> true;
        this.areaEffect = areaEffect; 
    }
    Actions(String name, int icon, String desc, int cooldown, TargetRule targetRule, ActionExecutor executor, AiValidator aiValidator) 
    {
       this(name, icon, desc, cooldown, targetRule, executor, aiValidator, null);
    }

    private static Actions ActionsSPLIT() {
		return Actions.SPLIT;
	}

	// Helper to calculate damage with effects
    private static int calculateDmg(Unit u, Unit tar) {
        int dmg = u.type.attack.getRandom(new Random());
        if (u.hasEffect(Effects.HEXEN)) dmg--;
        if (tar.hasEffect(Effects.MORALE)) dmg--;
        if (u.hasEffect(Effects.FURIOUS)) dmg++;
        return Math.max(0, dmg);
    }

    public Event exec(Unit u, int tx, int ty) {
        return this.executor.execute(this, u, tx, ty);
    }
}