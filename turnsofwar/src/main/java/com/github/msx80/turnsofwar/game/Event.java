package com.github.msx80.turnsofwar.game;

import com.github.msx80.turnsofwar.animations.IAnimation;

/**
 * An event is something happening in the game. Like an unit moving, casting a spell, dying or appearing.
 * Each event is basically an animation, whose onBegin and onEnd callbacks can do stuff.
 * 
 * Every action from the user or the AI cause an event to occour, but possibly more then one (for example
 * an unit dying could trigger something, or a movement could trigger a trap).
 * 
 * Events are piled one after the other and "resolved" in order (ie executed).
 * When the win/lose condition is met, the event queue is cleared.
 * 
 *  For the sake of UX, similar Events can be merged, for example is a bomb kill 5 unit, waiting 5 death animations can be long,
 *  so we play them all in parallel. See merge method. 
 */
public class Event {
	
	public final IAnimation animation;

	public Event(IAnimation animation) {
		super();
		this.animation = animation;
	}
	
	/**
	 * If possible, merge this Event with the given one.
	 * @param another The merged event or null if events cannot be merged.
	 * @return
	 */
	public Event merge(Event another)
	{
		return null;
	}
}
