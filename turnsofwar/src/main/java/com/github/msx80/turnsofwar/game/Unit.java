package com.github.msx80.turnsofwar.game;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

public class Unit {
    // Core Data
    public Units type;        
    public int x, y;
    public int team;
    public int hp;
    
    // Animation offsets 
    public float ax = 0;
    public float ay = 0;

    // Effect and Action Tracking
    public Map<Effects, Integer> eff = new HashMap<>();
    public Map<Actions, Integer> frees = new HashMap<>();
    public Map<Actions, Integer> cooldown = new HashMap<>();
    
    // Da capire se usare Object 
    public Map<Actions, List<Integer>> ai = new HashMap<>();
	private boolean died = false;

    public Unit(Units type, int x, int y, int team) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.team = team;
        this.hp = type.hp;
        
        // Initialize unit (mimicking unSpendUnit)
        unspend();
    }

    // --- EFFECT MANAGEMENT ---

    public void addEffect(Effects effect) {
        // unit.eff[effect] = effect.turns
        this.eff.put(effect, effect.turns);
    }

    public boolean hasEffect(Effects effect) {
        return eff.containsKey(effect);
    }

    public void removeAreaEffects() {
        // Removes effects where effect.area is true
        eff.entrySet().removeIf(entry -> entry.getKey().isAreaEffect);
    }

    public void decEffects() {
        Iterator<Map.Entry<Effects, Integer>> it = eff.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Effects, Integer> entry = it.next();
            int remaining = entry.getValue();
            
            if (remaining == 1) {
                it.remove();
            } else if (remaining > 0) {
                entry.setValue(remaining - 1);
            }
            // If remaining is -1 (permanent), we do nothing
        }
    }

    // --- GAME LOGIC ---

    public void damage(int dmg) {
        this.hp -= dmg;
        // Clamp HP between 0 and Max HP
        if (this.hp <= 0) {
            this.hp = 0;
            if(!died)
            {
            	died  = true; // avoid multiple death events
            	Game.appendEvent(new DieEvent(this));
            	
            }
        } else if (this.hp > type.hp) {
            this.hp = type.hp;
        }
    }

    public double getRange() {
        if (this.hasEffect(Effects.BLIND)) {
            return Math.min(this.type.range, 1.5);
        }
        
        double bonus = 0;
        if (this.hasEffect(Effects.FOCUSED)) {
            bonus = 1.0;
        }
        return this.type.range + bonus;
    }

    public boolean isActive() {
        if (this.hasEffect(Effects.SPENT)) {
            return false;
        }
        // This requires your game logic to check if targets are available
        // return game.hasAvailableTargets(this);
        return true; 
    }

    public void unspend() {
        this.eff.remove(Effects.SPENT);
        // Reset free actions from the Unit Type definition
        this.frees.clear();
        if (this.type.frees != null) {
            this.frees.putAll(this.type.frees);
        }
    }

	public void removeEffect(Effects e) {
		this.eff.remove(e);
		
	}
	
	public boolean isEvil()
	{
		return team == 1;
	}

	public Coord getCoord() {
		
		return new Coord(x, y);
	}
	
}


