package com.github.msx80.turnsofwar.game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.basicutils.MapDrawer;
import com.github.msx80.omicron.basicutils.MapDrawer.MapData;
import com.github.msx80.omicron.basicutils.MapDrawer.MapDataArray;
import com.github.msx80.omicron.basicutils.ShapeDrawer;
import com.github.msx80.omicron.basicutils.palette.Tic80;
import com.github.msx80.turnsofwar.TurnsOfWar;
import com.github.msx80.turnsofwar.animations.Animation;
import com.github.msx80.turnsofwar.animations.IAnimation;
import com.github.msx80.turnsofwar.ui.Button;
import com.github.msx80.turnsofwar.ui.Dialog;
import com.github.msx80.turnsofwar.ui.DialogType;
import com.github.msx80.turnsofwar.ui.Tooltip;

public class Game {
    // --- STATE ---
    public static int lvlNum;
    public static int currentTeam;
    public static int turnCount; // Used by ColumnTrap
    public static List<Unit> units = new ArrayList<>();
    public static List<TrapAction> activeTraps = new ArrayList<>();
    public static MapData map;
    public static WinCondition currentWinCondition;
    public static Runnable currentTurner;
    
    // UI/Selection State
    public static Unit selected = null;
    public static IAnimation anim = null;
    public static Dialog dialog = null;
    public static Map<Integer, List<Actions>> targets = null;
    public static List<Coord> radipix = null;
    public static List<Tooltip> tooltips = new ArrayList<Tooltip>();
    
    public static LinkedList<Event> eventQueue = new LinkedList<>();
    
    public static int jumpt;
	public static List<Button> buttons = new ArrayList<Button>();
    
	
	public static double inter(double a, double b, double p) {
		return b*p + a*(1.0-p);
	}
    
    // --- INITIALIZATION ---

    public static void makeGame(int levelIndex) {
    	eventQueue.clear();
    	anim = null;
    	dialog = null;
    	radipix = null;
        Level level = Levels.ALL.get(levelIndex);
        lvlNum = levelIndex;
        currentTeam = 0;
        turnCount = 1;
        selected = null;
        targets = null;
        
        
        // Setup Units
        units.clear();
        // Load Map
        map = loadMapFrom(level.mx, level.my);
        TurnsOfWar.mapDrawer = new MapDrawer(16, 16, 8, map);
        
        for (Level.UnitSpawn s : level.good) {
            addUnit(s.type, s.x, s.y, 0);
        }
        for (Level.UnitSpawn s : level.evil) {
            addUnit(s.type, s.x, s.y, 1);
        }
        
        // Setup Logic
        activeTraps = new ArrayList<>(level.traps);
        currentWinCondition = level.win;
        currentTurner = level.turner;
        
        sortY();
        recalcAreaEffects();
        
        showDialog(Arrays.asList(level.intro), null, DialogType.START);
        UtilsToCleanup.music((levelIndex + 1) % 2);
    }
    
    
    public static void showDialog(List<String> strs, Runnable onClose, DialogType type)
    {
    	 dialog = createDialog(strs, onClose, type);
    }
    
    public static Dialog createDialog(List<String> strs, Runnable onClose, DialogType type)
    {

    	  if (type == DialogType.POPUP) 
    	    UtilsToCleanup.sfx(1);
    	  
    	  final Dialog d = new Dialog(type, onClose, strs);
    	 
    	  return d;
    	 
    }
    

    // --- CORE QUERIES ---

    private static MapData loadMapFrom(int mx, int my) 
    {
		
    	MapDataArray map = new MapDataArray(11, 8);
    	for (int x = 0; x < 11; x++) {
    		for (int y = 0; y < 8; y++) {
    			// il primo layer contiene la mappa, il secondo gli oggetti statici
    			int tile = TurnsOfWar.globalMap.layer(0).data.getTile(mx+x, my+y);
    			map.setTile(x, y, tile);
    			
    			generateUnitFromLayer(mx, my, x, y, 1, -1); // neutral/landscape
    			generateUnitFromLayer(mx, my, x, y, 2, 0); // good guys
    			generateUnitFromLayer(mx, my, x, y, 3, 1); // baddies
    		}
		}
    	
    	handleCorners(map);
    	
		return map;
	}

	private static void generateUnitFromLayer(int mx, int my, int x, int y, int layer, int team) {
		int unitTile = TurnsOfWar.globalMap.layer(layer).data.getTile(mx+x, my+y);
		if(unitTile >= 0)
		{
			addUnit(Units.getBySpr(unitTile), x, y, team);
		}
	}

	private static void handleCorners(MapDataArray m) {
		Map<Coord, Integer> fixes = new HashMap<>();
		for (int x = 0; x < 11; x++) {
    		for (int y = 0; y < 8; y++) {
    			int tile = m.getTile(x, y);
    			if (tile == 1 || tile == 9 || tile == 17) // WATER
    			{
    				int delta = getBits(m, x, y);
    				if(delta > 0) fixes.put(new Coord(x, y), 80+delta);
    				
    			}
    			else if (tile == 2 || tile == 10 || tile == 18 || tile == 26) // DESERT - TODO use bits!
    			{
    				int delta = getBits(m, x, y);
    				if(delta > 0) fixes.put(new Coord(x, y), 96+delta);
    				
    			}
    		}
		}
		
		for (Entry<Coord, Integer> f : fixes.entrySet()) {
			Coord c = f.getKey();
			m.setTile(c.x, c.y, f.getValue());
		}
	}

	private static int getBits(MapDataArray m, int x, int y) {
		// find all nearby grass (tile 0)
		int res = 0;
		// up
		if (y>0 && isGrass(m.getTile(x, y-1))) {
			res+=1;
		}
		// right
		if (x>0 && isGrass(m.getTile(x-1, y))) {
			res+=8;
		}
		// down
		if (y<7 && isGrass(m.getTile(x, y+1))) {
			res+=4;
		}
		// left
		if (x<10 && isGrass(m.getTile(x+1, y))) {
			res+=2;
		}
		return res;
	}

	private static boolean isGrass(int tile) {
		return tile == 0 || tile == 8 || tile == 16 || tile == 24 || tile == 32; // TODO use bits
	}

	public static Unit unitAt(int tx, int ty) {
        for (Unit u : units) {
            if (u.x == tx && u.y == ty) return u;
        }
        return null;
    }

    public static int mapTile(int x, int y) {
        //int i = idx(x, y);
        //return (i >= 0 && i < baseTiles.length) ? baseTiles[i] : -1;
    	return map.getTile(x, y);
    }

    public static boolean isDefeated() {
        for (Unit u : units) {
            if (u.team == 0) return false;
        }
        return true;
    }

    public static int count(Units unitType) {
        int n = 0;
        for (Unit u : units) {
            if (u.type == unitType) n++;
        }
        return n;
    }

    // --- UNIT MANAGEMENT ---

    public static Unit addUnit(Units type, int x, int y, int team) {
        Unit u = new Unit(type, x, y, team);
        units.add(u);
        return u;
    }

    public static Unit addNear(Units type, int x, int y, int team) {
        if (unitAt(x, y) == null) return addUnit(type, x, y, team);
        
        for (int ax = Math.max(x - 1, 0); ax <= Math.min(x + 1, 10); ax++) {
            for (int ay = Math.max(y - 1, 0); ay <= Math.min(y + 1, 7); ay++) {
                if (unitAt(ax, ay) == null && TargetRules.WALKABLE.check(null, null, ax, ay)) {
                    return addUnit(type, ax, ay, team);
                }
            }
        }
        return null;
    }

    public static Coord freeNearBy(int x, int y) {
        List<Coord> res = new ArrayList<>();
        
        for (int ax = Math.max(x - 1, 0); ax <= Math.min(x + 1, 10); ax++) {
            for (int ay = Math.max(y - 1, 0); ay <= Math.min(y + 1, 7); ay++) {
                if (unitAt(ax, ay) == null && TargetRules.WALKABLE.check(null, null, ax, ay)) {
                    res.add(new Coord(ax, ay));
                }
            }
        }
        
        if (res.isEmpty()) return null;
        return res.get((int) (Math.random() * res.size()));
    }

    public static void removeUnit(Unit u) {
        units.remove(u);
    }

    public static void replace(Unit u, Units newType, int team) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i) == u) {
                units.set(i, new Unit(newType, u.x, u.y, team));
                return;
            }
        }
    }

    public static void pruneDeads() {
        /*for (int i = units.size() - 1; i >= 0; i--) {
            Unit u = units.get(i);
            if (u.hp <= 0 && u.team>=0) {
                if (u.type.onDie != null) u.type.onDie.accept(u);
                units.remove(i);
            }
        }*/
    }

    // --- LOGIC & EFFECTS ---

    public static void recalcAreaEffects() {
        for (Unit u : units) u.removeAreaEffects();

        for (Unit src : units) {
            for (Actions action : src.type.cap) {
                // If the action has an area effect (defined in Actions enum block)
                // We check if it matches the morale/buff logic from Lua
                if (action.areaEffect != null) {
                    applyAreaEffect(src, action.areaEffect);
                }
            }
        }
    }

    private static void applyAreaEffect(Unit src, Effects effect) {
        for (Unit u : units) {
            if (src != u && u.team == src.team) {
                // targetRange logic from Lua
                if (TargetRules.IN_RANGE.check(src, u, u.x, u.y)) {
                    u.addEffect(effect);
                }
            }
        }
    }

    /**
     * Collects all targets of a unit.
     * @param unit
     * @return
     */
    public static Map<Integer, List<Actions>> getTargets(Unit unit) {
        Map<Integer, List<Actions>> res = new HashMap<>();
        for (Actions a : unit.type.cap) {
            int cd = unit.cooldown.getOrDefault(a, 0);
            if (cd == 0) {
                for (int x = 0; x <= 10; x++) {
                    for (int y = 0; y <= 7; y++) {
                        Unit u2 = unitAt(x, y);
                        if (a.targetRule.check(unit, u2, x, y)) {
                            int pos = idx(x, y);
                            res.computeIfAbsent(pos, k -> new ArrayList<>()).add(a);
                        }
                    }
                }
            }
        }
        return res;
    }

    // --- GAME FLOW ---
/*
    public static void endAction(Actions action, Unit u) {
	     consumeAction(u, action);
	     moveEnded();
    }
*/
	public static List<Coord> calculateRadius(int fx, int fy, double r) {
	    List<Coord> pixels = new ArrayList<>();
	    
	    // 1. Determine which tiles are inside the radius
	    // Using 12x9 to match Lua's for x=0,11 and y=0,8
	    boolean[][] inout = new boolean[12][9];
	    
	    for (int x = 0; x <= 11; x++) {
	        for (int y = 0; y <= 8; y++) {
	            // Euclidean distance check
	            double d = Math.sqrt(Math.pow(fx - x, 2) + Math.pow(fy - y, 2));
	            inout[x][y] = d <= r;
	        }
	    }

	    // 2. Scan the grid to find boundaries and generate dotted line pixels
	    for (int x = 0; x <= 10; x++) {
	        for (int y = 0; y <= 7; y++) {
	            
	            // Vertical boundary (between current tile and the one to the right)
	            if (inout[x][y] != inout[x + 1][y]) {
	                // Generate dotted vertical line at the right edge of the tile
	                for (int i = 0; i < 16; i += 2) {
	                    pixels.add(new Coord(x * 16 + 16, y * 16 + i));
	                }
	            }
	            
	            // Horizontal boundary (between current tile and the one below)
	            if (inout[x][y] != inout[x][y + 1]) {
	                // Generate dotted horizontal line at the bottom edge of the tile
	                for (int i = 0; i < 16; i += 2) {
	                    pixels.add(new Coord(x * 16 + i, y * 16 + 16));
	                }
	            }
	        }
	    }
	    
	    return pixels;
	}
	
	public static void refreshTargets() {
	    // 1. Handle Action Targets
	    // Only show targets if a unit is selected and NOT spent
	    if (selected != null && !selected.hasEffect(Effects.SPENT)) {
	    	targets = getTargets(selected);
	    } else {
	    	targets = null;
	    }

	    // 2. Handle Range Radius Overlay
	    // In the original, radipix/radius draws the reach of the unit
	    if (selected != null && selected.type.range > 0) {
	        // We call the helper to calculate the circular overlay pixels/tiles
	        // radipix is likely a List of coordinates or a custom overlay object
	    	radipix = calculateRadius(selected.x, selected.y, selected.getRange());
	    } else {
	    	radipix = null;
	    }
	}
	
	
	public static void selectUnit(Unit u)
	{
	  selected = u;
	  jumpt = 0;
	  refreshTargets();
	}
	

	public static boolean checkGameStatus() {
    	pruneDeads();
    	sortY();
    	recalcAreaEffects();

    	if (currentWinCondition.isMet()) {
            doWin();
            return true;
        } else if (isDefeated()) {
            doDefeat();
            return true;
        }
     
    	return false;
    }

    
    public static void moveEnded() {
        System.out.println("Move ended!");
    	if (activeCountPerTeam(currentTeam)  == 0 )
    	{
    		halfTurnStarted();
    	}
		
    	if (currentTeam == 0) {
    	  // human turn, autoselect an unit
    	  if ( (selected!=null) && (selected.team == currentTeam) && selected.isActive() )
    	  {
    	    // selected unit hasn't done yet, keep selected
    		  refreshTargets();
    	  }
    	  else
    	  {
    		  // autoselect the first available unit
    	   for(Unit u : units)
    	   {
	    	   if ((u.team == currentTeam) && u.isActive() )
	    	   {
		    	     selectUnit(u);
		    		 break;
	    	   }
    	   } 
    	}
    	}
    	else
    	{
    		// AI turn, do something
    		if (activeCountPerTeam(currentTeam) == 0) 
    			moveEnded();
    		else
    			aiPlay();
    	}
    	
		
	}

	public static void checkTraps() {
		for (Iterator<TrapAction> iterator = activeTraps.iterator(); iterator.hasNext();) {
			TrapAction t = iterator.next();
			if(t instanceof ColumnTrap)
			{
				int c = ((ColumnTrap) t).colNum;
				for (int y = 0; y < 9; y++) 
				{
					Unit u = unitAt(c, y);
					if(u!=null && u.team == 0)
					{
						iterator.remove();
						Event e = t.trigger();
						if(e!=null)
						appendEvent( e );
						
						return;
					}
				}
			}
			else if (t instanceof TileTrap)
			{
				TileTrap tt = (TileTrap) t;
				Unit u = unitAt(tt.x, tt.y);
				if(u!=null && u.team == 0)
				{
					iterator.remove();
					Event e = t.trigger();
					if(e!=null)
					appendEvent( e );
					return;
				}
			}
		}
		
	}

	private static void halfTurnStarted() {
		radipix = null;
		currentTeam = (currentTeam +1 ) % 2;
		turnCount++;
		for (Unit u : units) {
			if (u.team == currentTeam) unSpendUnit(u);
		}
		// Run per-turn level logic
		if (currentTeam == 0) 
  	  	{
			fullTurnStarted();
  	  	}
	}

	private static void fullTurnStarted() {

		List<IAnimation> allAnims = new ArrayList<IAnimation>();
		// first apply effects
		for (Unit u : units) {
			for (Effects e : u.eff.keySet()) {
				if(e.turner != null)
				{
					IAnimation a = e.turner.apply(u);
					if(a!=null) allAnims.add(a);
				}
			}
		}

		Optional<IAnimation> par = allAnims.stream().reduce(Animation::parallel);
		
		if(par.isPresent())
		{
			par.get().setOnEnd(() -> 
			{
				
				pruneDeads();
		    	sortY();
		    	recalcAreaEffects();
		    	
		    	if (currentTurner != null) currentTurner.run();
				
			});
			setAnimation(par.get());
		}
		else
		{
			if (currentTurner != null) currentTurner.run();
		}
		
		
    	checkGameStatus();
    	
	}

	private static void unSpendUnit(Unit u) {
		// decrease cooldown
		for (Entry<Actions, Integer> c : u.cooldown.entrySet()) {
			if(c.getValue()>0) u.cooldown.put(c.getKey(), c.getValue()-1);
		}
		// reset freebies
		u.frees.clear();
		if (u.type.frees!=null)
		{
			u.frees.putAll(u.type.frees);
		}
		u.decEffects();
	}

	private static void aiPlay() {
		// this is ultra inefficient
		// recalculates the same things
		// again and again

		  List<Unit> units = activePerTeam(currentTeam);
		  
		  // attach targets to all units in field "ai"
		  for (Unit u : units) {
			  u.ai = targetsByAction(getTargets(u));
		  }
		  
		  
		  // first use units that can cast abilities
		  for (Unit u : units) {
		   if (aiTryCastSpell(u)) return;
		  }
		  
		  
		  // then use units that can already attack/range
		  for (Unit u : units) {
			  
		  
		   if (u.ai.containsKey(Actions.ATTACK))
		   {
			   aiAttack(u);
			   return;
		   }
		   if (u.ai.containsKey(Actions.RANGED))
		   {
		    aiRanged(u);
			return;
		   }
		  }
		  // if no attack/range, then move
		  for (Unit u : units) {
			  if (u.ai.containsKey(Actions.MOVE)) {
				  aiMove(u);
				  return;
			  }
		  }
		  
		  // TODO no move no attack ?
		  // consume all other active units
		  for (Unit u : units) {
			  u.addEffect(Effects.SPENT);
		  }
		  moveEnded();
		
	}

	private static void aiRanged(Unit u) {
		List<Integer> arr = u.ai.get(Actions.RANGED);
		  
		Coord c = bestTarget(u, arr);
				  
		appendEvent( Actions.RANGED.exec(u, c.x, c.y) );
	}
	
	
	

	private static void aiAttack(Unit u) {
		List<Integer> arr = u.ai.get(Actions.ATTACK);
				  
		Coord c = bestTarget(u, arr);
				  
		appendEvent( Actions.ATTACK.exec(u, c.x, c.y) );
		
	}

	private static Coord bestTarget(Unit u, List<Integer> targetIndices) {
		// choose best target:
		// by certainty that it can be killed
		// by value

		List<Unit> tars = coordsToUnitList(targetIndices);
		// tars now is array of target units
		  
		for (Unit t : tars) {
			if (t.hp<=u.type.attack.min)
			{
				// can probably be killed with certainty
				return new Coord(t.x, t.y);
		  	}
		}
		  
		// sort by value
		tars.sort((a,b) -> Integer.compare(b.type.aivalue, a.type.aivalue));

		return new Coord(tars.get(0).x,tars.get(0).y);
		
	}

	private static List<Unit> coordsToUnitList(List<Integer> targetIndices) {
		List<Unit> tars = new ArrayList<Unit>();
		  for (Integer i : targetIndices) {
			   Coord c = xdi(i);
			   tars.add( unitAt(c.x, c.y) );
		  }
		return tars;
	}

	private static void aiMove(Unit u) {
		List<Integer> idxs = u.ai.get(Actions.MOVE);
		// idxs are all possible movement targets
		int bestD = 10000;
		int bestI = -1;
		for (Integer id : idxs) {
			Coord c = xdi(id);
			int d = qdistToNearestEnemyBFS(c.x,c.y, u.team);
			if ((d<bestD) || (d==bestD && Math.random()>0.5)) // if more moves with same distance, randomize
			{
				bestD = d;
				bestI = id;
			}
		}
		Coord f = xdi(bestI);
		appendEvent( Actions.MOVE.exec(u, f.x, f.y) );
	}

	/**
	 * Finds the distance to the nearest enemy using a flood-fill (BFS) approach.
	 * * @param x Starting x coordinate
	 * @param y Starting y coordinate
	 * @param team The team of the unit looking for enemies
	 * @return The distance to the nearest enemy, or 1000 if none found.
	 */
	public static int qdistToNearestEnemyBFS(int x, int y, int team) {
		// Grid size is 11x8 = 88 tiles. Using 100 for safety.
		int arraySize = 100;
	    int[] m = new int[arraySize];
	    // Initialize map with -1 (representing nil/unvisited)
	    Arrays.fill(m,  -1);

	    int startIdx = idx(x, y);
	    m[startIdx] = 0;

	    // Iterative deepening / step-by-step expansion
	    for (int cur = 0; cur <= 100; cur++) { // max 100 steps
	        boolean changed = false;
	        
	        // We use a temporary list or array to store new findings for this "tick"
	        // to mimic the m1 logic in Lua
	        int[] m1 = new int[arraySize];
	        Arrays.fill(m1,  -1);

	        for (int k = 0; k < arraySize; k++) {
	            if (m[k] == cur) {
	            	Coord cc = xdi(k); 
	                int tx = cc.x;
	                int ty = cc.y;

	                // 1. Check if we found an enemy at this location
	                Unit u = unitAt(tx, ty);
	                if (u != null && u.team != team && u.team >= 0) {
	                    return cur;
	                }

	                // 2. Scan neighbors (8-way movement)
	                for (int ax = tx - 1; ax <= tx + 1; ax++) {
	                    for (int ay = ty - 1; ay <= ty + 1; ay++) {
	                        
	                        // Bounds check and skip self
	                        if (ax >= 0 && ay >= 0 && ax < 11 && ay < 8 && !(ax == tx && ay == ty)) {
	                            if (tilePathable(ax, ay)) {
	                                int targetIdx = idx(ax, ay);
	                                
	                                // Only mark if it hasn't been visited in 'm' yet
	                                if (m[targetIdx] == -1) {
	                                    m1[targetIdx] = cur + 1;
	                                    changed = true;
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }

	        // Apply m1 changes to m (like the Lua for k,v in pairs(m1) loop)
	        for (int i = 0; i < arraySize; i++) {
	            if (m1[i] != -1) m[i] = m1[i];
	        }

	        // Optimization: If no new tiles were marked this iteration, we can stop
	        if (!changed && cur > 0) break;
	    }

	    return 1000;
	}
	
	
	private static boolean tilePathable(int x, int y) {
		 Unit u = unitAt(x,y);

		 // static units
		 if (u!=null && u.team<0) return false; 
		 // check walkable
		 return TargetRules.WALKABLE.check(null, null, x, y);
		 
	}

	private static boolean aiTryCastSpell(Unit u) {
		for (Entry<Actions, List<Integer>> at : u.ai.entrySet()) {
			Actions a = at.getKey();
			if(a!=Actions.ATTACK && a!=Actions.MOVE && a!=Actions.RANGED) // TODO mettere una proprieta "isSpell"
			{
				  // we have a spell ready
				  // TODO choose a target?
				  Collections.shuffle(at.getValue()); // mix targets so it doesn't always choose the first good one
				  for (Integer coord : at.getValue()) {
					Coord c = xdi(coord);
					if (a.aiValidator.isValid(u, c.getX(), c.getY()))
					{
						appendEvent( a.exec(u, c.getX(), c.getY()) );
						return true;
					}
				}
			}
		}
		return false;
	
	}

	private static Map<Actions, List<Integer>> targetsByAction(Map<Integer, List<Actions>> targets2) {
		 // turn COORD->TARGET into TARGET->[COORDS]
		Map<Actions, List<Integer>> res = new HashMap<Actions, List<Integer>>();
		
		for (Entry<Integer, List<Actions>> t : targets2.entrySet()) {
			for (Actions a : t.getValue()) {
				res.computeIfAbsent(a, k -> new ArrayList<Integer>()).add(t.getKey());
			}
		}
		return res;
		
	}

	private static int activeCountPerTeam(int team) {
		int n = 0;
		for (Unit u : units) {
			if(u.team == team && u.isActive()) n++;
		}
		return n;
	}

	private static List<Unit> activePerTeam(int team) {
		List<Unit> n = new ArrayList<Unit>();
		for (Unit u : units) {
			if(u.team == team && u.isActive()) n.add(u);
		}
		return n;
	}


    public static void sortY() {
        units.sort(Comparator.comparingInt(u -> u.y));
    }

    public static Coord xdi(int idx)
    {
    	return new Coord( idx%12, idx / 12); // width
    }
    
    public static int idx(int x, int y) {
        return y * 12 + x;
    }

    public static void doWin() {
    	anim = null; // remove all pending things 
    	UtilsToCleanup.music(2, false);
    	radipix = null;
    	targets = null;
    	unlockLevel(lvlNum+1);
    	
    	showDialog(List.of("LEVEL COMPLETED!"), () -> {
    	   makeGame(lvlNum+1); },
    	  DialogType.END);
    }

    public static void unlockLevel(int i) {
		if(i>nextUnlockedLevel())
		{
			Sys.mem("nextLevel", i+"");
		}
	}

    public static int nextUnlockedLevel() {
		String nextLevel = Sys.mem("nextLevel");
		if(nextLevel == null || nextLevel.isEmpty()) nextLevel = "0";
		int nl = Integer.parseInt(nextLevel);
		return nl;
	}

	public static void doDefeat() {
    	Sys.stopMusic();
    	anim = null; // remove all pending things
    	radipix = null;
    	targets = null;
    	showDialog(List.of("YOU'VE BEEN DEFEATED!","","Too bad, the forces of evil", "overwhelmed you.", "", "Click to try again!"), () -> {
    		makeGame(lvlNum); },
    			DialogType.DEFEAT);
    		    
    }

    // Specific Win Logic helpers referenced in Levels
    public static boolean noMoreEnemies() {
        for (Unit u : units) if (u.team == 1) return false;
        return true;
    }
    
    public static boolean unitInTargetArea() {
        for (Unit u : units) {
            if (u.team == 0)
            {
            	int tile = mapTile(u.x, u.y);
            	if( tile == 32 ) return true; 
            }
        }
        return false;
    }

	public static void moveUnit(Unit u, int tx, int ty) {
		u.x = tx;
		u.y = ty;
		
	}

	public static void setAnimation(IAnimation anim2) {
		anim = anim2;
		
	}

	public static void consumeAction(Unit u, Actions act) {
		// 1. Handle Cooldowns
	    if (act.cooldown > 0) {
	        u.cooldown.put(act, act.cooldown);
	    }

	    // 2. Handle Free Actions
	    // Check if the unit has a map of free actions and if this specific action is in it
	    Map<Actions, Integer> frees = u.frees;
	    if (frees != null && frees.containsKey(act)) {
	        int remainingFrees = frees.get(act);
	        if (remainingFrees > 0) {
	            // Deduct one free use and exit without spending the unit
	            u.frees.put(act, remainingFrees - 1);
	            return;
	        }
	    }

	    // 3. Spend the Unit
	    // If no free actions were available, the unit is exhausted for the turn
	    u.addEffect(Effects.SPENT);
	}

	public static void tooltip(int x, int y, int w, int h, String text) {
		tooltips.add(new Tooltip(text, x, y, w, h));
		
	}

	public static void button(int x, int y, String text, String desc, Runnable cb) 
	{
		int w = 4+UtilsToCleanup.printBig(text, 0, -10, 15)+1; // TODO usare width
		button(x, y, text, w, desc, cb);
	}
	
	public static void button(int x, int y, String text, int w, String desc, Runnable cb) 
	{
		  int h = 8+2;
		  ShapeDrawer.rect(x,y,w,h,0, Tic80.P[6]);
		  ShapeDrawer.line(x,y,x+w-2,y, 0, Tic80.P[12]);
		  ShapeDrawer.line(x,y,x,y+h-2, 0, Tic80.P[12]);
		  
		  ShapeDrawer.line(x+1,y+h-1,x+w-1,y+h-1, 0, Tic80.P[4]);
		  ShapeDrawer.line(x+w-1,y+1,x+w-1,y+h-1, 0, Tic80.P[4]);
		  
		  UtilsToCleanup.printBig(text, x+2, y+2, 15);
		  
		  tooltip(x,y,w,h, desc);
		  buttons.add(new Button(text, x, y, w, h, cb));
	}

	public static void appendEvent(Event e) 
	{
		if(eventQueue.isEmpty())
		{
			eventQueue.add(e);
		}
		else
		{
			// check if events can be merged
			Event merged = eventQueue.getLast().merge(e);
			if(merged==null) 
			{
				eventQueue.add(e);
			}
			else
			{
				eventQueue.removeLast();
				eventQueue.add(merged);
			}
		}

	}

	public static void dieUnit(Unit u) 
	{
		if (u.type.onDie != null) u.type.onDie.accept(u);
		removeUnit(u);
	}
	
	

	public static void pickUpEvent() {
		// move the first event in the queue to the current animation, if available
		if(anim == null && !eventQueue.isEmpty())
		{
			Event first = eventQueue.removeFirst();
			anim = first.animation;
		}
	}

	public static void progressGame() {
		if( checkGameStatus() ) 
		{
			// the game has ended!
			eventQueue.clear();
		}
		else
		{
			checkTraps(); // could put more events on the event queue
			
			if(dialog == null && anim == null)
			{
				if(eventQueue.isEmpty())
				{
					// player or ai move has terminated all generated events, do next move
					moveEnded();
					pickUpEvent(); // moveEnded could have generated events, pick it up eventually
				}
				else
				{
					// animation ended but there's still events in the queue
					Event first = eventQueue.removeFirst();
					anim = first.animation;
				}
			}
		}
		
	}
   
}