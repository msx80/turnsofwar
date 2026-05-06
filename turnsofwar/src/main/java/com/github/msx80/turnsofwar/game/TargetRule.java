package com.github.msx80.turnsofwar.game;
@FunctionalInterface
public interface TargetRule {
	
    boolean check(Unit unit, Unit target, int x, int y);
    
 // The helper "and" function
    default TargetRule and(TargetRule other) {
        // Returns a new lambda that only returns true if BOTH check out
        return (unit, target, x, y) -> 
            this.check(unit, target, x, y) && other.check(unit, target, x, y);
    }
    
    // Bonus: A helper "or" function for flexibility
    default TargetRule or(TargetRule other) {
        return (unit, target, x, y) -> 
            this.check(unit, target, x, y) || other.check(unit, target, x, y);
    }
}