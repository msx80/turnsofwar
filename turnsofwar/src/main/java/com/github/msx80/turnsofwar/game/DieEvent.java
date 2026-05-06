package com.github.msx80.turnsofwar.game;

import com.github.msx80.turnsofwar.animations.Animation;
import com.github.msx80.turnsofwar.animations.AnimationFactory;
import com.github.msx80.turnsofwar.animations.IAnimation;

public class DieEvent extends Event {

	private DieEvent(IAnimation animation) {
		super(animation);
	}

	public DieEvent(Unit unit) {
		super(AnimationFactory.makeUnitDisappear(unit, () -> {
        	Game.dieUnit(unit);
        }).setOnBegin( () -> { 
        	UtilsToCleanup.sfx(5); 
        	System.out.println("Suono morte");
        }));
	}

	@Override
	public Event merge(Event another) {
		
		if(another instanceof DieEvent)
		{
			// remove sounds from the two animations and put it on the parallel one
			this.animation.setOnBegin(null);
			another.animation.setOnBegin(null);
			IAnimation a = Animation.parallel(another.animation, this.animation);
			a.setOnBegin( () -> { 
	        	UtilsToCleanup.sfx(5); 
	        	System.out.println("Suono morte");
	        });
			return new DieEvent(a);
		}
		return null;
	}
	
}
