package com.github.msx80.turnsofwar.game;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public enum Units {
    // --- PLAYER UNITS ---
    SOLDIER("Soldier", new String[]{"Protecting", "our people!"}, 1, 6, 7, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(2, 4), 0, Map.of(Actions.MOVE, 1), 0),
    PALADIN("Paladin", new String[]{"I have seen", "the light!"}, 26, 6, 9, EnumSet.of(Actions.MOVE, Actions.ATTACK, Actions.HEAL), new Range(3, 4), 4, Map.of(Actions.MOVE, 1, Actions.HEAL, 1), 2),
    ELF("Elf", new String[]{"Pointy ears,", "pointy arrows"}, 10, 0, 5, EnumSet.of(Actions.MOVE, Actions.RANGED), new Range(1, 2), 4, Map.of(), 1),
    FARMER("Farmer", new String[]{"It's hard work", ""}, 13, 0, 3, EnumSet.of(Actions.MOVE), null, 0, Map.of(), -1),
    DOG("Dog", new String[]{"Woof! Woof!", ""}, 37, 0, 5, EnumSet.of(Actions.MOVE), null, 0, Map.of(Actions.MOVE, 1), -1),
    MAGE("Mage", new String[]{"Usually never", "arrives late."}, 2, 0, 4, EnumSet.of(Actions.MOVE, Actions.ATTACK, Actions.HEAL), new Range(1, 2), 2, Map.of(Actions.HEAL, 1), 10),
    BRUTE("Brute", new String[]{"Me brute! You", "dead!"}, 9, 0, 7, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(3, 5), 0, Map.of(), 0),
    VAMPIRE("Vampire", new String[]{"I'm thirsty", "for blood!"}, 8, 0, 5, EnumSet.of(Actions.MOVE, Actions.BITE), new Range(2, 3), 0, Map.of(Actions.MOVE, 1), 0),
    CYCLOP("Cyclop", new String[]{"How far are you?", ""}, 27, 0, 15, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(3, 5), 0, Map.of(), 0),
    DRUID("Druid", new String[]{"I am one with", "nature."}, 5, 0, 5, EnumSet.of(Actions.MOVE, Actions.WAKEENT), null, 3, Map.of(), 0),
    ENT("Ent", new String[]{"You can call me", "Treebeard."}, 12, 0, 8, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(1, 2), 0, Map.of(), 0),
    KING("King", new String[]{"Democratically", "elected."}, 6, 11, 8, EnumSet.of(Actions.MOVE, Actions.MORALE), null, 2.5, Map.of(), 2),
    DWARF("Dwarf", new String[]{"Strong and", "chunky."}, 15, 0, 8, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(2, 5), 0, Map.of(), 0),
    BOMBER("Bomber", new String[]{"Already lighting", "the wick!"}, 14, 0, 8, EnumSet.of(Actions.MOVE, Actions.ATTACK, Actions.BOMB), new Range(2, 3), 5, Map.of(), 2),
    CAPTAIN("Captain", new String[]{"Leading elves", "in battle!"}, 7, 0, 5, EnumSet.of(Actions.MOVE, Actions.RANGED, Actions.FOCUS), new Range(1, 2), 4, Map.of(), 3),
    MONK("Monk", new String[]{"Usually brewing", "beer."}, 4, 0, 5, EnumSet.of(Actions.MOVE, Actions.HEX), null, 6, Map.of(), 1),
    WITCH("Witch", new String[]{"Old clothes and", "uncombed hair."}, 3, 0, 4, EnumSet.of(Actions.MOVE, Actions.RANGED, Actions.FURY), new Range(1, 2), 5, Map.of(Actions.FURY, 1), 0),
    SHAOLIN("Shaolin", new String[]{"Watatatata!", ""}, 11, 6, 5, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(2, 3), 0, Map.of(Actions.ATTACK, 1), 1),
    SLUGDO("Slugdo", new String[]{"Not fast but", "furious."}, 25, 0, 8, EnumSet.of(Actions.MOVE, Actions.ATTACK, Actions.HASTE), new Range(1, 2), 3, Map.of(), 0),
    STONECASTER("Stonecaster", new String[]{"Let's rock", "and rock!"}, 16, 0, 9, EnumSet.of(Actions.MOVE, Actions.ATTACK, Actions.STONECAST), new Range(1, 2), 3d, Map.of(), 0),
    CENTAUR("Centaur", new String[]{"Just horsing", " around.."}, 0, 0, 6, EnumSet.of(Actions.MOVE, Actions.RANGED), new Range(1, 2), 2.5d, Map.of(Actions.MOVE, 1), 0),
    SEM("Sem Demon", new String[]{"Who are we?", ""}, 38, 0, 2, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(0, 1), 0, Map.of(Actions.ATTACK, 1), -1),
    SKELETON("Skeleton", new String[]{"Spooky and scary!", ""}, 17, 0, 4, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(1, 2), 0, Map.of(), 0),
    GOBLIN("Goblin", new String[]{"We fight for", "the eeeevil!"}, 19, 0, 6, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(2, 3), 0, Map.of(), 0),
    SPIDER("Spider", new String[]{"Fall into my", "net!"}, 21, 0, 6, EnumSet.of(Actions.MOVE, Actions.RANGED, Actions.ENSNARE), new Range(1, 3), 3.5, Map.of(), 1),
    SNAKE("Snake", new String[]{"Sssssss..", ""}, 20, 0, 3, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(2, 3), 0, Map.of(), 0),
    KOBOLD("Kobold", new String[]{"You'll be my", "pincushion."}, 18, 0, 6, EnumSet.of(Actions.MOVE, Actions.RANGED), new Range(1, 2), 4, Map.of(), 1),
    LICHKING("Lichking", new String[]{"Raise and fight,", "my deads!"}, 23, 2, 12, EnumSet.of(Actions.MOVE, Actions.ATTACK, Actions.RECALL), new Range(2, 3), 7, Map.of(), 3),
    RAT("Rat", new String[]{"Sqweak!", ""}, 22, 0, 3, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(1, 2), 0, Map.of(Actions.MOVE, 1), 0),
    ARCHDEMON("Archdemon", new String[]{"Straight from", "the deepest hell!"}, 30, 0, 30, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(3, 6), 0, Map.of(Actions.MOVE, 1, Actions.ATTACK, 1), 0),
    SLIME("Slime", new String[]{"Sblorchsh..", ""}, 29, 0, 1, EnumSet.of(Actions.MOVE, Actions.ATTACK, Actions.SPLIT), new Range(0, 1), 0, Map.of(), 0),
    ROGER("Roger", new String[]{"He's a", "chonky boy!"}, 32, 0, 99, EnumSet.of(Actions.MOVE, Actions.ATTACK), new Range(2,5), 0, Map.of(Actions.MOVE, 5, Actions.ATTACK, 5), 0),
    SLIME2("Slime2", new String[]{"Blobblll."}, 24, 0, 1, EnumSet.of(Actions.MOVE, Actions.ATTACK, Actions.SPLIT), new Range(0, 2), 3, Map.of(), 0, u -> {
    
    	/*
    	Game.appendEvent(new Event(AnimationFactory.makeUnitAppear(new Coord(u.x+1, u.y), SLIME, false, null)));
    	Game.appendEvent(new Event(AnimationFactory.makeUnitAppear(new Coord(u.x-1, u.y), SLIME, false, null)));
    	Game.appendEvent(new Event(AnimationFactory.makeUnitAppear(new Coord(u.x, u.y+1), SLIME, false, null)));
    	Game.appendEvent(new Event(AnimationFactory.makeUnitAppear(new Coord(u.x, u.y-1), SLIME, false, null)));
    	*/
    }),
    POLPO("Octopus", new String[]{"Come here", "for a hug!"}, 35, 0, 6, EnumSet.of(Actions.RANGED, Actions.INK), new Range(2, 2), 5, Map.of(), 0),
   
    // --- STRUCTURES / STATIC ---
    TREE("Tree", new String[]{"Just a regular", "tree."}, 49, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    PINE("Pine", new String[]{"Just a regular", "pine."}, 50, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    YELLOW_TREE("Tree", new String[]{"Is it autumn?", ""}, 65, 6, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    CASTLE("Castle", new String[]{"A King lives", "here."}, 55, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    WELL("Well", new String[]{"Fresh water", "from earth"}, 51, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    TOMB("Tomb", new String[]{"Very spooky.", ""}, 71, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    GLOOMYTREE("Gloomy Tree", new String[]{"Very gloomy.", ""}, 69, 6, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    BOULDER("Boulder", new String[]{"Like, a BIG", "rock."}, 54, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    SNOWYBOULDER("Boulder", new String[]{"A boulder", "covered in snow."}, 62, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    BOULDER_PDOR("Boulder", new String[]{"Huge rock.", ""}, 70, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    HOUSE("House", new String[]{"Villagers", "live here."}, 52, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    SNOWYHOUSE("House", new String[]{"A modest recover", "from the cold."}, 60, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    RUINS("Ruins", new String[]{"Who lives", "here??"}, 68, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    PORTAL("Portal", new String[]{"It regurgitates", "evilness!"}, 33, 0, 50, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    PDOR_STAND("Pdor Stand", new String[]{"Housing Pdor", "Son of Kmer"}, 63, 0, 0, EnumSet.noneOf(Actions.class), null, 0, Map.of(), 0),
    
    PDOR("Pdor", new String[]{"Pdor, son of Kmer", "of Instar tribe"}, 34, 0, 17, EnumSet.of(Actions.RANGED, Actions.SUMMONSEM), new Range(2, 4), 4, Map.of(Actions.SUMMONSEM, 2), 0, u -> {
    	
    	Unit stand = Game.unitAt(u.x, u.y+1);
    	Game.replace(stand, BOULDER_PDOR, stand.team);
    	

    });

    // Fields
    public final String name;
    public final String[] flavor;
    public final int spr;
    public final int bg;
    public final int hp;
    public final Set<Actions> cap;
    public final Range attack;
    public final double range;
    public final Map<Actions, Integer> frees;
    public final int aivalue;
    public final Consumer<Unit> onDie;

    Units(String name, String[] flavor, int spr, int bg, int hp, Set<Actions> cap, Range attack, double range, Map<Actions, Integer> frees, int aivalue) {
    	this(name, flavor, spr, bg, hp, cap, attack, range, frees, aivalue, null);
    }
    Units(String name, String[] flavor, int spr, int bg, int hp, Set<Actions> cap, Range attack, double range, Map<Actions, Integer> frees, int aivalue, Consumer<Unit> onDie) {
        this.name = name;
        this.flavor = flavor;
        this.spr = spr;
        this.bg = bg;
        this.hp = hp;
        this.cap = cap;
        this.attack = attack;
        this.range = range;
        this.frees = frees;
        this.aivalue = aivalue;
        this.onDie = onDie;
    }
    
    public static Units getBySpr(int n)
    {
    	for (Units u : Units.values()) {
			if(u.spr == n)
			{
				return u;
			}
		}
    	throw new RuntimeException("Unit not found by sprite: "+n);
    }
}