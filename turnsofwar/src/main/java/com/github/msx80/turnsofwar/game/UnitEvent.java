package com.github.msx80.turnsofwar.game;

import com.github.msx80.turnsofwar.animations.IAnimation;

/**
 * An event specifically originating by an Unit.
 * NB doesn't really work with merge() for now. See if we can fix it or remove
 */
public class UnitEvent extends Event {

	public final Unit unit;

	public UnitEvent(Unit u, IAnimation animation) 
	{
		super(animation);
		this.unit=u;
	}

}
